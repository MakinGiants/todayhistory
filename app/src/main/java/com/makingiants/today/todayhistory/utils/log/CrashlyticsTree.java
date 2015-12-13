package com.makingiants.today.todayhistory.utils.log;

import android.content.Context;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.core.CrashlyticsCore;
import com.makingiants.today.todayhistory.BuildConfig;
import io.fabric.sdk.android.Fabric;
import java.util.Map;
import timber.log.Timber;

public class CrashlyticsTree extends Timber.Tree {
  public CrashlyticsTree(Context context, Map<String, String> properties) {
    Fabric.with(context, new Crashlytics(), new Answers());

    CrashlyticsCore core = Crashlytics.getInstance().core;

    for (Map.Entry<String, String> entry : properties.entrySet()) {
      core.setString(entry.getKey(), entry.getValue());
    }
  }

  @Override
  protected void log(int priority, String tag, String message, Throwable t) {
    if (priority == Log.VERBOSE && !"release".equals(BuildConfig.BUILD_TYPE)) {
      //Dont log verbose messages and avoid not release builds to send crash to crashlytics
      return;
    }

    CrashlyticsCore core = Crashlytics.getInstance().core;
    core.log(Log.ERROR, tag, message);

    if (t != null && priority == Log.ERROR) {
      core.logException(t);
    }
  }
}
