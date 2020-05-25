package com.sharkgulf.blue

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sharkgulf.blue.utils.LangeConfigUtil
import kotlinx.android.synthetic.main.activity_lange.*
import android.content.Intent
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class LangeActiivty:BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lange)
        btn_china.setOnClickListener {
            if(LangeConfigUtil.needUpdateLocale(this,LangeConfigUtil.LOCALE_ENGLISH)){
                LangeConfigUtil.updateLocale(this,LangeConfigUtil.LOCALE_ENGLISH)
                restartAct()
            }

        }
        btn_english.setOnClickListener {
            if(LangeConfigUtil.needUpdateLocale(this,LangeConfigUtil.LOCALE_CHINESE)){
                LangeConfigUtil.updateLocale(this,LangeConfigUtil.LOCALE_CHINESE)
                restartAct()
            }
        }
    }
    /**
     * 重启当前Activity
     */
    private fun restartAct() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
        //清除Activity退出和进入的动画
        overridePendingTransition(0, 0)
    }
}