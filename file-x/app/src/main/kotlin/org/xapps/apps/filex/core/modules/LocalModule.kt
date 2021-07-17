package org.xapps.apps.filex.core.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import org.xapps.apps.filex.core.local.BookmarkDao
import org.xapps.apps.filex.core.local.LocalDatabaseService
import org.xapps.apps.filex.core.local.SearchFilterDao
import org.xapps.apps.filex.core.repositories.BookmarkRepository
import org.xapps.apps.filex.core.repositories.SearchFilterRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    companion object {
        private const val DB_FILENAME = "application_database.db"
    }

    @Singleton
    @Provides
    fun provideLocalDatabaseService(@ApplicationContext context: Context): LocalDatabaseService =
        Room.databaseBuilder(context, LocalDatabaseService::class.java, DB_FILENAME).build()

    @Singleton
    @Provides
    fun provideBookmarkDao(localDatabaseService: LocalDatabaseService): BookmarkDao =
        localDatabaseService.bookmarkDao()

    @Singleton
    @Provides
    fun provideSearchFilterDao(localDatabaseService: LocalDatabaseService): SearchFilterDao =
        localDatabaseService.searchFilterDao()

    @Singleton
    @Provides
    fun provideBookmarkRepository(@ApplicationContext context: Context, bookmarkDao: BookmarkDao): BookmarkRepository =
        BookmarkRepository(dispatcher = Dispatchers.IO, context = context, bookmarkDao = bookmarkDao)

    @Singleton
    @Provides
    fun provideSearchFilterRepository(@ApplicationContext context: Context, searchFilterDao: SearchFilterDao): SearchFilterRepository =
        SearchFilterRepository(dispatcher = Dispatchers.IO, context = context, searchFilterDao = searchFilterDao)

}