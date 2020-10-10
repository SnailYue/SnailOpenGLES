package com.snail.snailopengles.rectangle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.snail.snailopengles.R
import kotlinx.android.synthetic.main.activity_rectangle_render.*

class RectangleRenderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rectangle_render)
        initView()
    }

    override fun onResume() {
        super.onResume()
        sglv_view?.onResume()
    }

    override fun onPause() {
        super.onPause()
        sglv_view?.onPause()
    }

    private fun initView() {
        sglv_view?.setShape(intent.getSerializableExtra("name") as Class<out Shape>)
    }
}