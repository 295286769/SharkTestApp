package com.sharkgulf.blue.utils

import android.os.Environment
import android.util.Log
import java.io.File

class FileUtils {
    companion object{
        val TAG="FileUtils"
        val ZIPPATH="zipPath"
        val SHARKZIPNAME="sharkZip.zip"
        //全路径创建文件
        fun createFile( filePath:String,fileName:String): File?{
            var file: File? =null
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                file =  File(Environment.getExternalStorageDirectory(), File.separator+filePath)
                if (!file?.exists()) {
                    file?.mkdirs()
                }
            } else {
                file= File(Environment.getDataDirectory().getAbsolutePath(), File.separator+filePath)
                if (!file?.exists()) {
                    file?.mkdirs()
                }

            }
            Log.d("path",file.absolutePath)
           var fileNew= File(file.absolutePath+ File.separator+fileName)
            if(!fileNew?.exists()){
                fileNew.getParentFile().mkdirs()
                fileNew?.createNewFile()
            }
            return fileNew
        }
        fun createSDCardFile( filePath:String,fileName:String): File?{
            var file: File? =null
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                file =  File(Environment.getExternalStorageDirectory(), File.separator+filePath)
                if (!file?.exists()) {
                    file?.mkdirs()
                }
                Log.d(TAG,file?.absolutePath)
                var fileNew= File(file?.absolutePath+ File.separator+fileName)
                if(!fileNew?.exists()){
                    fileNew.getParentFile().mkdirs()
                    fileNew?.createNewFile()
                }
                Log.d(TAG,fileNew?.absolutePath)
                return fileNew
            }

            return null
        }
    }
}