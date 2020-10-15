package com.snail.snailopengles.image.filter

import android.content.Context
import android.opengl.GLES20

class ColorFilter : AFilter {

    private lateinit var filter: Filter
    private var hChangeType: Int = 0
    private var hChangeColor: Int = 0

    constructor(context: Context, filter: Filter) : super(
        context,
        "filter/default_vertex.glsl",
        "filter/color_fragment.glsl"
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

    /**
     * 滤镜类型枚举
     */
    enum class Filter {
        NONE(0, floatArrayOf(0f, 0f, 0f)),
        GRAY(1, floatArrayOf(0.299f, 0.587f, 0.114f)),
        COOL(2, floatArrayOf(0f, 0f, 0.1f)),
        WARM(2, floatArrayOf(0.1f, 0.1f, 0.0f)),
        BLUR(3, floatArrayOf(0.006f, 0.004f, 0.002f)),
        MAGN(4, floatArrayOf(0.0f, 0.0f, 0.4f));

        private var vChangeType: Int = 0
        private var data: FloatArray = floatArrayOf(0f)

        constructor(changeType: Int, data: FloatArray) {
            this.vChangeType = changeType
            this.data = data
        }

        fun getType(): Int {
            return vChangeType
        }

        fun getData(): FloatArray {
            return data
        }
    }
}