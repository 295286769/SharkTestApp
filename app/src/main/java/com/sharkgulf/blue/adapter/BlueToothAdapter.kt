package com.sharkgulf.blue.adapter

import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.sharkgulf.blue.R

class BlueToothAdapter : RecyclerView.Adapter<BlueToothAdapter.MyViewHolder> {

    var data: ArrayList<BluetoothDevice>? = null
    private var context: Context? = null

    constructor(context: Context?, data: ArrayList<BluetoothDevice>?) : super() {
        this.context = context
        this.data = data
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BlueToothAdapter.MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.bluetooth_adapter,
                null,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data?.size?:0
    }

    override fun onBindViewHolder(holder: BlueToothAdapter.MyViewHolder, position: Int) {
        var value: BluetoothDevice? = data?.get(position)
        value?.let {
            holder.btn_pair?.setText(it.name)

            holder.btn_pair?.setOnClickListener {
                itemOnclickListener?.itemOnClick(it, value, position)
            }

        }

    }

     inner class MyViewHolder : RecyclerView.ViewHolder {
         var btn_pair: Button?=null
    constructor(view: View) : super(view) {
        btn_pair=view.findViewById(R.id.btn_pair)
    }

}

private var itemOnclickListener: ItemOnclickListener? = null
fun setItemOnclickListener(itemOnclickListener: ItemOnclickListener?) {
    this.itemOnclickListener = itemOnclickListener
}

interface ItemOnclickListener {
    fun itemOnClick(view: View, device: BluetoothDevice, position: Int)
}
}