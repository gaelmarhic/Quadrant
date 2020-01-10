package com.gaelmarhic.quadrant.helpers

import com.gaelmarhic.quadrant.extensions.QuadrantConfigurationExtension
import com.gaelmarhic.quadrant.models.generation.ConstantToBeGenerated
import com.gaelmarhic.quadrant.models.generation.FileToBeGenerated
import com.gaelmarhic.quadrant.models.modules.FilteredModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ConstantFileDeterminationHelperTest {

    private val configurationExtension = QuadrantConfigurationExtension()

    private val constantFileDeterminationHelper = ConstantFileDeterminationHelper(
        configurationExtension = configurationExtension
    )

    @Nested
    @DisplayName("When configuration is set to generate constants per module")
    inner class PerModule {

        @BeforeEach
        fun beforeEach() {
            configurationExtension.perModule = true
        }

        @Test
        fun `Should generate several files when there are several modules with addressable activities`() {

            // Given
            val filteredModule1ClassName1 = "com.gaelmarhic.quadrant.ModuleOneFirstActivity"
            val filteredModule1ClassName2 = "com.gaelmarhic.quadrant.ModuleOneSecondActivity"
            val filteredModule1 = FilteredModule(
                name = "module1",
                filteredClassNameList = listOf(
                    filteredModule1ClassName1,
                    filteredModule1ClassName2
                )
            )

            val filteredModule2ClassName1 = "com.gaelmarhic.quadrant.ModuleTwoFirstActivity"
            val filteredModule2ClassName2 = "com.gaelmarhic.quadrant.ModuleTwoSecondActivity"
            val filteredModule2 = FilteredModule(
                name = "module2",
                filteredClassNameList = listOf(
                    filteredModule2ClassName1,
                    filteredModule2ClassName2
                )
            )

            val filteredModules = listOf(filteredModule1, filteredModule2)

            // When
            val filesToBeGenerated = constantFileDeterminationHelper.determine(filteredModules)

            // Then
            val file1ToBeGenerated = FileToBeGenerated(
                name = "Module1",
                constantList = listOf(
                    ConstantToBeGenerated(
                        name = "MODULE_ONE_FIRST_ACTIVITY",
                        value = filteredModule1ClassName1
                    ),
                    ConstantToBeGenerated(
                        name = "MODULE_ONE_SECOND_ACTIVITY",
                        value = filteredModule1ClassName2
                    )
                )
            )

            val file2ToBeGenerated = FileToBeGenerated(
                name = "Module2",
                constantList = listOf(
                    ConstantToBeGenerated(
                        name = "MODULE_TWO_FIRST_ACTIVITY",
                        value = filteredModule2ClassName1
                    ),
                    ConstantToBeGenerated(
                        name = "MODULE_TWO_SECOND_ACTIVITY",
                        value = filteredModule2ClassName2
                    )
                )
            )

            val expectedFilesToBeGenerated = listOf(file1ToBeGenerated, file2ToBeGenerated)

            assertThat(filesToBeGenerated).isEqualTo(expectedFilesToBeGenerated)
        }

        @Test
        fun `Should generate only one file when there is only one module with addressable activities`() {

            // Given
            val filteredModuleClassName1 = "com.gaelmarhic.quadrant.FirstActivity"
            val filteredModuleClassName2 = "com.gaelmarhic.quadrant.SecondActivity"
            val filteredModule = FilteredModule(
                name = "module",
                filteredClassNameList = listOf(
                    filteredModuleClassName1,
                    filteredModuleClassName2
                )
            )

            val filteredModules = listOf(filteredModule)

            // When
            val filesToBeGenerated = constantFileDeterminationHelper.determine(filteredModules)

            // Then
            val file1ToBeGenerated = FileToBeGenerated(
                name = "Module",
                constantList = listOf(
                    ConstantToBeGenerated(
                        name = "FIRST_ACTIVITY",
                        value = filteredModuleClassName1
                    ),
                    ConstantToBeGenerated(
                        name = "SECOND_ACTIVITY",
                        value = filteredModuleClassName2
                    )
                )
            )

            val expectedFilesToBeGenerated = listOf(file1ToBeGenerated)

            assertThat(filesToBeGenerated).isEqualTo(expectedFilesToBeGenerated)
        }

        @Test
        fun `Should not generate any file when there is no module with addressable activities`() {

            // Given
            val filteredModules = emptyList<FilteredModule>()

            // When
            val filesToBeGenerated = constantFileDeterminationHelper.determine(filteredModules)

            // Then
            val expectedFilesToBeGenerated = emptyList<FileToBeGenerated>()

            assertThat(filesToBeGenerated).isEqualTo(expectedFilesToBeGenerated)
        }
    }

    @Nested
    @DisplayName("When configuration is NOT set to generate constants per module")
    inner class NotPerModule {

        @BeforeEach
        fun beforeEach() {
            configurationExtension.perModule = false
        }

        @Test
        fun `Should generate only one file when there are addressable activities, regardless of the modules`() {

            // Given
            val filteredModule1Name = "module1"
            val filteredModule1ClassName1 = "com.gaelmarhic.quadrant.ModuleOneFirstActivity"
            val filteredModule1ClassName2 = "com.gaelmarhic.quadrant.ModuleOneSecondActivity"
            val filteredModule1 = FilteredModule(
                name = filteredModule1Name,
                filteredClassNameList = listOf(
                    filteredModule1ClassName1,
                    filteredModule1ClassName2
                )
            )

            val filteredModule2Name = "module2"
            val filteredModule2ClassName1 = "com.gaelmarhic.quadrant.ModuleTwoFirstActivity"
            val filteredModule2ClassName2 = "com.gaelmarhic.quadrant.ModuleTwoSecondActivity"
            val filteredModule2 = FilteredModule(
                name = filteredModule2Name,
                filteredClassNameList = listOf(
                    filteredModule2ClassName1,
                    filteredModule2ClassName2
                )
            )

            val filteredModules = listOf(filteredModule1, filteredModule2)

            // When
            val filesToBeGenerated = constantFileDeterminationHelper.determine(filteredModules)

            // Then
            val file1ToBeGenerated = FileToBeGenerated(
                name = "QuadrantConstants",
                constantList = listOf(
                    ConstantToBeGenerated(
                        name = "MODULE_ONE_FIRST_ACTIVITY",
                        value = filteredModule1ClassName1
                    ),
                    ConstantToBeGenerated(
                        name = "MODULE_ONE_SECOND_ACTIVITY",
                        value = filteredModule1ClassName2
                    ),
                    ConstantToBeGenerated(
                        name = "MODULE_TWO_FIRST_ACTIVITY",
                        value = filteredModule2ClassName1
                    ),
                    ConstantToBeGenerated(
                        name = "MODULE_TWO_SECOND_ACTIVITY",
                        value = filteredModule2ClassName2
                    )
                )
            )

            val expectedFilesToBeGenerated = listOf(file1ToBeGenerated)

            assertThat(filesToBeGenerated).isEqualTo(expectedFilesToBeGenerated)
        }

        @Test
        fun `Should not generate any file when there is no module with addressable activities`() {

            // Given
            val filteredModules = emptyList<FilteredModule>()

            // When
            val filesToBeGenerated = constantFileDeterminationHelper.determine(filteredModules)

            // Then
            val expectedFilesToBeGenerated = emptyList<FileToBeGenerated>()

            assertThat(filesToBeGenerated).isEqualTo(expectedFilesToBeGenerated)
        }
    }
}
