package com.snail.snailopengles.image

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.snail.snailopengles.R
import com.snail.snailopengles.image.filter.ColorFilter
import com.snail.snailopengles.image.filter.ContrastColorFilter
import kotlinx.android.synthetic.main.activity_sgl_view.*

class SGLViewActivity : AppCompatActivity() {
    private val TAG = SGLViewActivity::class.java.simpleName

    private var ishalf = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sgl_view)
        initView()
    }

    private fun initView() {
        cb_half?.setOnCheckedChangeListener { buttonView, isChecked ->
            ishalf = isChecked
            sglv_view.getRender().refresh()
            render()
        }
        //原图
        bt_original?.setOnClickListener {
            sglv_view?.setFilter(ContrastColorFilter(this, ColorFilter.Filter.NONE))
            render()
        }
        //黑白
        bt_black_white?.setOnClickListener {
            sglv_view?.setFilter(ContrastColorFilter(this, ColorFilter.Filter.GRAY))
            render()
        }
        //冷色调
        bt_cool?.setOnClickListener {
            sglv_view?.setFilter(ContrastColorFilter(this, ColorFilter.Filter.COOL))
            render()
        }
        //暖色调
        bt_warm?.setOnClickListener {
            sglv_view?.setFilter(ContrastColorFilter(this, ColorFilter.Filter.WARM))
            render()
        }
        //模糊
        bt_blurry?.setOnClickListener {
            sglv_view?.setFilter(ContrastColorFilter(this, ColorFilter.Filter.BLUR))
            render()
        }
        //放大镜
        bt_magnifier?.setOnClickListener {
            sglv_view?.setFilter(ContrastColorFilter(this, ColorFilter.Filter.MAGN))
            render()
        }
    }

    private fun render() {
        Log.d(TAG, "isHalf = " + ishalf)
        sglv_view.getRender().getFilter().setHalf(ishalf)
        sglv_view.requestRender()
    }
}