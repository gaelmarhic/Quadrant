package com.gaelmarhic.quadrant.helpers

import com.gaelmarhic.quadrant.models.manifest.Activity
import com.gaelmarhic.quadrant.models.manifest.Application
import com.gaelmarhic.quadrant.models.manifest.MetaData
import com.gaelmarhic.quadrant.models.modules.ParsedManifest
import com.gaelmarhic.quadrant.models.modules.ParsedModule
import com.gaelmarhic.quadrant.models.modules.RawModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

internal class ManifestParsingHelperTest {

    private val manifestParsingHelper = ManifestParsingHelper()

    @Test
    fun `Manifest files are parsed correctly`() {

        // Given
        val module1Name = "Module 1"
        val module1ManifestPath = "src${S}test${S}resources${S}manifest1${S}AndroidManifest.xml"
        val module1 = RawModule(
            name = module1Name,
            manifestFiles = listOf(File(module1ManifestPath))
        )

        val module2Name = "Module 2"
        val module2ManifestPath = "src${S}test${S}resources${S}manifest2${S}AndroidManifest.xml"
        val module2 = RawModule(
            name = module2Name,
            manifestFiles = listOf(File(module2ManifestPath))
        )

        val rawModules = listOf(module1, module2)

        // When
        val parsedModules = manifestParsingHelper.parse(rawModules)

        // Then
        val parsedModule1 = ParsedModule(
            name = module1Name,
            manifestList = listOf(
                ParsedManifest(
                    path = File(module1ManifestPath).absolutePath,
                    application = Application(
                        activityList = mutableListOf(
                            Activity(
                                className = "com.gaelmarhic.quadrant.Manifest1Activity1",
                                metaDataList = mutableListOf(
                                    MetaData(
                                        name = "addressable",
                                        value = "true"
                                    )
                                )
                            ),
                            Activity(
                                className = "com.gaelmarhic.quadrant.Manifest1Activity2",
                                metaDataList = mutableListOf()
                            )
                        ),
                        metaDataList = mutableListOf(
                            MetaData(
                                name = "addressable",
                                value = "false"
                            )
                        )
                    )
                )
            )
        )

        val parsedModule2 = ParsedModule(
            name = module2Name,
            manifestList = listOf(
                ParsedManifest(
                    path = File(module2ManifestPath).absolutePath,
                    application = Application(
                        activityList = mutableListOf(
                            Activity(
                                className = "com.gaelmarhic.quadrant.Manifest2Activity1",
                                metaDataList = mutableListOf()
                            ),
                            Activity(
                                className = "com.gaelmarhic.quadrant.Manifest2Activity2",
                                metaDataList = mutableListOf()
                            )
                        ),
                        metaDataList = mutableListOf()
                    )
                )
            )
        )

        val expectedParsedModules = listOf(parsedModule1, parsedModule2)

        assertThat(parsedModules).isEqualTo(expectedParsedModules)
    }

    companion object {

        private val S = File.separator // Depending on the machine's file system.
    }
}
