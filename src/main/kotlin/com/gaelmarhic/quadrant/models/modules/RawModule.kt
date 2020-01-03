package com.gaelmarhic.quadrant.models.modules

import java.io.File

data class RawModule(
    val name: String,
    val manifestFiles: List<File>
)
