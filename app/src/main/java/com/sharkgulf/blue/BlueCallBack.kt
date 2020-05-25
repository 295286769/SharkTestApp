package com.sharkgulf.blue

import android.bluetooth.BluetoothDevice
interface BlueCallBack {
    fun  onScanStarted()
    fun  onScanFinished()
    fun  onScanning(device:BluetoothDevice)
}