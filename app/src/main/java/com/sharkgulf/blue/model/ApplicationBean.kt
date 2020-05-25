package com.sharkgulf.common.model

import android.os.Parcel
import android.os.Parcelable

class ApplicationBean():Parcelable {
    var sn=""
    var ver=""
    constructor(parcel: Parcel) : this() {
        sn = parcel.readString()
        ver = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(sn)
        parcel.writeString(ver)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AcuBean> {
        override fun createFromParcel(parcel: Parcel): AcuBean {
            return AcuBean(parcel)
        }

        override fun newArray(size: Int): Array<AcuBean?> {
            return arrayOfNulls(size)
        }
    }
}