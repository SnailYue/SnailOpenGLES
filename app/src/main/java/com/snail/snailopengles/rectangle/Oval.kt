package com.snail.snailopengles.rectangle

import android.opengl.GLES20
import android.opengl.Matrix
import android.view.View
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Oval : Shape {

    private var vertexBuffer: FloatBuffer? = null

    private val vertexShaderCode =
        "attribute vec4 vPosition;" +
                "uniform mat4 vMatrix;" +
                "void main() {" +
                "   gl_Position = vMatrix * vPosition;" +
                "}"
    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() { " +
                "   gl_FragColor = vColor;" +
                "}"

    private var mProject: Int = 0

    private val COORDE_PER_VERTEX = 3

    private var mPositionHandle: Int = 0
    private var mColorHandle: Int = 0

    private var mViewMatrix: FloatArray = FloatArray(16)
    private var mProjectMatrix: FloatArray = FloatArray(16)
    private var mMVPMatrix: FloatArray = FloatArray(16)

    private val vertexStride: Int = 0
    private var mMatrixHandler: Int = 0
    private var radius: Float = 1.0f
    private var shapePos: FloatArray? = null
    private var height: Float = 0.0f
    private var color: FloatArray = floatArrayOf(1f, 1f, 1f, 1f)


    constructor(view: View) : this(view, 0f)

    constructor(view: View, height: Float) : super(view) {
        this.height = height
        shapePos = createPositions()
        var bb = ByteBuffer.allocateDirect(shapePos!!.size * 4)
        bb.order(ByteOrder.nativeOrder())

        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(shapePos)
        vertexBuffer?.position(0)
        var vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        var fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        mProject = GLES20.glCreateProgram()
        GLES20.glAttachShader(mProject, vertexShader)
        GLES20.glAttachShader(mProject, fragmentShader)
        GLES20.glLinkProgram(mProject)
    }


    private fun createPositions(): FloatArray {
        var data = arrayListOf<Float>()
        data.add(0f)
        data.add(0f)
        data.add(height)
        var angleDesSpan = 360f / 360

        var i = 0f
        while (i < 360 + angleDesSpan) {
            /**
             * 计算坐标点
             */
            data.add((radius * Math.sin(i * Math.PI / 180f)).toFloat())
            data.add((radius * Math.cos(i * Math.PI / 180f)).toFloat())
            data.add(height)
            i += angleDesSpan
        }
        var f = FloatArray(data.size)
        for (index in 0 until f.size) {
            f[index] = data.get(index)
        }
        return f
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        /**
         * 计算宽高比
         */
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
            COORDE_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )
        mColorHandle = GLES20.glGetUniformLocation(mProject, "vColor")
        GLES20.glUniform4fv(mColorHandle, 1, color, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, shapePos!!.size / 3)
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }

    fun setMatrix(matrix: FloatArray) {
        this.mMVPMatrix = matrix
    }
}