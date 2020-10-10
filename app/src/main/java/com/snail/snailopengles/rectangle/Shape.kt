package com.snail.snailopengles.rectangle

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.view.View

abstract class Shape : GLSurfaceView.Renderer {

    protected var mView: View? = null

    constructor(view: View) {
        mView = view
    }

    fun loadShader(type: Int, shaderCode: String): Int {
        var shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }
}