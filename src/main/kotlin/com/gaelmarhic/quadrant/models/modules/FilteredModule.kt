package com.gaelmarhic.quadrant.models.modules

import com.gaelmarhic.quadrant.models.manifest.Activity

data class FilteredModule(
    val name: String,
    val filteredActivityList: List<Activity>
)
