package com.gaelmarhic.quadrant.processors

import com.gaelmarhic.quadrant.helpers.ActivityFilteringHelper
import com.gaelmarhic.quadrant.helpers.ConstantFileDeterminationHelper
import com.gaelmarhic.quadrant.helpers.ConstantGenerationHelper
import com.gaelmarhic.quadrant.helpers.ManifestParsingHelper
import com.gaelmarhic.quadrant.models.modules.RawModule

class GenerateActivityClassNameConstantProcessor(
    private val manifestParsingHelper: ManifestParsingHelper,
    private val activityFilteringHelper: ActivityFilteringHelper,
    private val constantFileDeterminationHelper: ConstantFileDeterminationHelper,
    private val constantGenerationHelper: ConstantGenerationHelper
) {

    fun process(rawModules: List<RawModule>) {
        val parsedModules = manifestParsingHelper.parse(rawModules)
        val filteredModules = activityFilteringHelper.filter(parsedModules)
        val filesToBeGenerated = constantFileDeterminationHelper.determine(filteredModules)
        constantGenerationHelper.generate(filesToBeGenerated)
    }
}
