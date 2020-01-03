package com.gaelmarhic.quadrant.helpers

import com.gaelmarhic.quadrant.constants.GeneralConstants.PLUGIN_NAME
import com.gaelmarhic.quadrant.models.generation.ConstantToBeGenerated
import com.gaelmarhic.quadrant.models.generation.FileToBeGenerated
import com.gaelmarhic.quadrant.models.manifest.Activity
import com.gaelmarhic.quadrant.models.modules.FilteredModule

class ConstantFileDeterminationHelper {

    fun determine(filteredModules: List<FilteredModule>) =
        filteredModules
            .toSingleFile()

    private fun List<FilteredModule>.toSingleFile() = listOf(
        FileToBeGenerated(
            name = SINGLE_FILE_NAME,
            constantList = this
                .flatMap { it.filteredActivityList }
                .map { it.toConstant() }
        )
    )

    private fun Activity.toConstant() = ConstantToBeGenerated(
        name = name.formatConstantName(),
        value = name
    )

    private fun String.formatConstantName() =
        split(PACKAGE_SEPARATOR)
            .last()
            .mapIndexed { index, letter -> formatLetter(index, letter) }
            .joinToString(EMPTY_SEPARATOR)

    private fun formatLetter(index: Int, letter: Char) =
        if (letter.isUpperCase() && index != 0) "_$letter" else letter.toUpperCase().toString()

    companion object {

        private const val SINGLE_FILE_NAME = "${PLUGIN_NAME}Constants"
        private const val PACKAGE_SEPARATOR = "."
        private const val EMPTY_SEPARATOR = ""
    }
}
