package com.sharkgulf.common.model

import android.os.Parcel
import android.os.Parcelable

/**
 * 版本升级提交组件类信息
 */
class ModulesBean() :Parcelable{
    var bike:BikeBean?=null
    var aplication:ApplicationBean?=null
    var ccu:CcuBean?=null
    var bms:BmsBean?=null
    var acu:AcuBean?=null
    var dcu:DcuBean?=null
    var mtcu:MtcuBean?=null
    var lcu:LcuBean?=null

    constructor(parcel: Parcel) : this() {
        bike = parcel.readParcelable(BikeBean::class.java.classLoader)
        aplication = parcel.readParcelable(ApplicationBean::class.java.classLoader)
        acu = parcel.readParcelable(AcuBean::class.java.classLoader)
        dcu = parcel.readParcelable(DcuBean::class.java.classLoader)
        mtcu = parcel.readParcelable(MtcuBean::class.java.classLoader)
        lcu = parcel.readParcelable(LcuBean::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(bike, flags)
        parcel.writeParcelable(aplication, flags)
        parcel.writeParcelable(acu, flags)
        parcel.writeParcelable(dcu, flags)
        parcel.writeParcelable(mtcu, flags)
        parcel.writeParcelable(lcu, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModulesBean> {
        override fun createFromParcel(parcel: Parcel): ModulesBean {
            return ModulesBean(parcel)
        }

        override fun newArray(size: Int): Array<ModulesBean?> {
            return arrayOfNulls(size)
        }
    }
}