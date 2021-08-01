package com.gaelmarhic.quadrant.extensions

import org.gradle.api.tasks.Input
import java.io.Serializable

open class QuadrantConfigurationExtension : Serializable {

    @get:Input
    var generateByDefault: Boolean = true

    @get:Input
    var perModule: Boolean = false
}
