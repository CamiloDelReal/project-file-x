package org.xapps.apps.filex.views.bindings

import androidx.databinding.BindingAdapter
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import org.xapps.apps.filex.core.models.StorageDevice


object MaterialProgressBarBindings {

    @JvmStatic
    @BindingAdapter("usedPercent")
    fun storageDeviceUsedPercent(view: MaterialProgressBar, device: StorageDevice) {
        view.progress = ((device.totalSize - device.freeSize) * 100 / device.totalSize).toInt()
    }

}