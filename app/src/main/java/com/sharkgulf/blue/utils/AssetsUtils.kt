package com.sharkgulf.blue.utils

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.nio.charset.Charset

class AssetsUtils {
    companion object{
        var TAG="AssetsUtils"
        fun readAssetsFile(context:Context,fileName: String):String{
            var buder: StringBuilder
             try {
                var inputString:InputStream=context.assets.openFd(fileName).createInputStream()
                var reader=BufferedReader(InputStreamReader(inputString))
                buder = StringBuilder()
                var name = ""
                while (reader.readLine().also { name=it }!=null){
                    buder.append(name)
                }
                return buder.toString()

//                 var size = inputString.available()
//                 // Read the entire asset into a local byte buffer.
//                 var buffer =  ByteArray(size)
//                 inputString.read(buffer)
//                 inputString.close();
//                 // Convert the buffer into a string.
//                 return String(buffer)
            } catch (e: Exception) {
                 Log.e(TAG,"Exception="+e.message)
             }
            return ""

        }
    }
}