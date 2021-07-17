package org.xapps.apps.filex.core.models


data class StorageDevice (
    val label: String,
    val type: Type,
    val isPrimary: Boolean,
    val isRemovable: Boolean,
    val totalSize: Long,
    val freeSize: Long,
    val mountPoint: MountPoint
) {

    enum class Type {
        Unknown,
        Root,
        Internal,
        SdCard,
        Usb
    }

}