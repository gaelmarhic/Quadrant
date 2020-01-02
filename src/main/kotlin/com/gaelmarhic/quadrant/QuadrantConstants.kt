package com.gaelmarhic.quadrant

import java.io.File

object QuadrantConstants {

    const val PLUGIN_NAME = "Quadrant"
    val TARGET_DIRECTORY = "generated${File.separator}source${File.separator}${PLUGIN_NAME.toLowerCase()}"
}
