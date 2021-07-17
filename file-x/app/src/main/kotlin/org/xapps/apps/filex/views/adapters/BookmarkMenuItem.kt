package org.xapps.apps.filex.views.adapters

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import org.xapps.apps.filex.R
import org.xapps.apps.filex.core.models.Bookmark
import org.xapps.apps.filex.core.utils.debug
import org.xapps.apps.filex.databinding.ItemMenuBookmarkBinding


class BookmarkMenuItem(
    private val item: Bookmark
) : BindableItem<ItemMenuBookmarkBinding>() {


    override fun getLayout(): Int =
        R.layout.item_menu_bookmark

    override fun initializeViewBinding(view: View): ItemMenuBookmarkBinding =
        ItemMenuBookmarkBinding.bind(view)


    override fun bind(viewBinding: ItemMenuBookmarkBinding, position: Int) {
        debug<BookmarkMenuItem>("Bookmark menu item bind")
        viewBinding.bookmark = item
        viewBinding.mcvRoot.setOnClickListener {
            debug<BookmarkMenuItem>("Click on $item")
        }
    }

}