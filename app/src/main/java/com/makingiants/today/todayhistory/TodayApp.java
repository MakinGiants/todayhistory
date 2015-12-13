package com.makingiants.today.todayhistory;

import android.app.Application;
import com.makingiants.today.api.Api;

public class TodayApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    Api.init(getApplicationContext(), "http://day-in-history.herokuapp.com");
    Api.setLogLevel(Api.LOG_LEVEL_FULL);
  }
}
