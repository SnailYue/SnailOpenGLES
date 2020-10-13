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

class Cylinder : Shape {

    private var mProgram: Int = 0
    private var vertexBuffer: FloatBuffer? = null
    private var ovalTop: Oval? = null
    private var ovalBottom: Oval? = null

    private var mViewMatrix = FloatArray(16)
    private var mProgramMatrix = FloatArray(16)
    private var mMVPMatrix = FloatArray(16)

    private var height: Float = 2f
    private var radius: Float = 1f

    private var vSize: Int = 0

    constructor(view: View) : super(view) {
        ovalBottom = Oval(view)
        ovalTop = Oval(view, height)

        var pos = arrayListOf<Float>()
        var angleDegSpan = 360f / 360
        var i = 0f
        while (i < 360 + angleDegSpan) {
            pos.add((radius * Math.sin(i * Math.PI / 180f)).toFloat())
            pos.add((radius * Math.cos(i * Math.PI / 180f)).toFloat())
            pos.add(height)

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
        ovalBottom?.onSurfaceCreated(gl, config)
        ovalTop?.onSurfaceCreated(gl, config)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        var ratio: Float = width.toFloat() / height
        Matrix.frustumM(mProgramMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 20f)
        Matrix.setLookAtM(mViewMatrix, 0, 1f, -10f, -4f, 0f, 0f, 0f, 0f, 1f, 0f)
        Matrix.multiplyMM(mMVPMatrix, 0, mProgramMatrix, 0, mViewMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glUseProgram(mProgram)
        var mMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix")
        GLES20.glUniformMatrix4fv(mMatrix, 1, false, mMVPMatrix, 0)
        var mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vSize)
        GLES20.glDisableVertexAttribArray(mPositionHandle)

        ovalBottom?.setMatrix(mMVPMatrix)
        ovalBottom?.onDrawFrame(gl)

        ovalTop?.setMatrix(mMVPMatrix)
        ovalTop?.onDrawFrame(gl)
    }
}