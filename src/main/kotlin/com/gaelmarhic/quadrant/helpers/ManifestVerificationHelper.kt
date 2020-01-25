package com.gaelmarhic.quadrant.helpers

import com.gaelmarhic.quadrant.constants.GeneralConstants.PLUGIN_NAME
import com.gaelmarhic.quadrant.constants.Miscellaneous.FALSE
import com.gaelmarhic.quadrant.constants.Miscellaneous.TRUE
import com.gaelmarhic.quadrant.constants.ModelConstants.METADATA_NAME_ATTRIBUTE_ADDRESSABLE_VALUE
import com.gaelmarhic.quadrant.models.manifest.Activity
import com.gaelmarhic.quadrant.models.manifest.Application
import com.gaelmarhic.quadrant.models.manifest.MetaData
import com.gaelmarhic.quadrant.models.modules.ParsedModule

class ManifestVerificationHelper {

    fun verify(parsedModules: List<ParsedModule>) {
        verifyClassNameFormat(parsedModules)
        verifyAddressableMetaDatas(parsedModules)
    }

    private fun verifyClassNameFormat(modules: List<ParsedModule>) {
        mutableListOf<ClassNameFormatErrorHolder>()
            .apply {
                modules.forEach { (_, manifests) ->
                    manifests.forEach { (path, packageName, application) ->
                        application.findClassNameFormatErrors(packageName).let { classNameFormatErrors ->
                            if (classNameFormatErrors.isNotEmpty()) {
                                add(
                                    ClassNameFormatErrorHolder(
                                        manifestFilePath = path,
                                        declaredPackageName = packageName,
                                        classNames = classNameFormatErrors
                                    )
                                )
                            }
                        }
                    }
                }
            }.ifNotEmptyThrow { errorHolders ->
                val formattedMessage = formatClassNameFormatErrorMessage(errorHolders)
                IllegalStateException(formattedMessage)
            }
    }

    private fun Application.findClassNameFormatErrors(packageName: String) =
        activityList
            .filte { !startsWithPackage(it.className, packageName) }
            .map { it.className }
            .distinct()

    private fun verifyAddressableMetaDatas(modules: List<ParsedModule>) {
        mutableListOf<AddressableMetaDataConflictHolder>()
            .apply {
                modules.forEach { (moduleName, manifestList) ->
                    manifestList
                        .map { it.application }
                        .let { it.toAddressableMetaDataConflictHolder(moduleName) }
                        ?.let { add(it) }
                }
            }
            .ifNotEmptyThrow { conflictHolders ->
                val formattedMessage = formatAddressableMetaDataConflictMessage(conflictHolders)
                IllegalArgumentException(formattedMessage)
            }
    }

    private fun List<Application>.toAddressableMetaDataConflictHolder(moduleName: String) =
        mutableListOf<String>()
            .apply {
                val applicationConflicts = searchApplicationAddressabilityConflicts()
                val activityConflicts = this@toAddressableMetaDataConflictHolder
                    .flatMap { it.activityList }
                    .searchActivityAddressabilityConflicts()
                addAll(applicationConflicts)
                addAll(activityConflicts)
            }.takeIf { it.isNotEmpty() }
            ?.let {
                AddressableMetaDataConflictHolder(
                    moduleName = moduleName,
                    conflictingEntities = it
                )
            }

    private fun List<Application>.searchApplicationAddressabilityConflicts() =
        flatMap { it.metaDataList }
            .hasAddressabilityConflicts()
            .let { hasConflicts -> if (hasConflicts) "$APPLICATION_TAG." else null }
            .wrapIntoList()

    private fun List<Activity>.searchActivityAddressabilityConflicts() =
        groupBy { it.className }
            .map { groupedActivities ->
                groupedActivities.value
                    .flatMap { it.metaDataList }
                    .hasAddressabilityConflicts()
                    .let { hasConflicts -> if (hasConflicts) groupedActivities.key else null }
            }
            .filterNotNull()

    private fun List<MetaData>.hasAddressabilityConflicts() =
        asSequence()
            .filter { it.name == METADATA_NAME_ATTRIBUTE_ADDRESSABLE_VALUE }
            .distinctBy { it.value }
            .map { it.value }
            .map { it.toBoolean() }
            .filterNotNull()
            .toList()
            .containsAll(listOf(true, false))

    private fun formatClassNameFormatErrorMessage(errorHolders: List<ClassNameFormatErrorHolder>) =
        StringBuilder().apply {
            append(CLASS_NAME_FORMAT_ERROR_MESSAGE.trimIndent())
            errorHolders.forEach { errorHolder ->
                appendln()
                append("$FILE: ${errorHolder.manifestFilePath}")
                appendln()
                append("$DECLARED_PACKAGE: ${errorHolder.declaredPackageName}")
                errorHolder.classNames.forEachIndexed { index, className ->
                    appendln()
                    append("     ${index + 1})$className")
                }
            }
        }.toString()

    private fun formatAddressableMetaDataConflictMessage(conflictHolders: List<AddressableMetaDataConflictHolder>) =
        StringBuilder().apply {
            append(ADDRESSABLE_METADATA_CONFLICT_MESSAGE.trimIndent())
            conflictHolders.forEach { conflictHolder ->
                appendln()
                append("$MODULE: ${conflictHolder.moduleName}")
                conflictHolder.conflictingEntities.forEachIndexed { index, conflictingEntity ->
                    appendln()
                    append("     ${index + 1})$conflictingEntity")
                }
            }
        }.toString()

    private fun String.toBoolean() = when (this) {
        TRUE -> true
        FALSE -> false
        else -> null
    }

    private fun <T> List<T>.ifNotEmptyThrow(predicate: (List<T>) -> Exception) {
        if (isNotEmpty()) {
            throw predicate(this)
        }
    }

    private fun <T> T?.wrapIntoList() = if (this != null) listOf(this) else emptyList()

    private data class ClassNameFormatErrorHolder(
        val manifestFilePath: String,
        val declaredPackageName: String,
        val classNames: List<String>
    )

    private data class AddressableMetaDataConflictHolder(
        val moduleName: String,
        val conflictingEntities: List<String>
    )

    companion object {

        private const val MODULE = "Module"
        private const val FILE = "File"
        private const val DECLARED_PACKAGE = "Declared package"
        private const val APPLICATION_TAG = "Application tag"
        private const val CANNOT_PROCEED_ERROR_MESSAGE = "$PLUGIN_NAME cannot proceed."
        private const val CLASS_NAME_FORMAT_ERROR_MESSAGE = """
            $CANNOT_PROCEED_ERROR_MESSAGE
            For $PLUGIN_NAME to work, you must:
            1) Declare the ABSOLUTE class name of your activities in your manifest files (not the relative ones).
            2) Declare correctly the package attribute's value of the <manifest> tag in your manifest files. By "correctly", we mean that it must correspond to the actual package organisation of your module's folders. Any mismatch will cause $PLUGIN_NAME to fail.
                            
            Errors found in:
            """
        private const val ADDRESSABLE_METADATA_CONFLICT_MESSAGE = """
            $CANNOT_PROCEED_ERROR_MESSAGE
            You have "addressable" meta-data conflicts. 
            This means that you probably have different values (true/false) of "addressable" meta-datas for a same <activity> tag on your different manifest files of a same module. 
            Please, verify your different source sets.
                            
            Conflicts found in:
            """
    }
}
