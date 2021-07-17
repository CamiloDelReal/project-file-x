package org.xapps.apps.filex.core.repositories

import android.content.Context
import android.os.Environment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.xapps.apps.filex.R
import org.xapps.apps.filex.core.local.BookmarkDao
import org.xapps.apps.filex.core.models.Bookmark
import org.xapps.apps.filex.core.utils.*


class BookmarkRepository (
    private val dispatcher: CoroutineDispatcher,
    private val context: Context,
    private val bookmarkDao: BookmarkDao
) {

    val bookmarksFlow: Flow<List<Bookmark>> =
        bookmarkDao.bookmarksFlow().flowOn(dispatcher)

    fun defaultBookmarks(): List<Bookmark> {
        return listOf(
            Bookmark(
                name = context.getString(R.string.downloads),
                path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path ?: "",
                icon = ""
            )
        )
    }

    suspend fun insertBookmarks(items: List<Bookmark>): Either<Failure, List<Bookmark>> = withContext(dispatcher) {
        info<BookmarkRepository>("Insert $items")
        try {
            val ids = bookmarkDao.insertAsync(items)
            if(ids.size == items.size) {
                items.forEachIndexed { index, item -> item.id = ids[index] }
                info<BookmarkRepository>("Bookmarks inserted successfully $items")
                items.toSuccess()
            } else {
                Failure.Database.toError()
            }
        } catch(ex: Exception) {
            error<BookmarkRepository>(ex, "Exception captured")
            Failure.Exception(ex.localizedMessage).toError()
        }
    }

}