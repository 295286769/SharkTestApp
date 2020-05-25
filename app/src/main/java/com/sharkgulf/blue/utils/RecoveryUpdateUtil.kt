package com.sharkgulf.blue.utils

import android.content.Context
import android.os.RecoverySystem
import android.util.Log
import java.io.File
import java.io.IOException
import android.os.PowerManager
import android.text.TextUtils
import java.io.FileWriter
import java.nio.file.Files.delete
import java.util.*


class RecoveryUpdateUtil {
    companion object {
        val TAG = "RecoveryUpdateUtil"
        fun updateSystem(context: Context, updateSavePath: String) {
            val packageFile = File(updateSavePath)
            try {
                //调用升级接口
                RecoverySystem.installPackage(context, packageFile)
            } catch (e: IOException) {
                Log.e(TAG, "RecoverySystem ERROR!!!"+e.toString())
                e.printStackTrace()
            }
        }
    }
}