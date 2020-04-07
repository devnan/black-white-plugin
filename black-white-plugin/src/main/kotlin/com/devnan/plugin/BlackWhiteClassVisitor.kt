package com.devnan.plugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*


class BlackWhiteClassVisitor(cv: ClassVisitor?) : ClassVisitor(Opcodes.ASM8, cv), Opcodes {

    private var className: String? = null
    private var superClassName: String? = null

    override fun visit(
        version: Int, access: Int, name: String, signature: String?,
        superName: String, interfaces: Array<String>?
    ) {
        cv.visit(version, access, name, signature, superName, interfaces)
        this.className = name
        this.superClassName = superName
    }

    override fun visitMethod(
        access: Int,
        name: String,
        desc: String,
        signature: String?,
        exceptions: Array<String>?
    ): MethodVisitor {
        val mv = cv.visitMethod(
            access, name, desc, signature, exceptions
        )
        if (superClassName.equals("androidx/appcompat/app/AppCompatActivity") ||
            superClassName.equals("android/support/v7/app/AppCompatActivity") ||
            superClassName.equals(" android/app/Activity")
        ) {
            if (name.startsWith("onCreate")) {
                return MyMethodVisitor(mv)
            }
        }
        return mv
    }

    internal inner class MyMethodVisitor(mv: MethodVisitor?) :
        MethodVisitor(Opcodes.ASM5, mv), Opcodes {
        override fun visitCode() {
            super.visitCode()
            mv.visitTypeInsn(NEW, "android/graphics/Paint")
            mv.visitInsn(DUP)
            mv.visitMethodInsn(
                INVOKESPECIAL,
                "android/graphics/Paint",
                "<init>",
                "()V",
                false
            )
            mv.visitVarInsn(ASTORE, 2)
            mv.visitTypeInsn(NEW, "android/graphics/ColorMatrix")
            mv.visitInsn(DUP)
            mv.visitMethodInsn(
                INVOKESPECIAL,
                "android/graphics/ColorMatrix",
                "<init>",
                "()V",
                false
            )
            mv.visitVarInsn(ASTORE, 3)
            mv.visitVarInsn(ALOAD, 3)
            mv.visitInsn(FCONST_0)
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "android/graphics/ColorMatrix",
                "setSaturation",
                "(F)V",
                false
            )
            mv.visitVarInsn(ALOAD, 2)
            mv.visitTypeInsn(
                NEW,
                "android/graphics/ColorMatrixColorFilter"
            )
            mv.visitInsn(DUP)
            mv.visitVarInsn(ALOAD, 3)
            mv.visitMethodInsn(
                INVOKESPECIAL,
                "android/graphics/ColorMatrixColorFilter",
                "<init>",
                "(Landroid/graphics/ColorMatrix;)V",
                false
            )
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "android/graphics/Paint",
                "setColorFilter",
                "(Landroid/graphics/ColorFilter;)Landroid/graphics/ColorFilter;",
                false
            )
            mv.visitInsn(POP)
            mv.visitVarInsn(ALOAD, 0)
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "android/app/Activity",
                "getWindow",
                "()Landroid/view/Window;",
                false
            )
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "android/view/Window",
                "getDecorView",
                "()Landroid/view/View;",
                false
            )
            mv.visitInsn(ICONST_2)
            mv.visitVarInsn(ALOAD, 2)
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "android/view/View",
                "setLayerType",
                "(ILandroid/graphics/Paint;)V",
                false
            )

        }
    }
}
