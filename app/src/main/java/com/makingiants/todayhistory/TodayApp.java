package com.makingiants.todayhistory;

import android.app.Application;
import android.os.Build;
import com.makingiants.today.api.Api;
import com.makingiants.todayhistory.utils.log.CrashlyticsTree;
import com.makingiants.todayhistory.utils.log.DebugTree;
import java.util.LinkedHashMap;
import java.util.Map;
import timber.log.Timber;

public class TodayApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    Map<String, String> properties = getProperties();
    if (BuildConfig.ANALYTICS_ENABLED) {
      Timber.plant(new CrashlyticsTree(this, properties));
    } else {
      Timber.plant(new DebugTree());
    }

    Api.init(getApplicationContext(), BuildConfig.HOST);
    Api.setLogLevel(Api.LOG_LEVEL_FULL);
  }

  /**
   * Return a map with keys and values referencing environment variables
   */
  public static Map<String, String> getProperties() {
    return new LinkedHashMap<String, String>() {{
      put("Host", BuildConfig.HOST);
      put("BuildType", BuildConfig.BUILD_TYPE);
      put("Flavor", BuildConfig.FLAVOR);
      put("AppVersion", BuildConfig.VERSION_NAME);
      put("BuildNumber", String.valueOf(BuildConfig.VERSION_CODE));
      put("AndroidVersion", Build.VERSION.RELEASE);
      put("GitSHA", BuildConfig.GIT_SHA);
      put("Manufacturer", Build.MANUFACTURER);
      put("Model", Build.MODEL);
    }};
  }
}
