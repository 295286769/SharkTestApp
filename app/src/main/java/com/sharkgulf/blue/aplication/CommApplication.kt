package com.sharkgulf.blue.aplication

import android.app.Application
import android.content.res.Configuration
import android.os.Build
import com.sharkgulf.blue.utils.LangeConfigUtil
import java.util.*


class CommApplication:Application() {
    companion object {
        var mContext: CommApplication? = null
        fun getInstance(): CommApplication? {
            return mContext
        }

    }
    override fun onCreate() {
        super.onCreate()
        mContext=this
        val _UserLocale = LangeConfigUtil.getUserLocale(this)
        LangeConfigUtil.updateLocale(this, _UserLocale)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        var _UserLocale=LangeConfigUtil.getUserLocale(this)
        //系统语言改变了应用保持之前设置的语言
        _UserLocale?.let {
            Locale.setDefault(it)
            var _Configuration =  Configuration(newConfig)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                _Configuration.setLocale(it)
                this.createConfigurationContext( _Configuration)
            } else {
                _Configuration.locale =it
                getResources().updateConfiguration(_Configuration, getResources().getDisplayMetrics())
            }
        }

}
}