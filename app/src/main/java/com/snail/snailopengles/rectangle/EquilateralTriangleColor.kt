package com.snail.snailopengles.rectangle

import android.opengl.GLES20
import android.opengl.Matrix
import android.view.View
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class EquilateralTriangleColor : Shape {

    private var vertexBuffer: FloatBuffer? = null
    private var colorBuffer: FloatBuffer? = null

    private val vertexShaderCode =
        "attribute vec4 vPosition;" +
                "uniform mat4 vMatrix;" +
                "varying vec4 vColor;" +
                "attribute vec4 aColor;" +
                "void main() { " +
                "   gl_Position = vMatrix * vPosition;" +
                "   vColor = aColor;" +
                "}"
    private val fragmentShaderCode =
        "precision mediump float;" +
                "varying vec4 vColor;" +
                "void main() {" +
                "   gl_FragColor = vColor;" +
                "}"

    private var mProgram: Int = 0
    private val COORDS_PER_VERTEX = 3
    private val triangleCoords =
        floatArrayOf(
            0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
        )

    private var mPositionHandle: Int = 0
    private var mColorHandle: Int = 0

    private var mViewMatrix: FloatArray = FloatArray(16)
    private var mProjectMatrix: FloatArray = FloatArray(16)
    private var mMVPMatrix: FloatArray = FloatArray(16)

    private val vertexCount = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride = COORDS_PER_VERTEX * 4
    private var mMatrixHandler: Int = 0

    private var color: FloatArray = floatArrayOf(
        0.0f, 1.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f
    )

    constructor(view: View) : super(view) {
        var bb1 = ByteBuffer.allocateDirect(triangleCoords.size * 4)
        bb1.order(ByteOrder.nativeOrder())
        vertexBuffer = bb1.asFloatBuffer()
        vertexBuffer?.put(triangleCoords)
        vertexBuffer?.position(0)

        var bb2 = ByteBuffer.allocateDirect(color.size * 4)
        bb2.order(ByteOrder.nativeOrder())
        colorBuffer = bb2.asFloatBuffer()
        colorBuffer?.put(color)
        colorBuffer?.position(0)

        var vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        var fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        mProgram = GLES20.glCreateProgram()
        GLES20.glAttachShader(mProgram, vertexShader)
        GLES20.glAttachShader(mProgram, fragmentShader)
        GLES20.glLinkProgram(mProgram)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        var ratio: Float = width.toFloat() / height
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 7f, 0f, 0f, 0f, 0f, 1f, 0f)
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        GLES20.glUseProgram(mProgram)

        mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "vMatrix")
        GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0)

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

        mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor")
        GLES20.glEnableVertexAttribArray(mColorHandle)
        GLES20.glVertexAttribPointer(
            mColorHandle,
            4,
            GLES20.GL_FLOAT,
            false,
            0,
            colorBuffer
        )

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }
}