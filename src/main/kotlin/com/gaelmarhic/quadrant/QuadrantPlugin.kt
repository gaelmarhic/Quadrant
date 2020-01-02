package com.gaelmarhic.quadrant

import com.android.build.gradle.*
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import kotlin.reflect.KClass

class QuadrantPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        plugins.all { plugin ->
            when (plugin) {
                is AppPlugin -> {
                    applyPlugin(AppExtension::class) { it.applicationVariants }
                }
                is LibraryPlugin -> {
                    applyPlugin(LibraryExtension::class) { it.libraryVariants }
                }
                else -> throw UnsupportedOperationException(WRONG_MODULE_TYPE_ERROR_MESSAGE)
            }
        }
    }

    private fun <E : BaseExtension, V : BaseVariant> Project.applyPlugin(
        extensionType: KClass<E>,
        block: (E) -> DomainObjectCollection<V>
    ) {
        val extension = getExtension(extensionType)
        val variants = block(extension)
        val sourceSets = extension.sourceSets
        // TODO: To be implemented.
    }

    private fun <E : BaseExtension> Project.getExtension(type: KClass<E>) = extensions[type]

    private operator fun <T : Any> ExtensionContainer.get(type: KClass<T>): T {
        return getByType(type.java)
    }

    companion object {

        private const val WRONG_MODULE_TYPE_ERROR_MESSAGE =
            "Quadrant can only be applied to Android Application or Android Library modules."
    }
}
