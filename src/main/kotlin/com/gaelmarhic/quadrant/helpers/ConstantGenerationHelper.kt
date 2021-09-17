package com.gaelmarhic.quadrant.helpers

import com.gaelmarhic.quadrant.constants.GeneralConstants.PACKAGE
import com.gaelmarhic.quadrant.models.generation.FileToBeGenerated
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier.CONST
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File

class ConstantGenerationHelper(
    private val targetDirectory: File
) {

    fun generate(filesToBeGenerated: List<FileToBeGenerated>) {
        targetDirectory.deleteRecursively()
        filesToBeGenerated.forEach { it.generate() }
    }

    private fun FileToBeGenerated.generate() {
        val fileBuilder = FileSpec.builder(PACKAGE, name)
        val objectBuilder = TypeSpec.objectBuilder(name)

        constantList.forEach { constant ->
            val property = PropertySpec
                .builder(constant.name, String::class, CONST)
                .initializer(KOTLIN_POET_STRING_PLACEHOLDER, constant.value)
                .build()

            objectBuilder.addProperty(property)
        }

        fileBuilder
            .addType(objectBuilder.build())
            .build()
            .writeTo(targetDirectory)
    }

    companion object {

        private const val KOTLIN_POET_STRING_PLACEHOLDER = "%S"
    }
}
