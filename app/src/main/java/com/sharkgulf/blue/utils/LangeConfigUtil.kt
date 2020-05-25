package com.sharkgulf.blue.utils

import android.annotation.TargetApi
import android.os.Build
import android.content.Context
import android.os.LocaleList
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import java.util.*


class LangeConfigUtil {
    companion object{
        var ACTIONACTIVITY="Locale"
        /**
         * 中文
         */
        val LOCALE_CHINESE = Locale.CHINESE
        /**
         * 英文
         */
        val LOCALE_ENGLISH = Locale.ENGLISH
        /**
         * 俄文
         */
        val LOCALE_RUSSIAN = Locale("ru")
        /**
         * 保存SharedPreferences的文件名
         */
        private val LOCALE_FILE = "LOCALE_FILE"
        /**
         * 保存Locale的key
         */
        private val LOCALE_KEY = "LOCALE_KEY"

        /**
         * 获取用户设置的Locale
         * @param pContext Context
         * @return Locale
         */
        fun getUserLocale(pContext: Context): Locale {
            val _SpLocale = pContext.getSharedPreferences(LOCALE_FILE, Context.MODE_PRIVATE)
            val _LocaleJson = _SpLocale.getString(LOCALE_KEY, "")
            return jsonToLocale(_LocaleJson)
        }

        /**
         * 获取当前的Locale
         * @param pContext Context
         * @return Locale
         */
        fun getCurrentLocale(pContext: Context): Locale {
            val _Locale: Locale
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //7.0有多语言设置获取顶部的语言
                _Locale = pContext.getResources().getConfiguration().getLocales().get(0)
            } else {
                _Locale = pContext.getResources().getConfiguration().locale
            }
            return _Locale
        }

        /**
         * 保存用户设置的Locale
         * @param pContext Context
         * @param pUserLocale Locale
         */
        fun saveUserLocale(pContext: Context, pUserLocale: Locale) {
            val _SpLocal = pContext.getSharedPreferences(LOCALE_FILE, Context.MODE_PRIVATE)
            val _Edit = _SpLocal.edit()
            val _LocaleJson = localeToJson(pUserLocale)
            _Edit.putString(LOCALE_KEY, _LocaleJson)
            _Edit.apply()
        }

        /**
         * Locale转成json
         * @param pUserLocale UserLocale
         * @return json String
         */
        private fun localeToJson(pUserLocale: Locale): String {
            val _Gson = Gson()
            return _Gson.toJson(pUserLocale)
        }

        /**
         * json转成Locale
         * @param pLocaleJson LocaleJson
         * @return Locale
         */
        private fun jsonToLocale(pLocaleJson: String?): Locale {
            val _Gson = Gson()
            if(TextUtils.isEmpty(pLocaleJson)){
                return _Gson.fromJson(Locale.CHINA.language, Locale::class.java)
            }
            return _Gson.fromJson(pLocaleJson, Locale::class.java)
        }

        /**
         * 更新Locale
         * @param pContext Context
         * @param pNewUserLocale New User Locale
         */
        fun updateLocale(pContext: Context, pNewUserLocale: Locale) {
            if (needUpdateLocale(pContext, pNewUserLocale)) {
                Log.d("updateLocale=",""+pNewUserLocale)
                val _Configuration = pContext.getResources().getConfiguration()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    _Configuration.setLocale(pNewUserLocale)
//                    val _DisplayMetrics = pContext.getResources().getDisplayMetrics()
//                    pContext.getResources().updateConfiguration(_Configuration, _DisplayMetrics)
                    pContext.createConfigurationContext( _Configuration)
                } else {
                    _Configuration.locale = pNewUserLocale
                    val _DisplayMetrics = pContext.getResources().getDisplayMetrics()
                    pContext.getResources().updateConfiguration(_Configuration, _DisplayMetrics)
                }
                saveUserLocale(pContext, pNewUserLocale)
            }
        }
        fun updateLocaleNotNeed(pContext: Context, pNewUserLocale: Locale){
            val _Configuration = pContext.getResources().getConfiguration()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                _Configuration.setLocale(pNewUserLocale)
                pContext.applicationContext.createConfigurationContext( _Configuration)
            } else {
                _Configuration.locale = pNewUserLocale
                val _DisplayMetrics = pContext.getResources().getDisplayMetrics()
                pContext.getResources().updateConfiguration(_Configuration, _DisplayMetrics)
            }
        }

        /**
         * 判断需不需要更新
         * @param pContext Context
         * @param pNewUserLocale New User Locale
         * @return true / false
         */
        fun needUpdateLocale(pContext: Context, pNewUserLocale: Locale?): Boolean {
            return pNewUserLocale != null && !getUserLocale(pContext).equals(pNewUserLocale)
        }
        fun  attachBaseContext( context:Context?):Context? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 8.0需要使用createConfigurationContext处理
                return updateResources(context)
            } else {
                return context
            }
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun  updateResources( context:Context?) :Context?{
            var resources = context?.getResources()
            var configuration = resources?.getConfiguration()
            context?.let {
                var locale = getUserLocale(it)// getSetLocale方法是获取新设置的语言
                configuration?.setLocale(locale)
                configuration?.setLocales( LocaleList(locale))

            }
            return context?.createConfigurationContext(configuration)
        }

    }



}