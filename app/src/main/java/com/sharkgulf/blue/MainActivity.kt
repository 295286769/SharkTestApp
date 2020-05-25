package com.sharkgulf.blue

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import androidx.core.content.FileProvider
import com.sharkgulf.blue.utils.*
import com.sharkgulf.checkandinstallapk.utils.StartActivityUtil
import okhttp3.Call
import java.io.File
import java.io.IOException


class MainActivity : BaseActivity() {
    private val TAG = "TestApp"
    var mBluetoothAdapter: BluetoothAdapter? = null
    var openRequsCode = 1000
    var closeRequsCode = 1001
    var scanRequsCode = 1002
    var device: BluetoothDevice? = null
    var downApkServer:ApkDowmManager?=null
//    var dialoghuider: DialogUtil?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDownApkServer()
        getBluetooth()

        btn_china.setOnClickListener {
            var intent = Intent(this, SeconActivity::class.java)
            startActivity(intent)
        }
        btn_english.setOnClickListener {
            btn_china.setText(AssetsUtils.readAssetsFile(this,"test"))
        }
        btn_down.setOnClickListener {
            downApkServer?.startDown(ApkDowmManager.testUrl)
//            dialoghuider?.showDialog()
        }
        btn_pause.setOnClickListener {
            downApkServer?.pauseDownload()
        }
        btn_jixu.setOnClickListener {
            downApkServer?.onRestart()
        }
        btn_delet.setOnClickListener {
            var file=FileUtils.createFile(ApkDowmManager.apkPath, ApkDowmManager.apkName)
            if(file?.exists()?:false){
                file?.delete()
            }
        }
        btn_zip.setOnClickListener {
            var zipPath=FileUtils.createSDCardFile(FileUtils.ZIPPATH,FileUtils.SHARKZIPNAME)?.absolutePath?:""
            if(!TextUtils.isEmpty(zipPath)){
                RecoveryUpdateUtil.updateSystem(this,zipPath)
            }

        }
//        btn_open.setOnClickListener {//扫描蓝牙
//            getPermission()
//        }
//        btn_close.setOnClickListener {//关闭蓝牙
//            cancelScanBule()
//        }
//        btn_startpair.setOnClickListener {//开始配对
//            device?.let {
//                pin(it)
//            }
//
//        }
//        btn_startpair.setOnClickListener {//取消配对
//            device?.let {
//                cancelPinBule(it)
//            }
//
//        }
        Log.d(TAG,"SysteSN="+getSysteSN())
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
    fun showDialog(){

    }
    fun initDownApkServer(){
        FileUtils.createSDCardFile(FileUtils.ZIPPATH,FileUtils.SHARKZIPNAME)
        downApkServer=ApkDowmManager()
        downApkServer?.setCallBackOkHttps(object : ApkDowmManager.CallBackOkHttp(){
            override fun onProgress(progress: Int) {
                runOnUiThread {
                    btn_press.setText(""+progress)
                }
            }

            override fun onComplete(apkPath: String) {
                Log.d(TAG,"onComplete="+apkPath)
                StartActivityUtil.installAPK(this@MainActivity,apkPath)
            }

            override fun onFailure(call: Call, e: IOException) {
            }

        })
//        dialoghuider= DialogUtil.Companion.DialogBuider(this).run {
//            setTitle("更新提示")
//            setContentText("您有新版本是否更新")
//            setLeftBtnText("取消",object : DialogButtonLeftInterface(){
//
//                override fun onComfireClick() {
//                    dialoghuider?.dissDialog()
//                }
//            })
//            setRightBtnText("确定",object: DialogButtonRightInterface(){
//                override fun onComfireClick() {
//                    downApkServer?.startDown(ApkDowmManager.testUrl)
//                }
//
//            })
//            buider()
//        }
    }

    fun getBluetooth(): BluetoothAdapter? {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return mBluetoothAdapter
    }

    /**
     * 设备是否支持蓝牙  true为支持
     * @return
     */
    fun isSupportBlue(): Boolean {
        return mBluetoothAdapter != null
    }

    /**
     * 蓝牙是否打开   true为打开
     * @return
     */
    fun isBlueEnable(): Boolean {
        return isSupportBlue() && mBluetoothAdapter?.isEnabled() ?: false
    }

    /**
     * 自动打开蓝牙（异步：蓝牙不会立刻就处于开启状态）
     * 这个方法打开蓝牙不会弹出提示
     */
    fun openBlueAsyn() {
        if (isSupportBlue() || !isBlueEnable()) {
            var bo = mBluetoothAdapter?.enable()
            Toast.makeText(this, "蓝牙已经打开" + bo, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 关闭蓝牙
     */
    fun clodeBlueAsyn() {
        if (isSupportBlue()) {
            var bo = mBluetoothAdapter?.disable()
            Toast.makeText(this, "蓝牙已经关闭" + bo, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 自动打开蓝牙（同步）
     * 这个方法打开蓝牙会弹出提示
     * 需要在onActivityResult 方法中判断resultCode == RESULT_OK  true为成功
     */
    fun openBlueSync() {
        if (!isSupportBlue() || isBlueEnable()) {
            Toast.makeText(this, "蓝牙已经打开", Toast.LENGTH_SHORT).show()
            return
        }
        var intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, openRequsCode);
    }

    /**
     * 自动打开蓝牙（同步）
     * 这个方法打开蓝牙会弹出提示
     * 需要在onActivityResult 方法中判断resultCode == RESULT_OK  true为成功
     */
    fun closeBlueSync() {
        if (!isSupportBlue() || !isBlueEnable()) {
            Toast.makeText(this, "蓝牙已经关闭", Toast.LENGTH_SHORT).show()
            return
        }
        var intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        startActivityForResult(intent, closeRequsCode)
    }

    /**
     * 扫描的方法 返回true 扫描成功
     * 通过接收广播获取扫描到的设备
     * @return
     */
    fun scanBlue(): Boolean {
        if (!isBlueEnable()) {
            return false
        }

        //当前是否在扫描，如果是就取消当前的扫描，重新扫描
        if (mBluetoothAdapter?.isDiscovering() ?: false) {
            mBluetoothAdapter?.cancelDiscovery()
        }

        //此方法是个异步操作，一般搜索12秒
        return mBluetoothAdapter?.startDiscovery() ?: false
    }

    /**
     * 取消扫描蓝牙
     * @return  true 为取消成功
     */
    fun cancelScanBule(): Boolean {
        if (isSupportBlue()) {
            return mBluetoothAdapter?.cancelDiscovery() ?: false
        }
        return true
    }

    /**
     * 配对（配对成功与失败通过广播返回）
     * @param device
     */
    fun pin(device: BluetoothDevice?): Boolean {
        if (device == null) {
            Log.e(TAG, "bond device null");
            return false;
        }
        if (!isBlueEnable()) {
            Log.e(TAG, "Bluetooth not enable!");
            return false;
        }
        //配对之前把扫描关闭
        if (mBluetoothAdapter?.isDiscovering() ?: false) {
            mBluetoothAdapter?.cancelDiscovery();
        }
        //判断设备是否配对，没有配对在配，配对了就不需要配了
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            Log.d(TAG, "attemp to bond:" + device.getName());
            try {
                var createBondMethod = device.javaClass.getMethod("createBond")
                var returnValue = createBondMethod.invoke(device) as Boolean
                return returnValue
            } catch (e: Exception) {
                e.printStackTrace();
                Log.e(TAG, "attemp to bond fail!");
            }
        }
        return false
    }

    /**
     * 取消配对（取消配对成功与失败通过广播返回 也就是配对失败）
     * @param device
     */
    fun cancelPinBule(device: BluetoothDevice) {
        if (device == null) {
            Log.d(TAG, "cancel bond device null");
            return;
        }
        if (!isBlueEnable()) {
            Log.e(TAG, "Bluetooth not enable!");
            return;
        }
        //判断设备是否配对，没有配对就不用取消了
        if (device.getBondState() != BluetoothDevice.BOND_NONE) {
            Log.d(TAG, "attemp to cancel bond:" + device.getName());
            try {
                var removeBondMethod = device.javaClass.getMethod("removeBond");
                var returnValue = removeBondMethod.invoke(device) as Boolean
            } catch (e: Exception) {
                e.printStackTrace();
                Log.e(TAG, "attemp to cancel bond fail!");
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {//成功
            if (requestCode == openRequsCode) {//打开成功
                Toast.makeText(this, "打开成功", Toast.LENGTH_SHORT).show()

            } else if (requestCode == closeRequsCode) {//关闭成功
                Toast.makeText(this, "关闭成功", Toast.LENGTH_SHORT).show()
            } else if (requestCode == scanRequsCode) {
                device = data?.getParcelableExtra<BluetoothDevice>("device")
                pin(device)
                Log.i(TAG, "device" + device);
            }

        }
    }

    /**
     * 解决：无法发现蓝牙设备的问题
     *
     * 对于发现新设备这个功能, 还需另外两个权限(Android M 以上版本需要显式获取授权,附授权代码):
     */
    val ACCESS_LOCATION = 1;

    @SuppressLint("WrongConstant")
    fun getPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            var permissionCheck = 0;
            permissionCheck = this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            permissionCheck += this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                //未获得权限
                this.requestPermissions( // 请求授权
                    arrayOf<String>(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    ACCESS_LOCATION
                );// 自定义常量,任意整型
            } else {
                scanBlue()
                var intent = Intent(this@MainActivity, BluetoothListActivity::class.java)
                startActivityForResult(intent, scanRequsCode)
            }
        }
    }

    /**
     * 请求权限的结果回调。每次调用 requestpermissions（string[]，int）时都会调用此方法。
     * @param requestCode 传入的请求代码
     * @param permissions 传入permissions的要求
     * @param grantResults 相应权限的授予结果:PERMISSION_GRANTED 或 PERMISSION_DENIED
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACCESS_LOCATION -> {
                if (hasAllPermissionGranted(grantResults)) {
                    scanBlue()
                    var intent = Intent(this@MainActivity, BluetoothListActivity::class.java)
                    startActivityForResult(intent, scanRequsCode)
                    Log.i(TAG, "onRequestPermissionsResult: 用户允许权限");
                } else {
                    Log.i(TAG, "onRequestPermissionsResult: 拒绝搜索设备权限");
                }
            }

        }
    }

    fun hasAllPermissionGranted(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    override fun onDestroy() {
        super.onDestroy()
    }
//    var longTime =0L
//    private var shortPress = false
//    var isUp=false
//    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
//        Log.d("onKey","onKeyLongPress"+keyCode)
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//            shortPress = false
//            Toast.makeText(this, "longPress", Toast.LENGTH_LONG).show();
//            return true
//        }
//        return super.onKeyLongPress(keyCode, event)
//    }
//
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        event?.startTracking()
//        Log.d("onKey","onKeyDown"+keyCode+"action="+event?.getAction())
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ) {
//            if(event?.action==KeyEvent.ACTION_DOWN){
//                Log.d("onKey","onKeyDown getRepeatCount"+event?.getRepeatCount())
//                if (event?.getRepeatCount() == 0) {
//                    event?.startTracking()
//                    longTime = System.currentTimeMillis()
//                    shortPress = true
//                }
//                return true;
//        }
//        }
//        return true
//    }
//
//    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
//        Log.d("onKey","onKeyUp"+keyCode)
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//            //毫秒数可以自己指定
//            if (System.currentTimeMillis() - longTime < 100){
//                Toast.makeText(this, "key抬起", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//            else {
//                Toast.makeText(this, "key终于", Toast.LENGTH_SHORT).show();
//            }
////            if (shortPress) {
////                Toast.makeText(this, "shortPress", Toast.LENGTH_LONG).show();
////            } else {
////                //Don't handle longpress here, because the user will have to get his finger back up first
////            }
//            shortPress = false;
//            return true;
//        }
//        return super.onKeyUp(keyCode, event)
//    }
}
