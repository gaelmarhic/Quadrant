package com.gaelmarhic.quadrant.helpers

import com.gaelmarhic.quadrant.models.manifest.Activity
import com.gaelmarhic.quadrant.models.manifest.Manifest
import com.gaelmarhic.quadrant.models.modules.FilteredModule
import com.gaelmarhic.quadrant.models.modules.ParsedModule

class ActivityFilteringHelper {

    fun filter(parsedModules: List<ParsedModule>) =
        parsedModules
            .map { it.filterActivities() }

    private fun ParsedModule.filterActivities() =
        applyFilter { noFilter }

    private fun ParsedModule.applyFilter(predicate: List<Manifest>.() -> List<Activity>) = FilteredModule(
        name = name,
        filteredActivityList = predicate(manifestList)
    )

    private val List<Manifest>.noFilter
        get() = this.flatMap { it.application.activityList }
}
