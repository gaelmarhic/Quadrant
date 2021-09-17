package com.gaelmarhic.quadrant.tasks

import com.gaelmarhic.quadrant.constants.GeneralConstants.PLUGIN_NAME
import com.gaelmarhic.quadrant.extensions.QuadrantConfigurationExtension
import com.gaelmarhic.quadrant.helpers.*
import com.gaelmarhic.quadrant.models.modules.RawModule
import com.gaelmarhic.quadrant.processors.GenerateActivityClassNameConstantProcessor
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import java.io.File

abstract class GenerateActivityClassNameConstants : DefaultTask() {

    @get:Nested
    abstract val configurationExtension: Property<QuadrantConfigurationExtension>

    @get:InputFile
    abstract val buildScript: RegularFileProperty

    @get:InputFiles
    abstract val manifestFiles: ListProperty<File>

    @get:OutputDirectory
    abstract val targetDirectory: DirectoryProperty

    @get:Input
    abstract val rawModules: ListProperty<RawModule>

    init {
        group = PLUGIN_NAME
        description = DESCRIPTION
    }

    @TaskAction
    fun generateConstants() {
        initProcessor().process(rawModules.get())
    }

    private fun initProcessor() = GenerateActivityClassNameConstantProcessor(
        manifestParsingHelper = ManifestParsingHelper(),
        manifestVerificationHelper = ManifestVerificationHelper(),
        activityFilteringHelper = ActivityFilteringHelper(
            configurationExtension = configurationExtension.get()
        ),
        constantFileDeterminationHelper = ConstantFileDeterminationHelper(
            configurationExtension = configurationExtension.get()
        ),
        constantGenerationHelper = ConstantGenerationHelper(targetDirectory.get().asFile)
    )

    companion object {

        private const val DESCRIPTION =
            "Generates files of constants that hold the Android Activities' full class name."
    }
}
