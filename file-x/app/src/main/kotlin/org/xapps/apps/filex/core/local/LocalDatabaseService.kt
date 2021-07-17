package org.xapps.apps.filex.core.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.xapps.apps.filex.core.models.Bookmark
import org.xapps.apps.filex.core.models.SearchFilter

@Database(
    entities = [
        Bookmark::class,
        SearchFilter::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabaseService : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao

    abstract fun searchFilterDao(): SearchFilterDao

}