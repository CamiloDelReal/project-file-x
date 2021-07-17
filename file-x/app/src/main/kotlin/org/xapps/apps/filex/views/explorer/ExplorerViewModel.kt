package org.xapps.apps.filex.views.explorer

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.xapps.apps.filex.core.models.Bookmark
import org.xapps.apps.filex.core.models.SearchFilter
import org.xapps.apps.filex.core.models.StorageDevice
import org.xapps.apps.filex.core.repositories.BookmarkRepository
import org.xapps.apps.filex.core.repositories.SearchFilterRepository
import org.xapps.apps.filex.core.repositories.StorageRepository
import org.xapps.apps.filex.core.utils.debug
import javax.inject.Inject


@HiltViewModel
class ExplorerViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val searchFilterRepository: SearchFilterRepository
): ViewModel() {

    val storageDevicesFlow: SharedFlow<List<StorageDevice>> = storageRepository.storageDevicesFlow
    val bookmarksFlow: Flow<List<Bookmark>> = bookmarkRepository.bookmarksFlow
    val searchFiltersFlow: Flow<List<SearchFilter>> = searchFilterRepository.searchFiltersFlow

//    init {
//        viewModelScope.launch {
//            storageRepository.storageDevicesFlow.collectLatest {
//                debug<ExplorerViewModel>("Storage devices received $it")
//                storageDevices.clear()
//                storageDevices.addAll(it)
//            }
//        }
//        viewModelScope.launch {
//            bookmarkRepository.bookmarksFlow.collectLatest {
//                debug<ExplorerViewModel>("Bookmarks received $it")
//                bookmarks.clear()
//                bookmarks.addAll(it)
//            }
//        }
//    }
}