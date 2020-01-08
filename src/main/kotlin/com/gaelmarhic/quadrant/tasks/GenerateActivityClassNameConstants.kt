package com.gaelmarhic.quadrant.tasks

import com.gaelmarhic.quadrant.constants.GeneralConstants.PLUGIN_CONFIG
import com.gaelmarhic.quadrant.constants.GeneralConstants.PLUGIN_NAME
import com.gaelmarhic.quadrant.constants.GeneralConstants.TARGET_DIRECTORY
import com.gaelmarhic.quadrant.extensions.QuadrantConfigurationExtension
import com.gaelmarhic.quadrant.helpers.*
import com.gaelmarhic.quadrant.models.modules.RawModule
import com.gaelmarhic.quadrant.processors.GenerateActivityClassNameConstantProcessor
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

open class GenerateActivityClassNameConstants : DefaultTask() {

    private val configurationExtension by lazy { retrieveConfigurationExtension() }

    private val rawModules by lazy { retrieveRawModules() }

    private val processor by lazy { initProcessor() }

    @InputFile
    val buildScript = project.buildFile

    @InputFiles
    val manifestFiles = rawModules?.flatMap { it.manifestFiles }

    @OutputDirectory
    val targetDirectory = project.buildDir.resolve(TARGET_DIRECTORY)

    init {
        group = PLUGIN_NAME
        description = DESCRIPTION
    }

    @TaskAction
    fun generateConstants() {
        rawModules?.let { processor.process(it) }
    }

    private fun retrieveConfigurationExtension() =
        project.extensions.findByName(PLUGIN_CONFIG) as QuadrantConfigurationExtension

    private fun retrieveRawModules() =
        project // This project is the project of the module where the plugin is applied.
            .parent
            ?.subprojects
            ?.map { it.toRawModule() }

    private fun Project.toRawModule() = RawModule(
        name = name,
        manifestFiles = manifestFiles
    )

    private val Project.manifestFiles: List<File>
        get() = projectDir
            .walk()
            .maxDepth(MANIFEST_FILE_DEPTH)
            .filter { it.name == MANIFEST_FILE_NAME }
            .toList()

    private fun initProcessor() = GenerateActivityClassNameConstantProcessor(
        manifestParsingHelper = ManifestParsingHelper(),
        manifestVerificationHelper = ManifestVerificationHelper(),
        activityFilteringHelper = ActivityFilteringHelper(
            configurationExtension = configurationExtension
        ),
        constantFileDeterminationHelper = ConstantFileDeterminationHelper(
            configurationExtension = configurationExtension
        ),
        constantGenerationHelper = ConstantGenerationHelper(targetDirectory)
    )

    companion object {

        private const val MANIFEST_FILE_DEPTH = 3
        private const val MANIFEST_FILE_NAME = "AndroidManifest.xml"
        private const val DESCRIPTION =
            "Generates files of constants that hold the Android Activities' full class name."
    }
}
