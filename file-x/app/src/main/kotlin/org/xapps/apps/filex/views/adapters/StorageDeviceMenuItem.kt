package org.xapps.apps.filex.views.adapters

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import org.xapps.apps.filex.R
import org.xapps.apps.filex.core.models.StorageDevice
import org.xapps.apps.filex.core.utils.debug
import org.xapps.apps.filex.databinding.ItemMenuStorageDeviceBinding


class StorageDeviceMenuItem(
    private val item: StorageDevice
) : BindableItem<ItemMenuStorageDeviceBinding>() {


    override fun getLayout(): Int =
        R.layout.item_menu_storage_device

    override fun initializeViewBinding(view: View): ItemMenuStorageDeviceBinding =
        ItemMenuStorageDeviceBinding.bind(view)


    override fun bind(viewBinding: ItemMenuStorageDeviceBinding, position: Int) {
        debug<StorageDeviceMenuItem>("Storage device menu item bind")
        viewBinding.device = item
        viewBinding.mcvRoot.setOnClickListener {
            debug<StorageDeviceMenuItem>("Click on $item")
        }
    }

}