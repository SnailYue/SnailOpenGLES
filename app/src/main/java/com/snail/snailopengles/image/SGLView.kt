package com.snail.snailopengles.image

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.snail.snailopengles.image.filter.AFilter

/**
 * 自定义GLSurfaceView
 */
class SGLView : GLSurfaceView {

    private lateinit var render: SGLRender

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?)
            : super(context, attributeSet) {
        init()
    }

    private fun init() {
        setEGLContextClientVersion(2)
        render = SGLRender(this)
        //设置渲染器
        setRenderer(render)
        renderMode = RENDERMODE_WHEN_DIRTY
        //设置图片
        render.setImage(
            BitmapFactory.decodeStream(
                resources.assets.open("texture/keji.jpg")
            )
        )
        requestRender()
    }

    /**
     * 获取Render
     */
    fun getRender(): SGLRender {
        return render
    }

    /**
     * 设置filter
     */
    fun setFilter(filter: AFilter) {
        render.setFilter(filter)
    }
}