package com.sharkgulf.blue

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sharkgulf.blue.adapter.BlueToothAdapter
import com.sharkgulf.blue.adapter.RegisterUtil
import kotlinx.android.synthetic.main.activity_bluetooth_list.*

class BluetoothListActivity: AppCompatActivity() {
    var adapter: BlueToothAdapter?=null
    var list=ArrayList<BluetoothDevice>()
    var device: BluetoothDevice?=null
    /**
     * 扫描
     */
    var scanBlueReceiver=ScanBlueReceiver(object :BlueCallBack{
        override fun onScanStarted() {
        }

        override fun onScanFinished() {
        }

        override fun onScanning(device: BluetoothDevice) {
            this@BluetoothListActivity.device=device
            if(!list.contains(device)){
                list.add(device)
            }
            runOnUiThread {
                adapter?.notifyDataSetChanged()
            }

            Toast.makeText(this@BluetoothListActivity,"扫描成功", Toast.LENGTH_SHORT).show()

        }

    })
    /**
     * 配对
     */
    var pinBlueReceiver:PinBlueReceiver= PinBlueReceiver(object :PinCallBack{
        override fun onBondFail(device: BluetoothDevice) {
        }

        override fun onBonding(device: BluetoothDevice) {
        }

        override fun onBondSuccess(device: BluetoothDevice) {
            this@BluetoothListActivity.device=device
//            Toast.makeText(this@BluetoothListActivity,"配对成功", Toast.LENGTH_SHORT).show()
        }

        override fun onBondRequest() {
        }

    })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_list)
        RegisterUtil.registerScan(this,scanBlueReceiver)
        RegisterUtil.registerPinResult(this,pinBlueReceiver)

        adapter=BlueToothAdapter(this,list)
        var linea= LinearLayoutManager(this)
        recyclerView?.layoutManager=linea
        recyclerView?.adapter=adapter
        adapter?.setItemOnclickListener(object : BlueToothAdapter.ItemOnclickListener{
            override fun itemOnClick(view: View, device: BluetoothDevice, position: Int) {
                var intent=Intent(this@BluetoothListActivity,MainActivity::class.java)
                var bundle=Bundle()
                bundle.putParcelable("device",device)
                intent.putExtras(bundle)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }

        })
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(scanBlueReceiver)
        unregisterReceiver(pinBlueReceiver)
    }
}