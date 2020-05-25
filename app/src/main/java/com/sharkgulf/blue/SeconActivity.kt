package com.sharkgulf.blue

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sencond.*

class SeconActivity:BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sencond)
        btn_china.setOnClickListener {
            var intent = Intent(this, LangeActiivty::class.java)
            startActivity(intent)
        }
    }
}