package com.gaelmarhic.quadrant.tasks

import com.gaelmarhic.quadrant.constants.QuadrantConstants.PLUGIN_NAME
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GenerateActivityClassNameConstantTask : DefaultTask() {

    init {
        group = PLUGIN_NAME
        description = DESCRIPTION
    }

    @TaskAction
    fun generateConstants() {
        // TODO: To be implemented.
    }

    companion object {

        private const val DESCRIPTION = "Generates files of constants that hold the Android Activities' full class name."
    }
}
