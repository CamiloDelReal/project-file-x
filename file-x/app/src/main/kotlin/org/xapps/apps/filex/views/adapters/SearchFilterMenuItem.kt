package org.xapps.apps.filex.views.adapters

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import org.xapps.apps.filex.R
import org.xapps.apps.filex.core.models.SearchFilter
import org.xapps.apps.filex.core.utils.debug
import org.xapps.apps.filex.databinding.ItemMenuSearchFilterBinding


class SearchFilterMenuItem(
    private val item: SearchFilter
) : BindableItem<ItemMenuSearchFilterBinding>() {


    override fun getLayout(): Int =
        R.layout.item_menu_search_filter

    override fun initializeViewBinding(view: View): ItemMenuSearchFilterBinding =
        ItemMenuSearchFilterBinding.bind(view)


    override fun bind(viewBinding: ItemMenuSearchFilterBinding, position: Int) {
        debug<SearchFilterMenuItem>("Search filter menu item bind")
        viewBinding.filter = item
        viewBinding.mcvRoot.setOnClickListener {
            debug<SearchFilterMenuItem>("Click on $item")
        }
    }

}