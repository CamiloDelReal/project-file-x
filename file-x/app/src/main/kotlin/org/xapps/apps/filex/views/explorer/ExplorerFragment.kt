package org.xapps.apps.filex.views.explorer

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.xapps.apps.filex.R
import org.xapps.apps.filex.core.models.Bookmark
import org.xapps.apps.filex.core.utils.debug
import org.xapps.apps.filex.databinding.FragmentExplorerBinding
//import org.xapps.apps.filex.views.adapters.BookmarkAdapter
import org.xapps.apps.filex.views.adapters.BookmarkMenuItem
import org.xapps.apps.filex.views.adapters.ExpandableMenuHeader
import org.xapps.apps.filex.views.adapters.SearchFilterMenuItem
import org.xapps.apps.filex.views.adapters.StorageDeviceMenuItem
import org.xapps.apps.filex.views.extensions.navigationBarColor
import org.xapps.apps.filex.views.extensions.setStatusBarForegoundColor
import javax.inject.Inject


@AndroidEntryPoint
class ExplorerFragment @Inject constructor(): Fragment() {

    private lateinit var bindings: FragmentExplorerBinding

    private val viewModel: ExplorerViewModel by viewModels()

    private var storageDevicesJob: Job? = null
    private var bookmarksJob: Job? = null
    private var searchFiltersJob: Job? = null

    private val onBackPressedCallback: OnBackPressedCallback by lazy {
        object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(bindings.dwrLayout.isDrawerOpen(GravityCompat.START)) {
                    bindings.dwrLayout.closeDrawer(GravityCompat.START)
                } else {
                    requireActivity().finish()
                }
            }
        }
    }

    private val menuHeaderItems: List<ExpandableGroup> by lazy {
        listOf(
            ExpandableGroup(ExpandableMenuHeader(getString(R.string.storage_devices)), true),
            ExpandableGroup(ExpandableMenuHeader(getString(R.string.bookmarks)), true),
            ExpandableGroup(ExpandableMenuHeader(getString(R.string.search_filters)), true)
        )
    }

    private val menuAdapter: GroupAdapter<GroupieViewHolder> by lazy {
        GroupAdapter<GroupieViewHolder>() .apply {
            add(menuHeaderItems[STORAGE_DEVICES_MENU_INDEX])
            add(menuHeaderItems[BOOKMARKS_MENU_INDEX])
            add(menuHeaderItems[SEARCH_FILTERS_MENU_INDEX])
        }
    }

    companion object {
        private val STORAGE_DEVICES_MENU_INDEX = 0
        private val BOOKMARKS_MENU_INDEX = 1
        private val SEARCH_FILTERS_MENU_INDEX = 2
    }

//    private val bookmarksListener: BookmarkAdapter.Listener by lazy {
//        object: BookmarkAdapter.Listener {
//            override fun clicked(item: Bookmark) {
//                debug<ExplorerFragment>("Click on $item")
//            }
//        }
//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindings = FragmentExplorerBinding.inflate(layoutInflater)
        bindings.lifecycleOwner = viewLifecycleOwner

        val theme = requireContext().theme
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorSurface, typedValue, true)
        val colorSurface = typedValue.data
        navigationBarColor(colorSurface)

        ViewCompat.setOnApplyWindowInsetsListener(bindings.clRoot, object : OnApplyWindowInsetsListener {

            override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
                val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                val navigationBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

                val statusBarLayoutParams = bindings.vStatusBg.layoutParams
                statusBarLayoutParams.height = statusBarHeight
                bindings.vStatusBg.layoutParams = statusBarLayoutParams

                val navigationBarLayoutParams = bindings.vNavigationBg.layoutParams
                navigationBarLayoutParams.height = navigationBarHeight
                bindings.vNavigationBg.layoutParams = navigationBarLayoutParams

                val menuStatusBarLayoutParams = bindings.clMenu.vMenuStatusBg.layoutParams
                menuStatusBarLayoutParams.height = statusBarHeight
                bindings.clMenu.vMenuStatusBg.layoutParams = menuStatusBarLayoutParams

                val menuNavigationBarLayoutParams = bindings.clMenu.vMenuNavigationBg.layoutParams
                menuNavigationBarLayoutParams.height = navigationBarHeight
                bindings.clMenu.vMenuNavigationBg.layoutParams = menuNavigationBarLayoutParams

                return ViewCompat.onApplyWindowInsets(v, insets)
            }
        })

        bindings.clMenu.btnAbout.setOnClickListener {
            findNavController().navigate(ExplorerFragmentDirections.actionExplorerFragmentToAboutFragment())
        }

        bindings.clMenu.rcvItems.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        bindings.clMenu.rcvItems.adapter = menuAdapter

        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storageDevicesJob = lifecycleScope.launchWhenResumed {
            viewModel.storageDevicesFlow.collectLatest { storageDevices ->
                debug<ExplorerFragment>("Devices received\n\t${storageDevices.joinToString("\n\t")}")
                menuHeaderItems[STORAGE_DEVICES_MENU_INDEX].replaceAll(storageDevices.map { device -> StorageDeviceMenuItem(device) })
            }
        }

        bookmarksJob = lifecycleScope.launchWhenResumed {
            viewModel.bookmarksFlow.collect { bookmarks ->
                debug<ExplorerFragment>("Bookmarks received\n\t${bookmarks.joinToString("\n\t")}")
                menuHeaderItems[BOOKMARKS_MENU_INDEX].replaceAll(bookmarks.map { bookmark -> BookmarkMenuItem(bookmark) })
            }
        }

        searchFiltersJob = lifecycleScope.launchWhenResumed {
            viewModel.searchFiltersFlow.collect { searchFilters ->
                debug<ExplorerFragment>("Search filters received\n\t${searchFilters.joinToString("\n\t")}")
                menuHeaderItems[SEARCH_FILTERS_MENU_INDEX].replaceAll(searchFilters.map { searchFilter -> SearchFilterMenuItem(searchFilter) })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setStatusBarForegoundColor(isLightStatusBar = false)
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        storageDevicesJob?.cancel()
        storageDevicesJob = null
        bookmarksJob?.cancel()
        bookmarksJob = null
        searchFiltersJob?.cancel()
        searchFiltersJob = null
    }
}