package org.xapps.apps.filex.views.adapters

import android.graphics.drawable.Animatable
import android.view.View
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem
import org.xapps.apps.filex.R
import org.xapps.apps.filex.core.utils.debug
import org.xapps.apps.filex.databinding.ItemExpandableMenuHeaderBinding


class ExpandableMenuHeader(
    private val title: String
) : BindableItem<ItemExpandableMenuHeaderBinding>() , ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun getLayout(): Int =
        R.layout.item_expandable_menu_header

    override fun initializeViewBinding(view: View): ItemExpandableMenuHeaderBinding =
        ItemExpandableMenuHeaderBinding.bind(view)

    override fun bind(viewBinding: ItemExpandableMenuHeaderBinding, position: Int) {
        viewBinding.txvTitle.text = title
        viewBinding.imgIcon.setImageResource(if (expandableGroup.isExpanded) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down)
        viewBinding.mcvHeaderLayout.setOnClickListener {
            debug<ExpandableMenuHeader>("Click on section $title")
            expandableGroup.onToggleExpanded()
            viewBinding.imgIcon.setImageResource(if (expandableGroup.isExpanded) R.drawable.ic_collapse_animated else R.drawable.ic_expand_animated)
            val drawable = viewBinding.imgIcon.drawable as Animatable
            drawable.start()
//            changeStuff(viewHolder)
        }
    }

    private fun changeStuff(viewHolder: GroupieViewHolder) {

//        viewHolder.root.indicator.apply {
//            setImageResource(
//                if (expandableGroup.isExpanded) R.drawable.ic_expanded_indicator
//                else R.drawable.ic_collapsed_indicator)
//        }

    }

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        this.expandableGroup = onToggleListener
    }

}