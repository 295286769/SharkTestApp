package com.sharkgulf.blue.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

class StartActivityUtils {
    companion object{
        //跳转系统页面
        val ACTIVITYREQUSETCODE = 100
        //跳转未知来源权限
        val PERMISSIONREQUSETCODE = 101
        val activityResultCode = 1000
        /**
         * 安装apk
         */
        fun installAPK(activity: Activity, path: String) {
            val apkFile = File(path)
            if (!apkFile.exists()) {
                Log.d("TestApp","!apkFile.exists()")
                return
            }
            val intent = Intent(Intent.ACTION_VIEW)
            //由于没有在Activity环境下启动Activity,所以设置下面的标签
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //      安装完成后，启动app（源码中少了这句话）

            if (null != apkFile) {
                try {
                    //兼容7.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                        val contentUri = FileProvider.getUriForFile(
                            activity,
                            activity.packageName + ".fileprovider",
                            apkFile
                        )
                        //添加这一句表示对目标应用临时授权该Uri所代表的文件
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
                        //兼容8.0
                        startInstallPermissionSettingActivity(activity)
                    } else {
                        intent.setDataAndType(
                            Uri.fromFile(apkFile),
                            "application/vnd.android.package-archive"
                        )
                    }
                    startInstallApk(activity, intent)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    Log.e("TestApp",e.message)
                }

            }
        }

        fun startInstallApk(context: Context, intent: Intent) {
            Log.d("TestApp","startInstallApk")
            if (context.packageManager.queryIntentActivities(intent, 0).size > 0) {
                context.startActivity(intent)
            }
        }

        /**
         * 跳转到允许未知来源权限页面
         */
        fun startInstallPermissionSettingActivity(activity: Activity) {
            Log.d("TestApp","startInstallPermissionSettingActivity")
            //注意这个是8.0新API
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val hasInstallPermission = activity.packageManager.canRequestPackageInstalls()
                Log.d("TestApp","hasInstallPermission="+hasInstallPermission)
                if (!hasInstallPermission) {
                    val packageURI = Uri.parse("package:${activity.packageName}")
                    val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivityForResult(intent, PERMISSIONREQUSETCODE)
                    return
                }
            }


        }
    }
}