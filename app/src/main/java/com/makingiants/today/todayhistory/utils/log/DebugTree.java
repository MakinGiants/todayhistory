package com.makingiants.today.todayhistory.utils.log;

import timber.log.Timber;

public class DebugTree extends Timber.DebugTree {

  @Override
  protected String createStackElementTag(StackTraceElement element) {
    // Print line number too
    return super.createStackElementTag(element) + ":" + element.getLineNumber();
  }
}
