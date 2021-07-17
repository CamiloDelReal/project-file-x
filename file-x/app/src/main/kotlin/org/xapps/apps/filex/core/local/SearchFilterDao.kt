package org.xapps.apps.filex.core.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.xapps.apps.filex.core.models.SearchFilter


@Dao
interface SearchFilterDao {

    @Insert
    suspend fun insertAsync(filter: SearchFilter): Long

    @Insert
    fun insert(filter: SearchFilter): Long

    @Insert
    suspend fun insertAsync(filters: List<SearchFilter>): List<Long>

    @Insert
    fun insert(filters: List<SearchFilter>): List<Long>

    @Transaction
    @Query("SELECT * FROM search_filters")
    fun filtersFlow(): Flow<List<SearchFilter>>

    @Transaction
    @Query("SELECT * FROM search_filters")
    suspend fun filtersAsync(): List<SearchFilter>

    @Transaction
    @Query("SELECT * FROM search_filters")
    fun filters(): List<SearchFilter>

    @Update
    suspend fun updateAsync(filter: SearchFilter): Int

    @Update
    fun update(filter: SearchFilter): Int

    @Update
    suspend fun updateAsync(filters: List<SearchFilter>): Int

    @Update
    fun update(filters: List<SearchFilter>): Int

    @Delete
    suspend fun deleteAsync(filter: SearchFilter): Int

    @Delete
    fun delete(filter: SearchFilter): Int

    @Delete
    suspend fun deleteAsync(filters: List<SearchFilter>): Int

    @Delete
    fun delete(filters: List<SearchFilter>): Int

}