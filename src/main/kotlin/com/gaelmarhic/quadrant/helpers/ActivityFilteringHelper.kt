package com.gaelmarhic.quadrant.helpers

import com.gaelmarhic.quadrant.models.modules.FilteredModule
import com.gaelmarhic.quadrant.models.modules.ParsedManifest
import com.gaelmarhic.quadrant.models.modules.ParsedModule

class ActivityFilteringHelper {

    fun filter(parsedModules: List<ParsedModule>) =
        parsedModules
            .map { it.filterActivities() }

    private fun ParsedModule.filterActivities() =
        applyFilter { noFilter }

    private fun ParsedModule.applyFilter(predicate: List<ParsedManifest>.() -> List<String>) = FilteredModule(
        name = name,
        filteredClassNameList = predicate(manifestList)
    )

    private val List<ParsedManifest>.noFilter
        get() = flatMap { manifest ->
            manifest
                .application
                .activityList
                .map { it.className }
        }
}
