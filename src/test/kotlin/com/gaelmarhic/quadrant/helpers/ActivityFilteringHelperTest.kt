package com.gaelmarhic.quadrant.helpers

import com.gaelmarhic.quadrant.extensions.QuadrantConfigurationExtension
import com.gaelmarhic.quadrant.models.manifest.Activity
import com.gaelmarhic.quadrant.models.manifest.Application
import com.gaelmarhic.quadrant.models.manifest.MetaData
import com.gaelmarhic.quadrant.models.modules.FilteredModule
import com.gaelmarhic.quadrant.models.modules.ParsedManifest
import com.gaelmarhic.quadrant.models.modules.ParsedModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ActivityFilteringHelperTest {

    private val configurationExtension = QuadrantConfigurationExtension()

    private val activityFilteringHelper = ActivityFilteringHelper(
        configurationExtension = configurationExtension
    )

    @Test
    fun `Should not return a module when it does not have any addressable activity`() {

        // Given
        configurationExtension.generateByDefault = false

        val parsedModule1 = ParsedModule(
            name = "module1",
            manifestList = listOf(
                ParsedManifest(
                    path = "module1ManifestPath",
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
                    path = "module2ManifestPath",
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
                )
            )
        )

        val parsedModules = listOf(parsedModule1, parsedModule2)

        // When
        val filteredModules = activityFilteringHelper.filter(parsedModules)

        // Then
        assertThat(filteredModules.size).isEqualTo(1)
        assertThat(filteredModules.firstOrNull { it.name == "module1" }).isNull()
    }

    @Test
    fun `Should not return anything when there is no addressable activity at all in the entire app`() {

        // Given
        configurationExtension.generateByDefault = false

        val parsedModule1 = ParsedModule(
            name = "module1",
            manifestList = listOf(
                ParsedManifest(
                    path = "module1ManifestPath",
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
                    path = "module2ManifestPath",
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
        val filteredModules = activityFilteringHelper.filter(parsedModules)

        // Then
        assertThat(filteredModules.size).isEqualTo(0)
    }

    @Nested
    @DisplayName("When the configuration is set to generate constants by default")
    inner class GenerateByDefault {

        @BeforeEach
        fun beforeEach() {
            configurationExtension.generateByDefault = true
        }

        @Nested
        @DisplayName("and the application is addressable")
        inner class ApplicationAddressable {

            @Test
            fun `Then the activity should have a constant when it has an addressable metadata set to true`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "true"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertContainsClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when it has an addressable metadata set to false`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "false"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should have a constant when it does NOT have any addressability information`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertContainsClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when its unique version is ignored`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when all of its versions are ignored`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
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
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should have a constant when NOT all of its versions are ignored`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
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
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertContainsClassName(filteredModules, activityClassName)
            }
        }

        @Nested
        @DisplayName("and the application is NOT addressable")
        inner class ApplicationNotAddressable {

            @Test
            fun `Then the activity should have a constant when it has an addressable metadata set to true`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "true"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertContainsClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when it has an addressable metadata set to false`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "false"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when it does NOT have any addressability information`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when its unique version is ignored`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when all of its versions are ignored`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
                                    )
                                ),
                                metaDataList = mutableListOf(
                                    MetaData(
                                        name = "addressable",
                                        value = "false"
                                    )
                                )
                            )
                        ),
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when NOT all of its versions are ignored`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
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
                        ),
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }
        }

        @Nested
        @DisplayName("and the application has no explicit addressability information")
        inner class ApplicationHasNoAddressabilityInformation {

            @Test
            fun `Then the activity should have a constant when it has an addressable metadata set to true`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "true"
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertContainsClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when it has an addressable metadata set to false`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "false"
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should have a constant when it does NOT have any addressability information`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertContainsClassName(filteredModules, activityClassName)
            }
        }

        @Test
        fun `Then the activity should NOT have a constant when its unique version is ignored`() {

            // Given
            val activityClassName = "com.gaelmarhic.quadrant.Activity1"

            val parsedModule = ParsedModule(
                name = "module",
                manifestList = listOf(
                    ParsedManifest(
                        path = "manifestPath",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = activityClassName,
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
            val filteredModules = activityFilteringHelper.filter(parsedModules)

            // Then
            assertDoesNotContainClassName(filteredModules, activityClassName)
        }

        @Test
        fun `Then the activity should NOT have a constant when all of its versions are ignored`() {

            // Given
            val activityClassName = "com.gaelmarhic.quadrant.Activity1"

            val parsedModule = ParsedModule(
                name = "module",
                manifestList = listOf(
                    ParsedManifest(
                        path = "manifestPath",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = activityClassName,
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
                        path = "manifestPath",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = activityClassName,
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
            val filteredModules = activityFilteringHelper.filter(parsedModules)

            // Then
            assertDoesNotContainClassName(filteredModules, activityClassName)
        }

        @Test
        fun `Then the activity should have a constant when NOT all of its versions are ignored`() {

            // Given
            val activityClassName = "com.gaelmarhic.quadrant.Activity1"

            val parsedModule = ParsedModule(
                name = "module",
                manifestList = listOf(
                    ParsedManifest(
                        path = "manifestPath",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = activityClassName,
                                    metaDataList = mutableListOf()
                                )
                            ),
                            metaDataList = mutableListOf()
                        )
                    ),
                    ParsedManifest(
                        path = "manifestPath",
                        application = Application(
                            activityList = mutableListOf(
                                Activity(
                                    className = activityClassName,
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
            val filteredModules = activityFilteringHelper.filter(parsedModules)

            // Then
            assertContainsClassName(filteredModules, activityClassName)
        }
    }

    @Nested
    @DisplayName("When the configuration is NOT set to generate constants by default")
    inner class DoNotGenerateByDefault {

        @BeforeEach
        fun beforeEach() {
            configurationExtension.generateByDefault = false
        }

        @Nested
        @DisplayName("and the application is addressable")
        inner class ApplicationAddressable {

            @Test
            fun `Then the activity should have a constant when it has an addressable metadata set to true`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "true"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertContainsClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when it has an addressable metadata set to false`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "false"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should have a constant when it does NOT have any addressability information`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertContainsClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when its unique version is ignored`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when all of its versions are ignored`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
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
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should have a constant when NOT all of its versions are ignored`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
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
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertContainsClassName(filteredModules, activityClassName)
            }
        }

        @Nested
        @DisplayName("and the application is NOT addressable")
        inner class ApplicationNotAddressable {

            @Test
            fun `Then the activity should have a constant when it has an addressable metadata set to true`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "true"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertContainsClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when it has an addressable metadata set to false`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "false"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when it does NOT have any addressability information`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when its unique version is ignored`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when all of its versions are ignored`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
                                    )
                                ),
                                metaDataList = mutableListOf(
                                    MetaData(
                                        name = "addressable",
                                        value = "false"
                                    )
                                )
                            )
                        ),
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when NOT all of its versions are ignored`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
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
                        ),
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "quadrant",
                                                value = "ignore"
                                            )
                                        )
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }
        }

        @Nested
        @DisplayName("and the application has no explicit addressability information")
        inner class ApplicationHasNoAddressabilityInformation {

            @Test
            fun `Then the activity should have a constant when it has an addressable metadata set to true`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "true"
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertContainsClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when it has an addressable metadata set to false`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf(
                                            MetaData(
                                                name = "addressable",
                                                value = "false"
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when it does NOT have any addressability information`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when its unique version is ignored`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when all of its versions are ignored`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
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
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }

            @Test
            fun `Then the activity should NOT have a constant when NOT all of its versions are ignored`() {

                // Given
                val activityClassName = "com.gaelmarhic.quadrant.Activity1"

                val parsedModule = ParsedModule(
                    name = "module",
                    manifestList = listOf(
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
                                        metaDataList = mutableListOf()
                                    )
                                ),
                                metaDataList = mutableListOf()
                            )
                        ),
                        ParsedManifest(
                            path = "manifestPath",
                            application = Application(
                                activityList = mutableListOf(
                                    Activity(
                                        className = activityClassName,
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
                val filteredModules = activityFilteringHelper.filter(parsedModules)

                // Then
                assertDoesNotContainClassName(filteredModules, activityClassName)
            }
        }
    }

    private fun assertContainsClassName(modules: List<FilteredModule>, className: String) =
        assertClassName(modules, className, true)

    private fun assertDoesNotContainClassName(modules: List<FilteredModule>, className: String) =
        assertClassName(modules, className, false)

    private fun assertClassName(modules: List<FilteredModule>, className: String, contains: Boolean) {
        val containsClassName = modules
            .firstOrNull()
            ?.filteredClassNameList
            ?.contains(className)
            ?: false

        assertThat(containsClassName).isEqualTo(contains)
    }
}
