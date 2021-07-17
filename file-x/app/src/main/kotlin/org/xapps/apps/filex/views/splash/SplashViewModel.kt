package org.xapps.apps.filex.views.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.xapps.apps.filex.R
import org.xapps.apps.filex.core.repositories.BookmarkRepository
import org.xapps.apps.filex.core.repositories.SearchFilterRepository
import org.xapps.apps.filex.core.repositories.SettingsRepository
import org.xapps.apps.filex.core.utils.error
import org.xapps.apps.filex.core.utils.info
import org.xapps.apps.filex.views.utils.Message
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val searchFilterRepository: SearchFilterRepository
): ViewModel() {

    private val _messageFlow: MutableSharedFlow<Message> = MutableSharedFlow(replay = 1)

    val messageFlow: SharedFlow<Message> = _messageFlow

    fun prepareApp() {
        viewModelScope.launch {
            _messageFlow.emit(Message.Loading)
            if (settingsRepository.isFirstTimeValue()) {
                val defaultBookmarks = bookmarkRepository.defaultBookmarks()
                val bookmarksResult = bookmarkRepository.insertBookmarks(defaultBookmarks)
                bookmarksResult.either({ failure ->
                    error<SplashViewModel>("Error received $failure")
                    _messageFlow.tryEmit((Message.Error(Exception(context.getString(R.string.error_creating_default_bookmarks)))))
                }, {
                    info<SplashViewModel>("Default bookmarks saved successfully")
                })

                val defaultSearchFilters = searchFilterRepository.defaultSearchFilters()
                val searchFiltersResult = searchFilterRepository.insertBookmarks(defaultSearchFilters)
                searchFiltersResult.either({ failure ->
                    error<SplashViewModel>("Error received $failure")
                    _messageFlow.tryEmit((Message.Error(Exception(context.getString(R.string.error_creating_default_search_filters)))))
                }, {
                    info<SplashViewModel>("Default search filters saved successfully")
                })

                _messageFlow.tryEmit((Message.Success()))
                settingsRepository.setIsFirstTime(false)
            } else {
                info<SplashViewModel>("It isn't the first time FileX is running in this device")
                _messageFlow.emit((Message.Success()))
            }
        }
    }

}