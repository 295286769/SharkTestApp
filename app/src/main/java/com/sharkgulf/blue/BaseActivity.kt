package com.sharkgulf.blue

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sharkgulf.blue.utils.LangeConfigUtil

open class BaseActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LangeConfigUtil.attachBaseContext(newBase))
    }
//    private var shortPress = false
//    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
//        Log.d("onKey","onKeyLongPress")
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//            shortPress = false
//            Toast.makeText(this, "longPress", Toast.LENGTH_LONG).show();
//            return true;
//        }
//        return false
//    }
//
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        Log.d("onKey","onKeyDown")
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//            if (event?.getAction() == KeyEvent.ACTION_DOWN) {
//                event?.startTracking()
//                if (event.getRepeatCount() == 0) {
//                    shortPress = true
//                }
//                return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event)
//    }
//
//    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
//        Log.d("onKey","onKeyUp")
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//            if (shortPress) {
//                Toast.makeText(this, "shortPress", Toast.LENGTH_LONG).show();
//            } else {
//                //Don't handle longpress here, because the user will have to get his finger back up first
//            }
//            shortPress = false;
//            return true;
//        }
//        return super.onKeyUp(keyCode, event)
//    }
}