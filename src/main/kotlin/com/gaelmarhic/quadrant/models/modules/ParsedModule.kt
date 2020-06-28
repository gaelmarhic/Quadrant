package com.gaelmarhic.quadrant.models.modules

import com.gaelmarhic.quadrant.models.manifest.Application

data class ParsedModule(
    val name: String,
    val manifestList: List<ParsedManifest>
)

data class ParsedManifest(
    val path: String,
    val application: Application
)
