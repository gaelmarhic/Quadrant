package com.gaelmarhic.quadrant.models.modules

import com.gaelmarhic.quadrant.models.manifest.Manifest

data class ParsedModule(
    val name: String,
    val manifestList: List<Manifest>
)
