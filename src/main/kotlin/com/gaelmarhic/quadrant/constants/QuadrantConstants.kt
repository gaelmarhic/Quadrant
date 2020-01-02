package com.gaelmarhic.quadrant.constants

import java.io.File

object QuadrantConstants {

    const val PLUGIN_NAME = "Quadrant"
    val TARGET_DIRECTORY = "generated${File.separator}source${File.separator}${PLUGIN_NAME.toLowerCase()}"
}
