package com.gaelmarhic.quadrant

import com.android.build.gradle.*
import com.android.build.gradle.api.AndroidSourceSet
import com.android.build.gradle.api.BaseVariant
import com.gaelmarhic.quadrant.constants.QuadrantConstants.PLUGIN_CONFIG
import com.gaelmarhic.quadrant.constants.QuadrantConstants.TARGET_DIRECTORY
import com.gaelmarhic.quadrant.extensions.QuadrantConfigurationExtension
import com.gaelmarhic.quadrant.tasks.GenerateActivityClassNameConstantTask
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionContainer
import java.io.File
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
            }
        }
    }

    private fun <E : BaseExtension, V : BaseVariant> Project.applyPlugin(
        extensionType: KClass<E>,
        block: (E) -> DomainObjectCollection<V>
    ) {
        val extension = getExtension(extensionType)
        val variants = block(extension)
        val mainSourceSet = extension.sourceSet(MAIN_SOURCE_SET)

        registerConfigurationExtension()
        registerTask(createTask(GenerateActivityClassNameConstantTask::class.java), variants)
        addTargetDirectoryToSourceSet(mainSourceSet)
    }

    private fun Project.registerConfigurationExtension() {
        extensions.create(PLUGIN_CONFIG, QuadrantConfigurationExtension::class.java)
    }

    private fun <V : BaseVariant> Project.registerTask(
        taskToBeRegistered: Task,
        variants: DomainObjectCollection<V>
    ) {
        afterEvaluate {
            variants.all { variant ->
                tasks.all { task ->
                    if (task.isCompileKotlinTask(variant)) {
                        task.dependsOn(taskToBeRegistered)
                    }
                }
            }
        }
    }

    private fun Project.addTargetDirectoryToSourceSet(sourceSet: AndroidSourceSet) {
        sourceSet.java.srcDir("$buildDir${File.separator}$TARGET_DIRECTORY")
    }

    private fun <E : BaseExtension> Project.getExtension(type: KClass<E>) = extensions[type]

    private operator fun <T : BaseExtension> ExtensionContainer.get(type: KClass<T>): T {
        return getByType(type.java)
    }

    private fun <T : Task> Project.createTask(taskType: Class<T>): Task {
        val taskName = taskType.simpleName.decapitalize()
        return tasks.create(taskName, taskType)
    }

    private fun BaseExtension.sourceSet(name: String) = sourceSets.getByName(name)

    private fun <T : BaseVariant> Task.isCompileKotlinTask(variant: T) =
        name == "compile${variant.name.capitalize()}Kotlin"

    companion object {

        private const val MAIN_SOURCE_SET = "main"
    }
}
