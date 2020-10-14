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

class BallWithLight : Shape {

    private var step: Float = 10f
    private var vertexBuffer: FloatBuffer? = null
    private var vSize: Int = 0
    private var mProgram: Int = 0

    private var mViewMatrix: FloatArray = FloatArray(16)
    private var mProjectMatrix: FloatArray = FloatArray(16)
    private var mMVPMatrix: FloatArray = FloatArray(16)

    constructor(view: View) : super(view) {
        var dataPos = createPos()
        var buffer = ByteBuffer.allocateDirect(dataPos.size * 4)
        buffer.order(ByteOrder.nativeOrder())
        vertexBuffer = buffer.asFloatBuffer()
        vertexBuffer?.put(dataPos)
        vertexBuffer?.position(0)
        vSize = dataPos.size / 3
    }


    private fun createPos(): FloatArray {
        var data = arrayListOf<Float>()
        var i: Float = -90f
        while (i < 90 + step) {
            var r1 = Math.cos(i * Math.PI / 180.0).toFloat()
            var r2 = Math.cos((i + step) * Math.PI / 180.0).toFloat()
            var h1 = Math.sin(i * Math.PI / 180.0).toFloat()
            var h2 = Math.sin((i + step) * Math.PI / 180.0).toFloat()
            var step2 = step * 2
            var j: Float = 0.0f
            while (j < 360.0f + step) {
                var cos = Math.cos(j * Math.PI / 180.0).toFloat()
                var sin = Math.sin(j * Math.PI / 180.0).toFloat()
                data.add(r2 * cos)
                data.add(h2)
                data.add(r2 * sin)
                data.add(r1 * cos)
                data.add(h1)
                data.add(r1 * sin)
                j += step2
            }
            i += step
        }
        var f = FloatArray(data.size)
        for (index in 0 until f.size) {
            f[index] = data.get(index)
        }
        return f
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        mProgram = ShaderUtils.createProgram(
            mView!!.resources,
            "vshader/BallWithLight.glsl",
            "fshader/BallWithLight.glsl"
        )
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        var ratio = width.toFloat() / height
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 20f)
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 10f, 0f, 0f, 0f, 0f, 1f, 0f)
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
    }
}