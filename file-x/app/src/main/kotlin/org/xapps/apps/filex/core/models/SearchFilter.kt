package org.xapps.apps.filex.core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "search_filters")
data class SearchFilter (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "value")
    var value: String,

    @ColumnInfo(name = "icon")
    var icon: String
)