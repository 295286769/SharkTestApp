package com.sharkgulf.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import java.util.*
import android.telephony.gsm.GsmCellLocation


class PhoneUtil {
    companion object {
        @SuppressLint("MissingPermission")
        fun getImei(context: Context): String {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return telephonyManager.imei
            }
            return ""
        }

        @SuppressLint("MissingPermission")
        fun getImsi(context: Context): String {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as android.telephony.TelephonyManager
            val simSerialNum = tm.simSerialNumber
            return simSerialNum
        }

        @SuppressLint("MissingPermission")
        fun getDeviceId(context: Context): String {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as android.telephony.TelephonyManager
            val deviceId = tm.deviceId
            return deviceId
        }

        @SuppressLint("MissingPermission")
        fun getMacAddress(context: Context): String {
            val wifi = context.getSystemService(Context.WIFI_SERVICE) as android.net.wifi.WifiManager
            val macAddress = wifi.connectionInfo.macAddress
            return macAddress
        }

        //系统版本号
        fun getSysVesion(): Int {
            var currentapiVersion = android.os.Build.VERSION.SDK_INT
            return currentapiVersion
        }

        //型号
        fun getSysModel(): String {
            var currentapiModel = android.os.Build.MODEL
            return currentapiModel
        }

        //品牌
        fun getSysBrand(): String {
            var currentapiBrand = android.os.Build.BRAND
            return currentapiBrand
        }

        //系统语言
        fun getSystemLanguage(): String {

            return Locale.getDefault().getLanguage()

        }

        //系统地区编码
        @SuppressLint("MissingPermission")
        fun getSystemAreaCode(context: Context): Int {
            val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
            if (telephony?.phoneType == TelephonyManager.PHONE_TYPE_GSM) {
                val location = telephony?.cellLocation as GsmCellLocation
                if (location != null) {
                    return location.lac
                }
            }
            return 0

        }

        //获取sn号
        fun getSysteSN(): String {
            var serial = ""
            try {

                var c = Class.forName("android.os.SystemProperties")
                var get = c.getMethod("get", String::class.java)
                serial = get?.invoke(c, "ro.serialno") as String
            } catch (e: Exception) {
                e.printStackTrace();
            }
            return serial

        }
    }
}
