package org.xapps.apps.filex.views.bindings

import android.widget.TextView
import androidx.databinding.BindingAdapter
import org.xapps.apps.filex.R
import org.xapps.apps.filex.core.models.StorageDevice
import org.xapps.apps.filex.views.extensions.toStorageSize


object TextViewBindings {

    @JvmStatic
    @BindingAdapter("usedPercent")
    fun storageDeviceUsedPercent(view: TextView, device: StorageDevice) {
        val percent = ((device.totalSize - device.freeSize) * 100 / device.totalSize).toInt()
        view.text = String.format("%d%%", percent)
    }

    @JvmStatic
    @BindingAdapter("stats")
    fun storageDeviceStats(view: TextView, device: StorageDevice) {
        val free = device.freeSize.toStorageSize()
        val total = device.totalSize.toStorageSize()
        view.text = view.context.getString(R.string.stats_info, free, total)
    }

}