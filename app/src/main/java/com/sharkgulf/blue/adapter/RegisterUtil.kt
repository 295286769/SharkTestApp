package com.sharkgulf.blue.adapter

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import com.sharkgulf.blue.PinBlueReceiver
import com.sharkgulf.blue.ScanBlueReceiver

class RegisterUtil {
    companion object{
        fun registerPinResult(activity:Activity,pinBlueReceiver:PinBlueReceiver){
            var filter4 =  IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
            var filter5 =  IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            activity.registerReceiver(pinBlueReceiver,filter4);
            activity.registerReceiver(pinBlueReceiver,filter5);
        }
        fun registerScan(activity:Activity,scanBlueReceiver:ScanBlueReceiver){
            var filter1 =  IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            var filter2 =  IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            var filter3 =  IntentFilter(BluetoothDevice.ACTION_FOUND);
            activity.registerReceiver(scanBlueReceiver,filter1);
            activity.registerReceiver(scanBlueReceiver,filter2);
            activity.registerReceiver(scanBlueReceiver,filter3);
        }
    }
}