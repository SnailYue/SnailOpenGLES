package com.snail.snailopengles.rectangle

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class SGLView : GLSurfaceView {
    private var renderer: SGLRnder? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init()
    }

    private fun init() {
        setEGLContextClientVersion(2)
        renderer = SGLRnder(this)
        setRenderer(renderer)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    fun setShape(clazz: Class<out Shape?>) {
        renderer?.setShape(clazz)
    }
}