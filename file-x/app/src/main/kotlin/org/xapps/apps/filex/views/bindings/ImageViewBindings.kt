package org.xapps.apps.filex.views.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import org.xapps.apps.filex.R
import org.xapps.apps.filex.core.models.StorageDevice


object ImageViewBindings {

    @JvmStatic
    @BindingAdapter("deviceType")
    fun storageDeviceStype(view: ImageView, device: StorageDevice) {
        view.setImageResource(
            when(device.type) {
                StorageDevice.Type.Unknown -> R.drawable.ic_alert_octagon
                StorageDevice.Type.Root -> R.drawable.ic_lock
                StorageDevice.Type.Internal -> R.drawable.ic_cellphone_android
                StorageDevice.Type.SdCard -> R.drawable.ic_micro_sd
                StorageDevice.Type.Usb -> R.drawable.ic_usb
            }
        )
    }

}