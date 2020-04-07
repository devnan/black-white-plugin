package com.devnan.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class BlackWhitePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("apply BlackWhitePlugin")
        val appExt = project.extensions.getByType<AppExtension>()
        val transform = BlackWhiteTransform()
        appExt.registerTransform(transform)
    }
}