package com.gaelmarhic.quadrant.models.modules

import java.io.File
import java.io.Serializable

data class RawModule(
    val name: String,
    val manifestFiles: List<File>,
    // A namespace which is used as the Kotlin or Java package name.
    // See https://developer.android.com/build/configure-app-module#set-namespace
    val namespace: String = "",
) : Serializable
