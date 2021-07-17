package org.xapps.apps.filex.core.models

import java.time.LocalDateTime


data class FileFolder (
    val name: String,
    val path: String,
    val type: Type,
    val extension: String,
    val size: Long,
    val lastModifiedDate: LocalDateTime,
    val createdDate: LocalDateTime,
    val lastReadDate: LocalDateTime
) {

    enum class Type {
        Unknown,
        Folder,
        Image,
        Video,
        Audio,
        Packed
    }

}