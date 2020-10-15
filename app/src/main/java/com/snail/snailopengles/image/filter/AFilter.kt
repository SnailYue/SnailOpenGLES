package com.snail.snailopengles.image.filter

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import com.snail.snailopengles.utls.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

abstract class AFilter : GLSurfaceView.Renderer {
    private lateinit var mContext: Context
    private var mProgram: Int = 0
    private var glHPosition: Int = 0
    private var glHTexture: Int = 0
    private var glHCoordinate: Int = 0
    private var glHMatrix: Int = 0
    private var hIsHalf: Int = 0
    private var glHUxy: Int = 0
    private lateinit var mBitmap: Bitmap

    private lateinit var bPos: FloatBuffer
    private lateinit var bCoord: FloatBuffer

    private var textureId: Int = 0
    private var isHalf: Boolean = false
    private var uXY: Float = 0.0f

    private var vertex: String = ""
    private var fragment: String = ""
    private var mViewMatrix: FloatArray = FloatArray(16)
    private var mProjectMatix: FloatArray = FloatArray(16)
    private var mMVPMatrix: FloatArray = FloatArray(16)

    private val sPos = floatArrayOf(
        -1f, 1f, -1f, -1f, 1f, 1f, 1f, -1f
    )
    private val sCoord = floatArrayOf(
        0f, 0f, 0f, 1f, 1f, 0f, 1f, 1f
    )

    constructor(context: Context, vertex: String, fragment: String) {
        this.mContext = context
        this.vertex = vertex
        this.fragment = fragment

        var bb1 = ByteBuffer.allocateDirect(sPos.size * 4)
        bb1.order(ByteOrder.nativeOrder())
        bPos = bb1.asFloatBuffer()
        bPos.put(sPos)
        bPos.position(0)

        var bb2 = ByteBuffer.allocateDirect(sCoord.size * 4)
        bb2.order(ByteOrder.nativeOrder())
        bCoord = bb2.asFloatBuffer()
        bCoord.put(sCoord)
        bCoord.position(0)
    }

    fun setHalf(half: Boolean) {
        this.isHalf = half
    }

    fun setImageBUffer(buffer: IntArray, width: Int, height: Int) {
        mBitmap = Bitmap.createBitmap(buffer, width, height, Bitmap.Config.RGB_565)
    }

    fun setBitmap(bitmap: Bitmap) {
        this.mBitmap = bitmap
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        GLES20.glEnable(GLES20.GL_TEXTURE_2D)
        mProgram = ShaderUtils.createProgram(mContext!!.resources, vertex, fragment)
        glHPosition = GLES20.glGetAttribLocation(mProgram, "vPosition")
        glHCoordinate = GLES20.glGetAttribLocation(mProgram, "vCoordinate")
        glHTexture = GLES20.glGetUniformLocation(mProgram, "vTexture")
        glHMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix")
        hIsHalf = GLES20.glGetUniformLocation(mProgram, "vIsHalf")
        glHUxy = GLES20.glGetUniformLocation(mProgram, "uXY")
        onDrawCreatedSet(mProgram)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        var w = mBitmap.width
        var h = mBitmap.height
        var sWH = w.toFloat() / h.toFloat()
        var sWidthHeight = width.toFloat() / height.toFloat()
        uXY = sWidthHeight
        if (width > height) {
            if (sWH > sWidthHeight) {
                Matrix.orthoM(
                    mProjectMatix,
                    0,
                    -sWidthHeight * sWH,
                    sWidthHeight * sWH,
                    -1f,
                    1f,
                    3f,
                    5f
                )
            } else {
                Matrix.orthoM(
                    mProjectMatix,
                    0,
                    -sWidthHeight / sWH,
                    sWidthHeight / sWH,
                    -1f,
                    1f,
                    3f,
                    5f
                )
            }
        } else {
            if (sWH > sWidthHeight) {
                Matrix.orthoM(
                    mProjectMatix,
                    0,
                    -1f, 1f,
                    -1 / sWidthHeight * sWH,
                    1 / sWidthHeight * sWH,
                    3f,
                    5f
                )
            } else {
                Matrix.orthoM(
                    mProjectMatix,
                    0,
                    -1f, 1f,
                    -sWH / sWidthHeight,
                    sWH / sWidthHeight,
                    3f,
                    5f
                )
            }
        }
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 5f, 0f, 0f, 0f, 0f, 1f, 0f)
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatix, 0, mViewMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        GLES20.glUseProgram(mProgram)
        onDrawSet()
        GLES20.glUniform1i(hIsHalf, if (isHalf) 1 else 0)
        GLES20.glUniform1f(glHUxy, uXY)
        GLES20.glUniformMatrix4fv(glHMatrix, 1, false, mMVPMatrix, 0)
        GLES20.glEnableVertexAttribArray(glHPosition)
        GLES20.glEnableVertexAttribArray(glHCoordinate)
        GLES20.glUniform1i(glHTexture, 0)
        textureId = createTexture()
        GLES20.glVertexAttribPointer(glHPosition, 2, GLES20.GL_FLOAT, false, 0, bPos)
        GLES20.glVertexAttribPointer(glHCoordinate, 2, GLES20.GL_FLOAT, false, 0, bCoord)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    private fun createTexture(): Int {
        var texture = IntArray(1)
        mBitmap?.let {
            if (!it.isRecycled) {
                GLES20.glGenTextures(1, texture, 0)
                GLES20.glBindTexture(
                    GLES20.GL_TEXTURE_2D,
                    texture[0]
                )
                GLES20.glTexParameterf(
                    GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_NEAREST.toFloat()
                )
                GLES20.glTexParameterf(
                    GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_LINEAR.toFloat()
                )
                GLES20.glTexParameterf(
                    GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S,
                    GLES20.GL_CLAMP_TO_EDGE.toFloat()
                )

                GLES20.glTexParameterf(
                    GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T,
                    GLES20.GL_CLAMP_TO_EDGE.toFloat()
                )
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0)
                return texture[0]
            }
        }
        return 0
    }

    abstract fun onDrawSet()

    abstract fun onDrawCreatedSet(program: Int)

}