package com.sharkgulf.checkandinstallapk.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import java.util.ArrayList
import java.util.HashMap

class PermissionUtil {
    interface RequestPermissionListener {
        fun onRequestPermissionSuccess()

        fun onRequestPermissionFail(grantResults: IntArray)
    }
companion object{
    val PERMISSIONREQUESTCODE:Int=1
    var showSystemSetting = true
    /**
     * PermissionRequestList is a map that used to store permission list. The key make up by
     * activity's
     * simpleName and requestCode that concatenate by a '#'
     */
     class PermissionRequestList {

        private var mPermissionReqs: MutableMap<String, Request>? = null

        class Request {
            var activity: Activity? = null
            var requestCode: Int = 0
            var permissions: Array<String>? = null
            var listener: RequestPermissionListener? = null
        }

        fun add(req: Request?) {
            if (req != null) {
                if (mPermissionReqs == null) {
                    mPermissionReqs = HashMap(5)
                }
                mPermissionReqs?.let { it[getRequestKey(req?.activity, req?.requestCode)] = req }
            }
        }

         fun pop(activity: Activity?, requestCode: Int): Request? {
            return mPermissionReqs?.let { it.remove(getRequestKey(activity, requestCode)) }
        }

        /**
         * Get a request's key by activity and requestCode.
         *
         * @param activity    The request's activity.
         * @param requestCode The request's requestCode.
         * @return The key of request.
         */
        private fun getRequestKey(activity: Activity?, requestCode: Int): String {
            return String.format("%s#%d", activity?.javaClass?.simpleName, requestCode)
        }
    }
        private val mReqs = PermissionRequestList()

        /**
         * Check whether the api is not less than 23.
         *
         * @return If the api is not less than 23, then return true, else return false.
         */
        private fun isOverMarshmallow(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        }

        /**
         * request permissions. If the api is not less than 23, then request permissions in running
         * time,
         * else we regard the request as success.
         *
         * @param activity
         * @param requestCode
         * @param permissions
         * @param listener
         */
        fun requestPermisions(
            activity: Activity?,
            requestCode: Int,
            permissions: Array<String>?,
            listener: RequestPermissionListener?
        ) {
            if (activity != null && permissions != null && permissions.size != 0 && listener != null) {
                if (!isOverMarshmallow()) {
                    listener.onRequestPermissionSuccess()
                } else {
                    val denied = findDeniedPermissions(activity, permissions)
                    if (denied.size > 0) {
                        doRequestPermisions(activity, requestCode, denied.toTypedArray(), listener)
                    } else {
                        listener.onRequestPermissionSuccess()
                    }
                }
            }
        }

        /**
         * find permissions that don't grant in a given permissions.
         *
         * @param activity
         * @param permissions The permissions need to be checked.
         * @return
         */
        @TargetApi(value = Build.VERSION_CODES.M)
        private fun findDeniedPermissions(activity: Activity, permissions: Array<String>): List<String> {
            //创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
            val denyPermissions = ArrayList<String>()
            //逐个判断你要的权限是否已经通过
            for (value in permissions) {
                try {
                    if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {//添加还未授予的权限
                        denyPermissions.add(value)
                    }
                } catch (e: Exception) {
                    // do nothing.
                }

            }
            return denyPermissions
        }

    /**
     * 逐个申请权限
     */
        @TargetApi(Build.VERSION_CODES.M)
        private fun doRequestPermisions(
            activity: Activity?,
            requestCode: Int,
            permissions: Array<String>?,
            listener: RequestPermissionListener?
        ) {
            if (activity != null && permissions != null && permissions.size != 0 && listener != null) {
                val req = PermissionRequestList.Request()
                req.activity = activity
                req.requestCode = requestCode
                req.permissions = permissions
                req.listener = listener
                mReqs.add(req)

                try {
                    activity.requestPermissions(permissions, requestCode)
                } catch (e: Exception) {
                    // do nothing.
                }

            }
        }

        /**
         * An callback must be invoked by the activity's onRequestPermissionsResult.
         *
         * @param activity     The activity correspond to `requestPermisions`'s activity.
         * @param requestCode  The requestCode correspond to `requestPermisions`'s requestCode.
         * @param permissions  The permissions that request to grant permission.
         * @param grantResults The result of granting permissions.
         */
        fun onRequestPermissionsResult(
            activity: Activity?, requestCode: Int,
            permissions: Array<String>, grantResults: IntArray) {
            if (activity != null) {
                val req = mReqs?.pop(activity, requestCode)

                if (req != null && req.listener != null && grantResults.size > 0) {
                    var hasPermission = true
                    for (permission in grantResults) {

                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            hasPermission = false
                            break
                        }
                    }
                    if (hasPermission) {//已经通过权限申请
                        req!!.listener!!.onRequestPermissionSuccess()
                    } else {//拒绝过弹框提示让用户选择

                    }
                }
            }
        }

}



}