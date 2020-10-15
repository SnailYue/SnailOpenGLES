package com.snail.snailopengles.image.filter

import android.content.Context
import android.opengl.GLES20

class ContrastColorFilter : AFilter {

    private lateinit var filter: ColorFilter.Filter

    private var hChangeType: Int = 0
    private var hChangeColor: Int = 0

    constructor(context: Context, filter: ColorFilter.Filter) : super(
        context,
        "filter/half_color_vertex.glsl",
        "filter/half_color_fragment.glsl"
    ) {
        this.filter = filter
    }

    override fun onDrawSet() {
        GLES20.glUniform1i(hChangeType, filter.getType())
        GLES20.glUniform3fv(hChangeColor, 1, filter.getData(), 0)
    }

    override fun onDrawCreatedSet(program: Int) {
        hChangeType = GLES20.glGetUniformLocation(program, "vChangeType")
        hChangeColor = GLES20.glGetUniformLocation(program, "vChangeColor")
    }

}