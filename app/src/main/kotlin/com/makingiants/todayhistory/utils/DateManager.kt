package com.makingiants.todayhistory.utils

import android.os.Parcel
import android.os.Parcelable
import java.util.*

open class DateManager : Parcelable {
    internal var mCalendar: Calendar

    constructor() {
        mCalendar = Calendar.getInstance()
    }

    open fun getTodayDay(): Int = mCalendar.get(Calendar.DAY_OF_MONTH)
    open fun getTodayMonth(): Int = mCalendar.get(Calendar.MONTH)

    //<editor-fold desc="Parcelable">
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeSerializable(this.mCalendar)
    }

    protected constructor(`in`: Parcel) {
        this.mCalendar = `in`.readSerializable() as Calendar
    }

    companion object {
        val CREATOR: Parcelable.Creator<DateManager> = object : Parcelable.Creator<DateManager> {
            override fun createFromParcel(source: Parcel): DateManager {
                return com.makingiants.todayhistory.utils.DateManager(source)
            }

            override fun newArray(size: Int): Array<out DateManager?> = arrayOfNulls(size)

        }
    }
    //</editor-fold>
}
