package com.sharkgulf.blue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScanBlueReceiver extends BroadcastReceiver {
    private final static String TAG="TestApp";
    private BlueCallBack blueCallBack;
    public ScanBlueReceiver(BlueCallBack blueCallBack){
        this.blueCallBack=blueCallBack;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action:" + action);
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        switch (action){
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                Log.d(TAG, "开始扫描...");
                blueCallBack.onScanStarted();
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                Log.d(TAG, "结束扫描...");
                blueCallBack.onScanFinished();
                break;
            case BluetoothDevice.ACTION_FOUND:
                Log.d(TAG, "发现设备...");
                blueCallBack.onScanning(device);
                break;
        }

    }
}
