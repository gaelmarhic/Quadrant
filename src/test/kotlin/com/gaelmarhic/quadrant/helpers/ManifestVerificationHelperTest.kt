package com.gaelmarhic.quadrant.helpers

import com.gaelmarhic.quadrant.models.manifest.Activity
import com.gaelmarhic.quadrant.models.manifest.Application
import com.gaelmarhic.quadrant.models.manifest.MetaData
import com.gaelmarhic.quadrant.models.modules.ParsedManifest
import com.gaelmarhic.quadrant.models.modules.ParsedModule
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ManifestVerificationHelperTest {

    private val manifestVerificationHelper = ManifestVerificationHelper()

    @Nested
    inner class ClassNameFormat {

        @Test
        fun `Should throw an exception when one of the manifests' classnames is relative`() {

            // Then
            assertThrows<IllegalStateException> {

                // Given
                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifest1Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = ".Activity1",
                                        metaDataList = mutableListOf()
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity2",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf()
                            )
                        ),
                        ParsedManifest(
                            path = "manifest2Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity1",
                                        metaDataList = mutableListOf()
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity2",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf()
                            )
                        )
                    )
                )

                val parsedModules = listOf(parsedModule)

                // When
                manifestVerificationHelper.verify(parsedModules)
            }
        }

        @Test
        fun `Should throw an exception when several classnames in a same manifest are relative`() {

            // Then
            assertThrows<IllegalStateException> {

                // Given
                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifest1Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = ".Activity1",
                                        metaDataList = mutableListOf()
                                    ),
                                    Activity(
                                        className = ".Activity2",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf()
                            )
                        ),
                        ParsedManifest(
                            path = "manifest2Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity1",
                                        metaDataList = mutableListOf()
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity2",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf()
                            )
                        )
                    )
                )

                val parsedModules = listOf(parsedModule)

                // When
                manifestVerificationHelper.verify(parsedModules)
            }
        }

        @Test
        fun `Should throw an exception when classnames in different manifests are relative`() {

            // Then
            assertThrows<IllegalStateException> {

                // Given
                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifest1Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = ".Activity1",
                                        metaDataList = mutableListOf()
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity2",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf()
                            )
                        ),
                        ParsedManifest(
                            path = "manifest2Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity1",
                                        metaDataList = mutableListOf()
                                    ),
                                    Activity(
                                        className = ".Activity2",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf()
                            )
                        )
                    )
                )

                val parsedModules = listOf(parsedModule)

                // When
                manifestVerificationHelper.verify(parsedModules)
            }
        }

        @Test
        fun `Should NOT throw any exception when all classnames are fine in all manifests from all modules`() {

            // Given
            val parsedModule1 = ParsedModule(
                name = "module1",
                manifestList = listOf(
                    ParsedManifest(
                        path = "module1Manifest1Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Module1Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Module1Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    ),
                    ParsedManifest(
                        path = "module1Manifest2Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Module1Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Module1Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModule2 = ParsedModule(
                name = "module2",
                manifestList = listOf(
                    ParsedManifest(
                        path = "module2Manifest1Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Module2Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Module2Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    ),
                    ParsedManifest(
                        path = "module2Manifest2Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Module2Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Module2Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModules = listOf(parsedModule1, parsedModule2)

            // When
            manifestVerificationHelper.verify(parsedModules)

            // Then
            // No exception is thrown.
        }

        @Test
        fun `Should NOT throw any exception when one of the manifests' classnames is relative but it is ignored`() {

            // Given
            val parsedModule = ParsedModule(
                name = "module",
                manifestList = listOf(
                    ParsedManifest(
                        path = "manifest1Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = ".Activity1",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "quadrant",
                                            value = "ignore"
                                        )
                                    )
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    ),
                    ParsedManifest(
                        path = "manifest2Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModules = listOf(parsedModule)

            // When
            manifestVerificationHelper.verify(parsedModules)

            // Then
            // No exception is thrown.
        }

        @Test
        fun `Should NOT throw any exception when several classnames in a same manifest are relative but are ignored`() {

            // Given
            val parsedModule = ParsedModule(
                name = "module",
                manifestList = listOf(
                    ParsedManifest(
                        path = "manifest1Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = ".Activity1",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "quadrant",
                                            value = "ignore"
                                        )
                                    )
                                ),
                                Activity(
                                    className = ".Activity2",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "quadrant",
                                            value = "ignore"
                                        )
                                    )
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    ),
                    ParsedManifest(
                        path = "manifest2Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModules = listOf(parsedModule)

            // When
            manifestVerificationHelper.verify(parsedModules)

            // Then
            // No exception is thrown.
        }

        @Test
        fun `Should NOT throw any exception when classnames in different manifests are relative but are ignored`() {

            // Given
            val parsedModule = ParsedModule(
                name = "module",
                manifestList = listOf(
                    ParsedManifest(
                        path = "manifest1Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = ".Activity1",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "quadrant",
                                            value = "ignore"
                                        )
                                    )
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    ),
                    ParsedManifest(
                        path = "manifest2Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = ".Activity2",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "quadrant",
                                            value = "ignore"
                                        )
                                    )
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModules = listOf(parsedModule)

            // When
            manifestVerificationHelper.verify(parsedModules)

            // Then
            // No exception is thrown.
        }
    }

    @Nested
    inner class ClassNameDuplication {

        @Test
        fun `Should throw an exception when there is one duplicated class name`() {

            // Then
            assertThrows<IllegalStateException> {

                val parsedModule1 = ParsedModule(
                    name = "module1",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "module1ManifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.module.Activity1",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf()
                            )
                        )
                    )
                )

                val parsedModule2 = ParsedModule(
                    name = "module2",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "module2ManifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.module.Activity1",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf()
                            )
                        )
                    )
                )

                val parsedModules = listOf(parsedModule1, parsedModule2)

                // When
                manifestVerificationHelper.verify(parsedModules)
            }
        }

        @Test
        fun `Should throw an exception when there are more than one duplicated class names`() {

            // Then
            assertThrows<IllegalStateException> {

                val parsedModule1 = ParsedModule(
                    name = "module1",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "module1ManifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.module.Activity1",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf()
                            )
                        )
                    )
                )

                val parsedModule2 = ParsedModule(
                    name = "module2",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "module2ManifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.module.Activity1",
                                        metaDataList = mutableListOf()
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.module.Activity2",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf()
                            )
                        )
                    )
                )

                val parsedModule3 = ParsedModule(
                    name = "module3",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "module3ManifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.module.Activity2",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf()
                            )
                        )
                    )
                )

                val parsedModules = listOf(parsedModule1, parsedModule2, parsedModule3)

                // When
                manifestVerificationHelper.verify(parsedModules)
            }
        }

        @Test
        fun `Should NOT throw any exception when there are no duplicated class names`() {

            val parsedModule1 = ParsedModule(
                name = "module1",
                manifestList = listOf(
                    ParsedManifest(
                        path = "module1ManifestPath",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.module1.Activity1",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModule2 = ParsedModule(
                name = "module2",
                manifestList = listOf(
                    ParsedManifest(
                        path = "module2ManifestPath",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.module2.Activity1",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModules = listOf(parsedModule1, parsedModule2)

            // When
            manifestVerificationHelper.verify(parsedModules)

            // Then
            // No exception is thrown.
        }

        @Test
        fun `Should NOT throw any exception when there is one duplicated class name but one version is ignored`() {

            val parsedModule1 = ParsedModule(
                name = "module1",
                manifestList = listOf(
                    ParsedManifest(
                        path = "module1ManifestPath",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.module.Activity1",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "quadrant",
                                            value = "ignore"
                                        )
                                    )
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModule2 = ParsedModule(
                name = "module2",
                manifestList = listOf(
                    ParsedManifest(
                        path = "module2ManifestPath",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.module.Activity1",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModules = listOf(parsedModule1, parsedModule2)

            // When
            manifestVerificationHelper.verify(parsedModules)

            // Then
            // No exception is thrown.
        }

        @Test
        fun `Should NOT throw any exception when there are more than one duplicated class names but one version is ignored`() {

            val parsedModule1 = ParsedModule(
                name = "module1",
                manifestList = listOf(
                    ParsedManifest(
                        path = "module1ManifestPath",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.module.Activity1",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "quadrant",
                                            value = "ignore"
                                        )
                                    )
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModule2 = ParsedModule(
                name = "module2",
                manifestList = listOf(
                    ParsedManifest(
                        path = "module2ManifestPath",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.module.Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.module.Activity2",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "quadrant",
                                            value = "ignore"
                                        )
                                    )
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModule3 = ParsedModule(
                name = "module3",
                manifestList = listOf(
                    ParsedManifest(
                        path = "module3ManifestPath",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.module.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModules = listOf(parsedModule1, parsedModule2, parsedModule3)

            // When
            manifestVerificationHelper.verify(parsedModules)

            // Then
            // No exception is thrown.
        }
    }

    @Nested
    inner class AddressableMetaDatas {

        @Test
        fun `Should throw an exception when there is an addressability conflict in application in one module`() {

            // Then
            assertThrows<IllegalArgumentException> {

                // Given
                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifest1Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity1",
                                        metaDataList = mutableListOf()
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity2",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf(
                                    MetaData(
                                        name = "addressable",
                                        value = "true"
                                    )
                                )
                            )
                        ),
                        ParsedManifest(
                            path = "manifest2Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity1",
                                        metaDataList = mutableListOf()
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity2",
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

                val parsedModules = listOf(parsedModule)

                // When
                manifestVerificationHelper.verify(parsedModules)
            }
        }

        @Test
        fun `Should throw an exception when there is an addressability conflict in an activity in one module`() {

            // Then
            assertThrows<IllegalArgumentException> {

                // Given
                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifest1Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity1",
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "true"
                                            )
                                        )
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity2",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf()
                            )
                        ),
                        ParsedManifest(
                            path = "manifest2Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity1",
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "false"
                                            )
                                        )
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity2",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf()
                            )
                        )
                    )
                )

                val parsedModules = listOf(parsedModule)

                // When
                manifestVerificationHelper.verify(parsedModules)
            }
        }

        @Test
        fun `Should throw an exception when there are addressability conflicts in both application and an activity in one module`() {

            // Then
            assertThrows<IllegalArgumentException> {

                // Given
                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifest1Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity1",
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "true"
                                            )
                                        )
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity2",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf(
                                    MetaData(
                                        name = "addressable",
                                        value = "true"
                                    )
                                )
                            )
                        ),
                        ParsedManifest(
                            path = "manifest2Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity1",
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "false"
                                            )
                                        )
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity2",
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

                val parsedModules = listOf(parsedModule)

                // When
                manifestVerificationHelper.verify(parsedModules)
            }
        }

        @Test
        fun `Should throw an exception when there are addressability conflicts in different modules`() {

            // Then
            assertThrows<IllegalArgumentException> {

                // Given
                val parsedModule1 = ParsedModule(
                    name = "module1",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "module1Manifest1Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Module1Activity1",
                                        metaDataList = mutableListOf()
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Module1Activity2",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf(
                                    MetaData(
                                        name = "addressable",
                                        value = "trie"
                                    )
                                )
                            )
                        ),
                        ParsedManifest(
                            path = "module1Manifest2Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Module1Activity1",
                                        metaDataList = mutableListOf()
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Module1Activity2",
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
                    name = "module2",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "module2Manifest1Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Module2Activity1",
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "true"
                                            )
                                        )
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Module2Activity2",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf()
                            )
                        ),
                        ParsedManifest(
                            path = "module2Manifest2Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Module2Activity1",
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "false"
                                            )
                                        )
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Module2Activity2",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf()
                            )
                        )
                    )
                )

                val parsedModules = listOf(parsedModule1, parsedModule2)

                // When
                manifestVerificationHelper.verify(parsedModules)
            }
        }

        @Test
        fun `Should throw an exception when there are addressability conflicts in both application and an activity in one module but one version of the activity is ignored`() {

            // Then
            assertThrows<IllegalArgumentException> {

                // Given
                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifest1Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity1",
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "true"
                                            ),
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity2",
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf(
                                    MetaData(
                                        name = "addressable",
                                        value = "true"
                                    )
                                )
                            )
                        ),
                        ParsedManifest(
                            path = "manifest2Path",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity1",
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "false"
                                            )
                                        )
                                    ),
                                    Activity(
                                        className = "com.gaelmarhic.quadrant.Activity2",
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

                val parsedModules = listOf(parsedModule)

                // When
                manifestVerificationHelper.verify(parsedModules)
            }
        }

        @Test
        fun `Should not evaluate addressability when addressable is not written properly`() {

            // Given
            val parsedModule = ParsedModule(
                name = "module",
                manifestList = listOf(
                    ParsedManifest(
                        path = "manifest1Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf(
                                MetaData(
                                    name = "addressa",
                                    value = "true"
                                )
                            )
                        )
                    ),
                    ParsedManifest(
                        path = "manifest2Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
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

            val parsedModules = listOf(parsedModule)

            // When
            manifestVerificationHelper.verify(parsedModules)

            // Then
            // No exception is thrown.
        }

        @Test
        fun `Should not evaluate addressability when boolean value is not written properly`() {

            // Given
            val parsedModule = ParsedModule(
                name = "module",
                manifestList = listOf(
                    ParsedManifest(
                        path = "manifest1Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf(
                                MetaData(
                                    name = "addressable",
                                    value = "tr"
                                )
                            )
                        )
                    ),
                    ParsedManifest(
                        path = "manifest2Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
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

            val parsedModules = listOf(parsedModule)

            // When
            manifestVerificationHelper.verify(parsedModules)

            // Then
            // No exception is thrown.
        }

        @Test
        fun `Should not take into account other metadatas`() {

            // Given
            val parsedModule = ParsedModule(
                name = "module",
                manifestList = listOf(
                    ParsedManifest(
                        path = "manifest1Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf(
                                MetaData(
                                    name = "other metadata",
                                    value = "true"
                                )
                            )
                        )
                    ),
                    ParsedManifest(
                        path = "manifest2Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
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

            val parsedModules = listOf(parsedModule)

            // When
            manifestVerificationHelper.verify(parsedModules)

            // Then
            // No exception is thrown.
        }

        @Test
        fun `Should NOT throw any exception when there are no addressable metadatas at all`() {

            // Given
            val parsedModule = ParsedModule(
                name = "module",
                manifestList = listOf(
                    ParsedManifest(
                        path = "manifest1Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    ),
                    ParsedManifest(
                        path = "manifest2Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModules = listOf(parsedModule)

            // When
            manifestVerificationHelper.verify(parsedModules)

            // Then
            // No exception is thrown.
        }

        @Test
        fun `Should NOT throw any exception when there are metadatas in application and they are not conflicting`() {

            // Given
            val parsedModule = ParsedModule(
                name = "module",
                manifestList = listOf(
                    ParsedManifest(
                        path = "manifest1Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf(
                                MetaData(
                                    name = "addressable",
                                    value = "true"
                                )
                            )
                        )
                    ),
                    ParsedManifest(
                        path = "manifest2Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf()
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf(
                                MetaData(
                                    name = "addressable",
                                    value = "true"
                                )
                            )
                        )
                    )
                )
            )

            val parsedModules = listOf(parsedModule)

            // When
            manifestVerificationHelper.verify(parsedModules)

            // Then
            // No exception is thrown.
        }

        @Test
        fun `Should NOT throw any exception when there are metadatas in activities and they are not conflicting`() {

            // Given
            val parsedModule = ParsedModule(
                name = "module",
                manifestList = listOf(
                    ParsedManifest(
                        path = "manifest1Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "addressable",
                                            value = "false"
                                        )
                                    )
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    ),
                    ParsedManifest(
                        path = "manifest2Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "addressable",
                                            value = "false"
                                        )
                                    )
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModules = listOf(parsedModule)

            // When
            manifestVerificationHelper.verify(parsedModules)

            // Then
            // No exception is thrown.
        }

        @Test
        fun `Should NOT throw any exception when there are metadatas in both application and activities and they are not conflicting`() {

            // Given
            val parsedModule = ParsedModule(
                name = "module",
                manifestList = listOf(
                    ParsedManifest(
                        path = "manifest1Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "addressable",
                                            value = "false"
                                        )
                                    )
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf(
                                MetaData(
                                    name = "addressable",
                                    value = "true"
                                )
                            )
                        )
                    ),
                    ParsedManifest(
                        path = "manifest2Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "addressable",
                                            value = "false"
                                        )
                                    )
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf(
                                MetaData(
                                    name = "addressable",
                                    value = "true"
                                )
                            )
                        )
                    )
                )
            )

            val parsedModules = listOf(parsedModule)

            // When
            manifestVerificationHelper.verify(parsedModules)

            // Then
            // No exception is thrown.
        }

        @Test
        fun `Should NOT throw any exception when there is an addressability conflict in an activity in one module but one version is ignored`() {

            // Given
            val parsedModule = ParsedModule(
                name = "module",
                manifestList = listOf(
                    ParsedManifest(
                        path = "manifest1Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "addressable",
                                            value = "true"
                                        ),
                                        MetaData(
                                            name = "quadrant",
                                            value = "ignore"
                                        )
                                    )
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    ),
                    ParsedManifest(
                        path = "manifest2Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity1",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "addressable",
                                            value = "false"
                                        )
                                    )
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModules = listOf(parsedModule)

            // When
            manifestVerificationHelper.verify(parsedModules)

            // Then
            // No exception is thrown.
        }

        @Test
        fun `Should NOT throw any exception when there are addressability conflicts in activities in different modules but one version is ignored`() {

            // Given
            val parsedModule1 = ParsedModule(
                name = "module1",
                manifestList = listOf(
                    ParsedManifest(
                        path = "module1Manifest1Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Module1Activity1",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "addressable",
                                            value = "true"
                                        ),
                                        MetaData(
                                            name = "quadrant",
                                            value = "ignore"
                                        )
                                    )
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Module1Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    ),
                    ParsedManifest(
                        path = "module1Manifest2Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Module1Activity1",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "addressable",
                                            value = "false"
                                        )
                                    )
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Module1Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModule2 = ParsedModule(
                name = "module2",
                manifestList = listOf(
                    ParsedManifest(
                        path = "module2Manifest1Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Module2Activity1",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "addressable",
                                            value = "true"
                                        )
                                    )
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Module2Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    ),
                    ParsedManifest(
                        path = "module2Manifest2Path",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Module2Activity1",
                                    metaDataList = mutableListOf(
                                        MetaData(
                                            name = "addressable",
                                            value = "false"
                                        ),
                                        MetaData(
                                            name = "quadrant",
                                            value = "ignore"
                                        )
                                    )
                                ),
                                Activity(
                                    className = "com.gaelmarhic.quadrant.Module2Activity2",
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    )
                )
            )

            val parsedModules = listOf(parsedModule1, parsedModule2)

            // When
            manifestVerificationHelper.verify(parsedModules)

            // Then
            // No exception is thrown.
        }
    }
}
