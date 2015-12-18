package com.makingiants.todayhistory.utils;

import java.util.Calendar;

public class DateManager {
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
}
