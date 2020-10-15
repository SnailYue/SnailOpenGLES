package com.snail.snailopengles.image

import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.view.View
import com.snail.snailopengles.image.filter.AFilter
import com.snail.snailopengles.image.filter.ColorFilter
import com.snail.snailopengles.image.filter.ContrastColorFilter
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 自定义Render
 */
class SGLRender : GLSurfaceView.Renderer {
    private lateinit var mFilter: AFilter
    private var bitmap: Bitmap? = null
    private var width: Int = 0
    private var height: Int = 0
    private var refreshFlag: Boolean = false
    private lateinit var config: EGLConfig

    constructor(view: View) {
        mFilter = ContrastColorFilter(view.context, ColorFilter.Filter.NONE)
    }

    fun setFilter(filter: AFilter) {
        refreshFlag = true
        mFilter = filter
        bitmap?.let {
            mFilter.setBitmap(it)
        }
    }

    fun setImageBuffer(buffer: IntArray, width: Int, height: Int) {
        bitmap = Bitmap.createBitmap(buffer, width, height, Bitmap.Config.RGB_565)
        mFilter.setBitmap(bitmap!!)
    }

    fun refresh() {
        refreshFlag = true
    }

    fun getFilter(): AFilter {
        return mFilter
    }

    fun setImage(bitmap: Bitmap) {
        this.bitmap = bitmap
        mFilter.setBitmap(bitmap)
    }

    override fun onDrawFrame(gl: GL10?) {
        if (refreshFlag and (width != 0) and (height != 0)) {
            mFilter.onSurfaceCreated(gl, config)
            mFilter.onSurfaceChanged(gl, width, height)
            refreshFlag = false
        }
        mFilter.onDrawFrame(gl)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height
        mFilter.onSurfaceChanged(gl, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        this.config = config!!
        mFilter.onSurfaceCreated(gl, config)
    }
}