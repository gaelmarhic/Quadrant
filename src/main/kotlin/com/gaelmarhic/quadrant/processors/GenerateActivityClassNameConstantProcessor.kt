package com.gaelmarhic.quadrant.processors

import com.gaelmarhic.quadrant.helpers.*
import com.gaelmarhic.quadrant.models.modules.RawModule

class GenerateActivityClassNameConstantProcessor(
    private val manifestParsingHelper: ManifestParsingHelper,
    private val manifestVerificationHelper: ManifestVerificationHelper,
    private val activityFilteringHelper: ActivityFilteringHelper,
    private val constantFileDeterminationHelper: ConstantFileDeterminationHelper,
    private val constantGenerationHelper: ConstantGenerationHelper
) {

    fun process(rawModules: List<RawModule>) {
        val parsedModules = manifestParsingHelper.parse(rawModules)
        manifestVerificationHelper.verify(parsedModules)
        val filteredModules = activityFilteringHelper.filter(parsedModules)
        val filesToBeGenerated = constantFileDeterminationHelper.determine(filteredModules)
        constantGenerationHelper.generate(filesToBeGenerated)
    }
}
