package com.gaelmarhic.quadrant.models.modules

import java.io.File
import java.io.Serializable

data class RawModule(
    val name: String,
    val manifestFiles: List<File>
) : Serializable
