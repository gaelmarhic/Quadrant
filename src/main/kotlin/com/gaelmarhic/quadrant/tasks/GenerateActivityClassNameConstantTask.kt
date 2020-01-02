package com.gaelmarhic.quadrant.tasks

import com.gaelmarhic.quadrant.constants.QuadrantConstants.PLUGIN_NAME
import com.gaelmarhic.quadrant.extensions.QuadrantConfigurationExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class GenerateActivityClassNameConstantTask : DefaultTask() {

    private val configurationExtension by lazy { retrieveConfigurationExtension() }

    init {
        group = PLUGIN_NAME
        description = DESCRIPTION
    }

    @TaskAction
    fun generateConstants() {
        // TODO: To be implemented.
    }

    private fun retrieveConfigurationExtension() =
        project.extensions.findByName(PLUGIN_NAME) as QuadrantConfigurationExtension

    companion object {

        private const val DESCRIPTION =
            "Generates files of constants that hold the Android Activities' full class name."
    }
}
