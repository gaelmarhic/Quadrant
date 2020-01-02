package com.gaelmarhic.quadrant.constants

import java.io.File

object QuadrantConstants {

    const val PLUGIN_NAME = "Quadrant"
    val PLUGIN_CONFIG = PLUGIN_NAME.toLowerCase() + "Config"
    val TARGET_DIRECTORY = "generated${File.separator}source${File.separator}${PLUGIN_NAME.toLowerCase()}"
}
