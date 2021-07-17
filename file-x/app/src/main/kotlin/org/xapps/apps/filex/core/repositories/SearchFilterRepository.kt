package org.xapps.apps.filex.core.repositories

import android.content.Context
import android.os.Environment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.xapps.apps.filex.R
import org.xapps.apps.filex.core.local.SearchFilterDao
import org.xapps.apps.filex.core.models.Bookmark
import org.xapps.apps.filex.core.models.SearchFilter
import org.xapps.apps.filex.core.utils.*


class SearchFilterRepository (
    private val dispatcher: CoroutineDispatcher,
    private val context: Context,
    private val searchFilterDao: SearchFilterDao
) {

    val searchFiltersFlow: Flow<List<SearchFilter>> =
        searchFilterDao.filtersFlow().flowOn(dispatcher)

    fun defaultSearchFilters(): List<SearchFilter> {
        return listOf(
            SearchFilter(
                name = context.getString(R.string.images),
                value = "*.jpg,*.jpeg,*.png",
                icon = ""
            ),
            SearchFilter(
                name = context.getString(R.string.videos),
                value = "*.mp4,*.mkv",
                icon = ""
            )
        )
    }

    suspend fun insertBookmarks(items: List<SearchFilter>): Either<Failure, List<SearchFilter>> = withContext(dispatcher) {
        info<SearchFilterRepository>("Insert $items")
        try {
            val ids = searchFilterDao.insertAsync(items)
            if(ids.size == items.size) {
                items.forEachIndexed { index, item -> item.id = ids[index] }
                info<SearchFilterRepository>("Search filters inserted successfully $items")
                items.toSuccess()
            } else {
                Failure.Database.toError()
            }
        } catch(ex: Exception) {
            error<SearchFilterRepository>(ex, "Exception captured")
            Failure.Exception(ex.localizedMessage).toError()
        }
    }

}