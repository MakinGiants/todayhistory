package com.makingiants.todayhistory.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import timber.log.Timber;

public class NetworkChecker {

  private final ConnectivityManager mConnectivityManager;

  public NetworkChecker(Context context) {
    mConnectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  /**
   * https://gist.github.com/A7maDev/427694dcae675435ce53
   */
  public boolean isNetworkConnectionAvailable() {
    try {
      NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
      return networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable();
    } catch (Exception e) {
      Timber.e(e, "Getting internet connection status");
    }

    return false;
  }
}
