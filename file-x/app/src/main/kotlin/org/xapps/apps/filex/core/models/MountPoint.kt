package org.xapps.apps.filex.core.models


data class MountPoint (
    val path: String = "",
    val device: String = "",
    val allowReadWrite: Boolean = false,
    val fileSystem: String = ""
)