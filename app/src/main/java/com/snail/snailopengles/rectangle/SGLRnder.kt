package com.snail.snailopengles.rectangle

import android.opengl.GLES20
import android.view.View
import java.lang.Exception
import java.lang.reflect.Constructor
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SGLRnder : Shape {

    private var shape: Shape? = null
    private var clazz: Class<out Shape?> = Cube::class.java

    constructor(view: View) : super(view)

    fun setShape(shape: Class<out Shape?>) {
        this.clazz = shape
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        try {
            val constructor: Constructor<*> = clazz.getDeclaredConstructor(View::class.java)
            constructor.isAccessible = true
            shape = constructor.newInstance(mView) as Shape
        } catch (e: Exception) {
            shape = Cube(mView!!)
        }
        shape?.onSurfaceCreated(gl, config)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        shape?.onSurfaceChanged(gl, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        shape?.onDrawFrame(gl)
    }
}