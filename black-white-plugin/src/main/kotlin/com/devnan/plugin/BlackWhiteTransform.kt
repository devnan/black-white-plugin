package com.devnan.plugin

import TimeReporter
import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream

class BlackWhiteTransform : Transform() {

    private var timeReporter: TimeReporter? = null

    override fun getName(): String {
        return "BlackWhiteTransform"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        timeReporter = TimeReporter()
        val transformInputs = transformInvocation?.inputs
        timeReporter?.start("black-white plugin transform")
        transformInputs?.forEach {
            transformClasses(it, transformInvocation)
            transformJar(it, transformInvocation)
        }
        timeReporter?.stop()
    }

    private fun transformClasses(
        it: TransformInput,
        transformInvocation: TransformInvocation
    ) {
        it.directoryInputs.forEach { directoryInput ->
            val dirPath: String = directoryInput.file.absolutePath
            directoryInput.file.walkTopDown().forEach { file ->
                if (file.isFile && file.name.endsWith(".class")) {
                    val classReader = ClassReader(file.readBytes())
                    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    val classVisitor = BlackWhiteClassVisitor(classWriter)
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                    val data = classWriter.toByteArray()
                    val fout = FileOutputStream(file)
                    fout.write(data)
                    fout.close()

                    val dest = File(
                        directoryInput.getOutputFile(transformInvocation, Format.DIRECTORY),
                        file.absolutePath.substring(dirPath.length)
                    )
                    dest.parentFile.mkdirs()
                    file.copyTo(dest)
                }
            }
        }
//        timeReporter?.splitTime("transform classes: " + it.directoryInputs.toString())
    }

    private fun transformJar(
        transformInput: TransformInput,
        transformInvocation: TransformInvocation
    ) {
        //TODO transform activity in jar
        transformInput.jarInputs.forEach { jarInput ->
            val dirPath: String = jarInput.file.absolutePath
            jarInput.file.walkTopDown().forEach { file ->
                if (file.isFile && file.name.endsWith(".jar")) {
                    val dest = File(
                        jarInput.getOutputFile(transformInvocation, Format.JAR),
                        file.absolutePath.substring(dirPath.length)
                    )
                    dest.parentFile.mkdirs()
                    file.copyTo(dest)
                }
            }
        }
//        timeReporter?.splitTime("transform jar: " + transformInput.jarInputs.toString())
    }

    private fun QualifiedContent.getOutputFile(
        transformInvocation: TransformInvocation,
        format: Format
    ): File {
        return transformInvocation.outputProvider.getContentLocation(
            this.name,
            this.contentTypes,
            this.scopes,
            format
        )
    }
}