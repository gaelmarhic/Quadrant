package com.gaelmarhic.quadrant.helpers

import com.gaelmarhic.quadrant.constants.ModelConstants.METADATA_NAME_ATTRIBUTE_ADDRESSABLE_VALUE
import com.gaelmarhic.quadrant.extensions.QuadrantConfigurationExtension
import com.gaelmarhic.quadrant.helpers.ActivityFilteringHelper.Addressability.*
import com.gaelmarhic.quadrant.models.manifest.Activity
import com.gaelmarhic.quadrant.models.manifest.MetaData
import com.gaelmarhic.quadrant.models.modules.FilteredModule
import com.gaelmarhic.quadrant.models.modules.ParsedManifest
import com.gaelmarhic.quadrant.models.modules.ParsedModule
import com.gaelmarhic.quadrant.constants.Miscellaneous.FALSE as BOOLEAN_FALSE
import com.gaelmarhic.quadrant.constants.Miscellaneous.TRUE as BOOLEAN_TRUE

class ActivityFilteringHelper(
    private val configurationExtension: QuadrantConfigurationExtension
) {

    fun filter(parsedModules: List<ParsedModule>) =
        parsedModules
            .map { it.applyFilter() }

    private fun ParsedModule.applyFilter() = FilteredModule(
        name = name,
        filteredClassNameList = manifestList.filterClassNames()
    )

    private fun List<ParsedManifest>.filterClassNames() =
        mutableListOf<String>().apply {
            forEachActivity { className, isAddressable ->
                if (isAddressable) {
                    add(className)
                }
            }
        }

    private fun List<ParsedManifest>.forEachActivity(block: (String, Boolean) -> Unit) =
        map { it.application }
            .flatMap { it.activityList }
            .groupBy { it.className }
            .forEach {
                block(
                    it.key,
                    isActivityAddressable(
                        activityAddressability = it.value.toActivityAddressability(),
                        applicationAddressability = toApplicationAddressability()
                    )
                )
            }

    private fun isActivityAddressable(
        activityAddressability: Addressability,
        applicationAddressability: Addressability
    ) =
        when (activityAddressability) {
            TRUE -> true
            FALSE -> false
            UNDEFINED -> {
                when (applicationAddressability) {
                    TRUE -> true
                    FALSE -> false
                    UNDEFINED -> configurationExtension.generateByDefault
                }
            }
        }

    private fun List<Activity>.toActivityAddressability() =
        flatMap { it.metaDataList }
            .toAddressability()

    private fun List<ParsedManifest>.toApplicationAddressability() =
        map { it.application }
            .flatMap { it.metaDataList }
            .toAddressability()

    private fun List<MetaData>.toAddressability() =
        when (firstOrNull { it.name == METADATA_NAME_ATTRIBUTE_ADDRESSABLE_VALUE }?.value) {
            BOOLEAN_TRUE -> TRUE
            BOOLEAN_FALSE -> FALSE
            else -> UNDEFINED
        }

    private enum class Addressability { TRUE, FALSE, UNDEFINED }
}
