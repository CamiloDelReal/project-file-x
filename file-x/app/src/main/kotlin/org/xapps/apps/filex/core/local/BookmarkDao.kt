package org.xapps.apps.filex.core.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.xapps.apps.filex.core.models.Bookmark


@Dao
interface BookmarkDao {

    @Insert
    suspend fun insertAsync(bookmark: Bookmark): Long

    @Insert
    fun insert(bookmark: Bookmark): Long

    @Insert
    suspend fun insertAsync(bookmarks: List<Bookmark>): List<Long>

    @Insert
    fun insert(bookmarks: List<Bookmark>): List<Long>

    @Transaction
    @Query("SELECT * FROM bookmarks")
    fun bookmarksFlow(): Flow<List<Bookmark>>

    @Transaction
    @Query("SELECT * FROM bookmarks")
    suspend fun bookmarksAsync(): List<Bookmark>

    @Transaction
    @Query("SELECT * FROM bookmarks")
    fun bookmarks(): List<Bookmark>

    @Update
    suspend fun updateAsync(bookmark: Bookmark): Int

    @Update
    fun update(bookmark: Bookmark): Int

    @Update
    suspend fun updateAsync(bookmarks: List<Bookmark>): Int

    @Update
    fun update(bookmarks: List<Bookmark>): Int

    @Delete
    suspend fun deleteAsync(bookmark: Bookmark): Int

    @Delete
    fun delete(bookmark: Bookmark): Int

    @Delete
    suspend fun deleteAsync(bookmarks: List<Bookmark>): Int

    @Delete
    fun delete(bookmarks: List<Bookmark>): Int

}