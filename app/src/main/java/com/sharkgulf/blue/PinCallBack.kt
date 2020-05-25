package com.sharkgulf.blue

import android.bluetooth.BluetoothDevice

/**
 * 配对回调
 */
interface PinCallBack {
    fun onBondRequest()//配对请求
    fun onBondFail(device: BluetoothDevice)//取消配对
    fun onBonding(device: BluetoothDevice)//配对中
    fun onBondSuccess(device: BluetoothDevice)//配对成功
}