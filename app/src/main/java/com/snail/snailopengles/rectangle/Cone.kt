package com.snail.snailopengles.rectangle

import android.opengl.GLES20
import android.opengl.Matrix
import android.view.View
import com.snail.snailopengles.utls.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Cone : Shape {

    private var mProgram: Int = 0
    private var oval: Oval
    private var vertexBuffer: FloatBuffer? = null

    private var mViewMatrix = FloatArray(16)
    private var mProjectMatrix = FloatArray(16)
    private var mMVPMatrix = FloatArray(16)

    private var height = 2.0f
    private var radius = 1.0f

    private var vSize: Int = 0

    constructor(view: View) : super(view) {
        oval = Oval(view)
        var pos = arrayListOf<Float>()
        pos.add(0f)
        pos.add(0f)
        pos.add(height)
        var angleDegSpan = 360f / 360
        var i = 0f
        while (i < 360 + angleDegSpan) {
            pos.add((radius * Math.sin(i * Math.PI / 180f)).toFloat())
            pos.add((radius * Math.cos(i * Math.PI / 180f)).toFloat())
            pos.add(0f)
            i += angleDegSpan
        }
        var d = FloatArray(pos.size)
        for (index in 0 until d.size) {
            d[index] = pos.get(index)
        }
        vSize = d.size / 3
        var buffer = ByteBuffer.allocateDirect(d.size * 4)
        buffer.order(ByteOrder.nativeOrder())
        vertexBuffer = buffer.asFloatBuffer()
        vertexBuffer?.put(d)
        vertexBuffer?.position(0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        mProgram =
            ShaderUtils.createProgram(mView!!.resources, "vshader/Cone.glsl", "fshader/Cone.glsl")
        oval.onSurfaceCreated(gl, config)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        var ratio = width.toFloat() / height
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 20f)
        Matrix.setLookAtM(mViewMatrix, 0, 1f, -10f, -4f, 0f, 0f, 0f, 0f, 1f, 0f)
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glUseProgram(mProgram)
        var mMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix")
        GLES20.glUniformMatrix4fv(mMatrix, 1, false, mMVPMatrix, 0)

        var mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vSize)
        GLES20.glDisableVertexAttribArray(mPositionHandle)

        oval.setMatrix(mMVPMatrix)
        oval.onDrawFrame(gl)
    }
}