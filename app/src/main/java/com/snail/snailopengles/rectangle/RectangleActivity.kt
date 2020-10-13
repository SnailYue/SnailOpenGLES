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
            startActivity(Triangle::class.java)
        }
        bt_equilateral_triangle?.setOnClickListener {
            startActivity(EquilateralTriangle::class.java)
        }
        bt_equilateral_triangle_color?.setOnClickListener {
            startActivity(EquilateralTriangleColor::class.java)
        }
        bt_square?.setOnClickListener {
            startActivity(Square::class.java)
        }
        bt_oval?.setOnClickListener {
            startActivity(Oval::class.java)
        }
        bt_cube?.setOnClickListener {
            startActivity(Cube::class.java)
        }
        bt_cone?.setOnClickListener {
            startActivity(Cone::class.java)
        }
        bt_cylinder?.setOnClickListener {
            startActivity(Cylinder::class.java)
        }
    }

    /**
     * 启动activity
     */
    private fun startActivity(clazz: Class<out Shape>) {
        var intent = Intent(this, RectangleRenderActivity::class.java)
        intent.putExtra("name", clazz)
        startActivity(intent)
    }

}