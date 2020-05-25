package com.sharkgulf.common.model

import android.os.Parcel
import android.os.Parcelable

/**
 * 版本升级提交类信息
 */
class ModulefirmBean() :Parcelable{
    var sn=""
    var ts=""
    var modules:ModulesBean?=null

    constructor(parcel: Parcel) : this() {
        sn = parcel.readString()
        ts = parcel.readString()
        modules = parcel.readParcelable(ModulesBean::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(sn)
        parcel.writeString(ts)
        parcel.writeParcelable(modules, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModulefirmBean> {
        override fun createFromParcel(parcel: Parcel): ModulefirmBean {
            return ModulefirmBean(parcel)
        }

        override fun newArray(size: Int): Array<ModulefirmBean?> {
            return arrayOfNulls(size)
        }
    }

}