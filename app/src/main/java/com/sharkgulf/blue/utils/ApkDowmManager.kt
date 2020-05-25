package com.sharkgulf.blue.utils

import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.sharkgulf.blue.aplication.CommApplication
import com.sharkgulf.common.model.ApplicationBean
import com.sharkgulf.common.model.ModulefirmBean
import com.sharkgulf.common.model.ModulesBean
import com.sharkgulf.common.utils.ApplicationUtils
import com.sharkgulf.common.utils.PhoneUtil
import okhttp3.*
import java.io.IOException
import java.io.File
import java.io.RandomAccessFile
import java.io.InputStream
import java.util.*
import java.util.concurrent.*
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession


class ApkDowmManager {

    //成功完成
    val TYPE_SUCCESS = 0
    //失败
    val TYPE_FAILED = 1
    //暂停
    val TYPE_PAUSED = 2
    //取消
    val TYPE_CANCELED = 3
    //进度
    val TYPE_PRESS = 4
    var threadServer = Executors.newSingleThreadExecutor()
    var okHttpClient: OkHttpClient? = null
    var request: Request? = null
    var callHttpClient: Call? = null
    private var downloadUrl: String = ""
    private var downloadFile: File? = null
    //得到下载内容的大小
    var contentLength = 0L
    //得到文件的大小
    var downloadLength = 0L
    private var isCanceled = false

    private var isPaused = false
    //是否正在下载
    private var isDownLoading = false

    companion object {
        val TAG = "ApkDowmManager"
        const val testUrl =
            "http://storage.blueshark.com/ota/app-debug.apk"
//        val UPDATEURL = "dev/getmodulefirm"//固件升级接口
//        val BASE_URL = "http://dev.d.bluesharkmotor.com/"
        //apk路径
        const val apkPath = "apkPath"
        //apk名称
        const val apkName = "sharkApk.apk"
    }

    constructor() {
        okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .sslSocketFactory(SSLContextSecurity.createSSLSocketFactory())
            .hostnameVerifier(object : HostnameVerifier{
                override fun verify(p0: String?, p1: SSLSession?): Boolean {
                    return true
                }

            })
            .build()
        downloadFile = FileUtils.createFile(apkPath, apkName)
    }

    @Synchronized
    fun startDown(url: String) {
        if (isDownLoading) {
            return
        }
        downloadUrl = url
        isDownLoading = true
        threadServer?.submit(runnable)
    }

    var runnable = object : Runnable {
        override fun run() {
            if(!(downloadFile?.exists()?:false)){
                downloadFile = FileUtils.createFile(apkPath, apkName)
            }
            //如果文件存在的话，得到文件的大小
            downloadLength = downloadFile?.length() ?: 0
            //得到下载内容的大小
            contentLength = getContentLength(downloadUrl)
            Log.d(TAG, "===" + downloadLength + "   contentLength=" + contentLength)
//            if (contentLength == 0L) {
//                isDownLoading = false
//                callBackOkHttp?.onFailure(null, IOException())
//                if(downloadLength>0L){
//                    downloadFile?.delete()
//                }
//                return
//            } else if (contentLength == downloadLength) {
//                isDownLoading = false
//                //已下载字节和文件总字节相等，说明已经下载完成了
//                callBackOkHttp?.onComplete(downloadFile?.absolutePath ?: "")
//                return
//            }
            request = Request.Builder()
                .url(downloadUrl)
//                .addHeader(
//                    "RANGE",
//                    "bytes=" + downloadLength + "-" + contentLength
//                )//断点续传要用到的，指示下载的区间
                .build()
            callHttpClient = okHttpClient?.newCall(request)
            callHttpClient?.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(TAG, "===" + "onFailure"+e.message)
                    isPaused = false
                    isCanceled = false
                    isDownLoading = false
                    callBackOkHttp?.onFailure(call, e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d(TAG, "===" + "onResponse")
                    var byteStream: InputStream? = null
                    var savedFile: RandomAccessFile? = null
                    try {
                        response?.let {
                            byteStream = it.body()?.byteStream()
                            savedFile = RandomAccessFile(downloadFile, "rw")
                            savedFile?.seek(downloadLength)//跳过已经下载的字节
                            var b = ByteArray(1024)
                            var total = 0
                            var len = 0
                            byteStream?.let {
                                while (true){
                                    len=  it.read(b)
                                    if(len==-1){//完成
                                        isDownLoading = false
                                        callBackOkHttp?.onComplete(downloadFile?.absolutePath ?: "")
                                        break
                                    }
                                    if (isCanceled) {
                                        isDownLoading = false
                                        callBackOkHttp?.onCancell()
                                        return
                                    } else if (isPaused) {
                                        isDownLoading = false
                                        callBackOkHttp?.onStop()
                                        return
                                    } else {
                                        total += len
                                        savedFile?.write(b, 0, len)
                                        //计算已经下载的百分比
                                        var progress = 0
                                        if (contentLength == 0L) {
                                            progress = 0
                                        } else {
                                            progress = (((total + downloadLength.toFloat()) * 100 )/ contentLength.toFloat()).toInt()
                                        }

                                        isDownLoading = false
                                        callBackOkHttp?.onProgress(progress)
                                    }

                                }
//                                while ((it.read(b).also { len = it } != -1)) {
//
//
//                                }



                            }

                            byteStream?.close()
                            return
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        isDownLoading = false
                        callBackOkHttp?.let {
                            it.onFailure(call, e)
                        }
                    } finally {
                        try {
                            byteStream?.close()
                            savedFile?.close()
                            if (isCanceled) {
                                downloadFile?.delete()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                }

            })
        }

    }
    fun update(url:String){
        var model= ModulefirmBean()
        model.sn= PhoneUtil.getSysteSN()
        model.ts= (Date().getTime()/1000).toString()
        var module= ModulesBean()
        var aplication= ApplicationBean()
        aplication.sn=PhoneUtil.getSysteSN()
        aplication.ver= ApplicationUtils.getVersionName(CommApplication.getInstance())?:""
        module.aplication=aplication
        model.modules=module
        var modelJson= Gson().toJson(model,ModulesBean::class.java)


        var  JSON = MediaType.parse("application/json; charset=utf-8")
        var body = RequestBody.create(JSON, modelJson)
        request=Request.Builder().url(url).post(body).build()
        callHttpClient = okHttpClient?.newCall(request)
        callHttpClient?.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG," onFailure "+e.message)

            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG," onResponse "+response.body())
                var re=response.body()
                if( response.code()==0){//请求成功
//                    startDown()

                }
            }

        })
    }

    /**
     * 得到下载内容的完整大小
     *
     * @param downloadUrl
     * @return
     */
    private fun getContentLength(downloadUrl: String): Long {
        val request = Request.Builder().url(downloadUrl)
            .addHeader("Accept-Encoding", "identity")
            .build()
        try {
            val response = okHttpClient?.newCall(request)?.execute()
            Log.d(
                TAG,
                "====" + response + "    " + response?.isSuccessful + "    " + response?.body()?.contentLength()
            )
            if (response?.isSuccessful ?: false) {
                val contentLength = response?.body()?.contentLength() ?: 0
                response?.body()?.close()
                return contentLength
            } else {
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return 0
    }

    private var callBackOkHttp: CallBackOkHttp? = null
    fun setCallBackOkHttps(callBackOkHttp: CallBackOkHttp) {
        this.callBackOkHttp = callBackOkHttp
    }

    //继续下载
    fun onRestart() {
        isPaused = false
        isCanceled = false
        isDownLoading = false
        startDown(downloadUrl)
    }

    //暂停下载
    fun pauseDownload() {
        isPaused = true
    }

    //取消下载
    fun cancelDownload() {
        isCanceled = true
        callHttpClient?.cancel()
    }

    //取消线程池的执行
    fun shutdown() {
        callHttpClient?.cancel()
        threadServer.shutdown()
    }

    //取消线程池的执行(包括正在执行的线程)
    fun shutdownNow() {
        callHttpClient?.cancel()
        threadServer.shutdownNow()
    }

    abstract class CallBackOkHttp : Callback {
        override fun onResponse(call: Call, response: Response) {
        }

        open fun onProgress(progress: Int) {}
        abstract fun onComplete(apkPath: String)
        open fun onCancell() {

        }

        open fun onStop() {

        }

    }
}