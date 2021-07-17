package org.xapps.apps.filex.core.repositories

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Parcel
import android.os.StatFs
import android.os.storage.StorageManager
import com.skydoves.whatif.addAllWhatIfNotNull
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.xapps.apps.filex.core.models.MountPoint
import org.xapps.apps.filex.core.models.StorageDevice
import java.io.File
import java.util.*


class StorageRepository(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher
) {

    private val _storageDevicesFlow: MutableSharedFlow<List<StorageDevice>> = MutableSharedFlow(replay = 1)
    val storageDevicesFlow: SharedFlow<List<StorageDevice>> = _storageDevicesFlow

    private var coroutineScope: CoroutineScope? = null

    private val mediaFilter = IntentFilter().apply {
        addAction(Intent.ACTION_MEDIA_MOUNTED)
        addAction(Intent.ACTION_MEDIA_UNMOUNTED)
        addDataScheme("file")
    }

    private val usbFilter = IntentFilter().apply {
        addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
    }

    private val mediaDeviceReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.action?.let {
                if(it == Intent.ACTION_MEDIA_MOUNTED || it == Intent.ACTION_MEDIA_UNMOUNTED) {
                    updateStorageDevices()
                }
            }
        }
    }

    private val usbDeviceReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.action?.let {
                if(it == UsbManager.ACTION_USB_DEVICE_ATTACHED || it == UsbManager.ACTION_USB_DEVICE_DETACHED) {
                    updateStorageDevices()
                }
            }
        }
    }

    init {
        registerReceivers()
        updateStorageDevices()
    }

    private fun updateStorageDevices() {
        coroutineScope?.cancel()
        coroutineScope = CoroutineScope(Job() + dispatcher)
        coroutineScope?.launch {
            retrieveStorageDevices()
        }
    }

    private fun registerReceivers() {
        context.registerReceiver(mediaDeviceReceiver, mediaFilter)
        context.registerReceiver(usbDeviceReceiver, usbFilter)
    }

    fun unregisterReceivers() {
        context.unregisterReceiver(mediaDeviceReceiver)
        context.unregisterReceiver(usbDeviceReceiver)
    }

    suspend fun retrieveStorageDevices() = withContext(dispatcher) {
        val storageDevices = storageDevices()
        _storageDevicesFlow.emit(storageDevices)
    }

    private fun storageDevices(): List<StorageDevice> {
        val storageDevices = mutableListOf<StorageDevice>()

        val mountedPoints = mountedPoints()
        val rootPath = "/"
        if(mountedPoints.containsKey(rootPath)) {
            val rootPoint = mountedPoints[rootPath]
            val stats = StatFs(rootPath)
            val device = StorageDevice(
                label = "Root",
                type = StorageDevice.Type.Root,
                isPrimary = false,
                isRemovable = false,
                totalSize = stats.totalBytes,
                freeSize = stats.availableBytes,
                mountPoint = rootPoint!!
            )
            storageDevices.add(device)
        }

        val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val storageVolumes = storageManager.storageVolumes

        storageVolumes.forEach { volume ->
            val volumePath = if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                val getPathMethod = volume.javaClass.getMethod("getPath")
                getPathMethod.invoke(volume) as String
            } else {
                volume.directory?.path ?: ""
            }

            val volumePoint = if(mountedPoints.containsKey(volumePath)) {
                mountedPoints[volumePath]!!
            } else {
                val emulatedPattern = Regex(".*emulated\\/\\d+$")
                if(emulatedPattern.matches(volumePath)) {
                    val emulatedSuffixRemover = Regex("\\/\\d+$")
                    val mountedPointPath = emulatedSuffixRemover.replace(volumePath, "")
                    if(mountedPoints.containsKey(mountedPointPath)) {
                        val mountedPoint = mountedPoints[mountedPointPath]
                        MountPoint(
                            path = volumePath,
                            device = mountedPoint!!.device,
                            allowReadWrite = mountedPoint.allowReadWrite,
                            fileSystem = mountedPoint.fileSystem
                        )
                    } else {
                        MountPoint(path = volumePath)
                    }
                } else {
                    MountPoint(path = volumePath)
                }
            }

            val label = volume.getDescription(context)
            val isRemovable = volume.isRemovable
            val stats = StatFs(volumePoint.path)
            val isPrimary = volume.isPrimary
            val type = if(!isRemovable) {
                StorageDevice.Type.Internal
            } else {
                val usbPattern = Regex(".*usb.*|.*media_rw.*")
                if(usbPattern.matches(volumePoint.path)) {
                    StorageDevice.Type.Usb
                } else {
                    StorageDevice.Type.SdCard
                }
            }

            storageDevices.add(
                StorageDevice(
                    label = label,
                    type = type,
                    isPrimary = isPrimary,
                    isRemovable = isRemovable,
                    totalSize = stats.totalBytes,
                    freeSize = stats.availableBytes,
                    mountPoint = volumePoint
                ))
        }

        return storageDevices
    }

    private fun mountedPoints(): Map<String, MountPoint> {
        var mountsFile = File("/proc/mounts")
        var mountsFileScanner: Scanner? = null
        val points = mutableMapOf<String, MountPoint>()

        if(!mountsFile.exists()) {
            mountsFile = File("/proc/self/mounts")
            if(mountsFile.exists()) {
                mountsFileScanner = Scanner(mountsFile)
            }
        } else {
            mountsFileScanner = Scanner(mountsFile)
        }

        mountsFileScanner?.let { scanner->
            while(scanner.hasNextLine()) {
                val line = scanner.nextLine()
                val lineSections = line.split(" ")
                if(lineSections.size >= 5) {
                    val device = lineSections[0]
                    var jump = 1
                    if(lineSections[jump] == "on") {
                        jump++
                    }
                    val path = lineSections[jump]
                    jump++
                    if(lineSections[jump] == "type") {
                        jump++
                    }
                    val fileSystem = lineSections[jump]
                    jump++
                    val allowReadWrite = lineSections[jump].contains("rw")
                    points[path] = MountPoint(path = path, device = device, allowReadWrite = allowReadWrite, fileSystem = fileSystem)
                }
            }
        }

        mountsFileScanner?.close()

        return points
    }

}