package com.snail.snailopengles.rectangle

import android.opengl.GLES20
import android.view.View
import com.snail.snailopengles.utls.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Triangle : Shape {

    private var vertexBuffer: FloatBuffer? = null
    private var mProgram = 0

    val COORDS_PER_VERTEX = 3

    var triangleCoords = floatArrayOf(
        0.5f, 0.5f, 0.0f,  // top
        -0.5f, -0.5f, 0.0f,  // bottom left
        0.5f, -0.5f, 0.0f // bottom right
    )

    private var mPositionHandle = 0
    private var mColorHandle = 0

    /**
     * 顶点个数
     */
    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX

    /**
     * 顶点之间的偏移量,每个顶点四个字节
     */
    private val vertexStride: Int = COORDS_PER_VERTEX * 4

    /**
     * 设置颜色，依次为红绿蓝和透明通道
     */
    var color = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)

    constructor(view: View) : super(view) {
        var bb = ByteBuffer.allocateDirect(triangleCoords.size * 4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(triangleCoords)
        vertexBuffer?.position(0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        mProgram =
            ShaderUtils.createProgram(
                mView!!.resources,
                "vshader/Triangle.glsl",
                "fshader/Triangle.glsl"
            )
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glUseProgram(mProgram)
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        GLES20.glVertexAttribPointer(
            mPositionHandle,
            COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor")
        GLES20.glUniform4fv(mColorHandle, 1, color, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }
}