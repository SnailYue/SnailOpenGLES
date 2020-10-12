package com.snail.snailopengles.rectangle

import android.opengl.GLES20
import android.opengl.Matrix
import android.view.View
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Square : Shape {

    private var vertexBuffer: FloatBuffer? = null
    private var indexBuffer: ShortBuffer? = null

    private val vertexShaderCode =
        "attribute vec4 vPosition;" +
                "uniform mat4 vMatrix;" +
                "void main() {" +
                "   gl_Position = vMatrix * vPosition;" +
                "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "   gl_FragColor = vColor;" +
                "}"
    private var mProject: Int = 0

    private val COORDS_PER_VERTEX = 3

    private val triangleCoords = floatArrayOf(
        -0.5f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.5f, 0.5f, 0.0f
    )

    private val index = shortArrayOf(
        0, 1, 2, 0, 2, 3
    )

    private var mPositionHandle: Int = 0
    private var mColorHandle: Int = 0

    private var mViewMatrix = FloatArray(16)
    private var mProjectMatrix = FloatArray(16)
    private var mMVPMatrix = FloatArray(16)

    private var vertexStride = COORDS_PER_VERTEX * 4
    private var mMatrixHandler: Int = 0
    private var color = floatArrayOf(1f, 1f, 1f, 1f)


    constructor(view: View) : super(view) {
        var bb1 = ByteBuffer.allocateDirect(triangleCoords.size * 4)
        bb1.order(ByteOrder.nativeOrder())
        vertexBuffer = bb1.asFloatBuffer()
        vertexBuffer?.put(triangleCoords)
        vertexBuffer?.position(0)

        var bb2 = ByteBuffer.allocateDirect(index.size * 2)
        bb2.order(ByteOrder.nativeOrder())
        indexBuffer = bb2.asShortBuffer()
        indexBuffer?.put(index)
        indexBuffer?.position(0)

        var vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        var fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        mProject = GLES20.glCreateProgram()
        GLES20.glAttachShader(mProject, vertexShader)
        GLES20.glAttachShader(mProject, fragmentShader)
        GLES20.glLinkProgram(mProject)
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
        GLES20.glUseProgram(mProject)
        mMatrixHandler = GLES20.glGetUniformLocation(mProject, "vMatrix")
        GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0)

        mPositionHandle = GLES20.glGetAttribLocation(mProject, "vPosition")
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        GLES20.glVertexAttribPointer(
            mPositionHandle,
            COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )
        mColorHandle = GLES20.glGetUniformLocation(mProject, "vColor")
        GLES20.glUniform4fv(mColorHandle, 1, color, 0)
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            index.size,
            GLES20.GL_UNSIGNED_SHORT,
            indexBuffer
        )
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }
}