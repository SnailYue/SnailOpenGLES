package com.snail.snailopengles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.snail.snailopengles.image.SGLViewActivity
import com.snail.snailopengles.rectangle.RectangleActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }


    private fun initView() {
        bt_rectangle.setOnClickListener {
            startActivity(Intent(this, RectangleActivity::class.java))
        }
        bt_image.setOnClickListener {
            startActivity(Intent(this, SGLViewActivity::class.java))
        }
    }
}