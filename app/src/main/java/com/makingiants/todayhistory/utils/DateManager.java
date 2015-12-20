package com.makingiants.todayhistory.utils;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Calendar;

public class DateManager implements Parcelable {
  Calendar mCalendar;

  public DateManager() {
    mCalendar = Calendar.getInstance();
  }

  public int getTodayDay() {
    return mCalendar.get(Calendar.DAY_OF_MONTH);
  }

  public int getTodayMonth() {
    Calendar cal = Calendar.getInstance();
    return mCalendar.get(Calendar.MONTH);
  }

  //<editor-fold desc="Parcelable">
  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeSerializable(this.mCalendar);
  }

  protected DateManager(Parcel in) {
    this.mCalendar = (Calendar) in.readSerializable();
  }

  public static final Parcelable.Creator<DateManager> CREATOR =
      new Parcelable.Creator<DateManager>() {
        public DateManager createFromParcel(Parcel source) {
          return new DateManager(source);
        }

        public DateManager[] newArray(int size) {
          return new DateManager[size];
        }
      };
  //</editor-fold>
}
