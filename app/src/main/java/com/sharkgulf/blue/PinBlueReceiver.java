package com.sharkgulf.blue;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Method;

public class PinBlueReceiver extends BroadcastReceiver {
    private final static String TAG="TestApp";
    private String pin = "0000";
    private PinCallBack pinCallBack;
    public PinBlueReceiver(PinCallBack pinCallBack){
        this.pinCallBack=pinCallBack;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action:" + action);
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)){
            try {
                pinCallBack.onBondRequest();
                //1.确认配对
//                ClsUtils.setPairingConfirmation(device.getClass(), device, true);
                Method setPairingConfirmation = device.getClass().getDeclaredMethod("setPairingConfirmation",boolean.class);
                setPairingConfirmation.invoke(device,true);
                //2.终止有序广播
                Log.d(TAG, "isOrderedBroadcast:"+isOrderedBroadcast()+",isInitialStickyBroadcast:"+isInitialStickyBroadcast());
                abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
                //3.调用setPin方法进行配对...
//                boolean ret = ClsUtils.setPin(device.getClass(), device, pin);
                Method removeBondMethod = device.getClass().getDeclaredMethod("setPin", new Class[]{byte[].class});
                Boolean returnValue = (Boolean) removeBondMethod.invoke(device, new Object[]{pin.getBytes()});
                Log.d(TAG, "returnValue:"+returnValue);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            switch (device.getBondState()) {
                case BluetoothDevice.BOND_NONE:
                    Log.d(TAG, "取消配对");
                    pinCallBack.onBondFail(device);
                    break;
                case BluetoothDevice.BOND_BONDING:
                    Log.d(TAG, "配对中");
                    pinCallBack.onBonding(device);
                    break;
                case BluetoothDevice.BOND_BONDED:
                    Log.d(TAG, "配对成功");
                    pinCallBack.onBondSuccess(device);
                    break;
            }
        }

    }
}
