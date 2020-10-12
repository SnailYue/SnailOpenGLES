package com.snail.snailopengles.rectangle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.snail.snailopengles.R
import kotlinx.android.synthetic.main.activity_rectangle.*

class RectangleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rectangle)
        initView()
    }


    private fun initView() {
        bt_triangle?.setOnClickListener {
            var intent = Intent(this, RectangleRenderActivity::class.java)
            intent.putExtra("name", Triangle::class.java)
            startActivity(intent)
        }
        bt_equilateral_triangle?.setOnClickListener {
            var intent = Intent(this, RectangleRenderActivity::class.java)
            intent.putExtra("name", EquilateralTriangle::class.java)
            startActivity(intent)
        }
        bt_equilateral_triangle_color?.setOnClickListener {
            var intent = Intent(this, RectangleRenderActivity::class.java)
            intent.putExtra("name", EquilateralTriangleColor::class.java)
            startActivity(intent)
        }
    }
}