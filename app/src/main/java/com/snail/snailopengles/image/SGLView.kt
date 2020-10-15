package com.snail.snailopengles.image

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.snail.snailopengles.image.filter.AFilter

class SGLView : GLSurfaceView {

    private lateinit var render: SGLRender

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init()
    }

    private fun init() {
        setEGLContextClientVersion(2)
        render = SGLRender(this)
        setRenderer(render)
        renderMode = RENDERMODE_WHEN_DIRTY
        render.setImage(BitmapFactory.decodeStream(resources.assets.open("texture/keji.jpg")))
        requestRender()
    }

    fun getRender(): SGLRender {
        return render
    }

    fun setFilter(filter: AFilter) {
        render.setFilter(filter)
    }
}