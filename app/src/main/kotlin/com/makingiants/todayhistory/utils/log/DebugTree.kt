package com.makingiants.todayhistory.utils.log

import timber.log.Timber

class DebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String {
        // Print line number too
        return super.createStackElementTag(element) + ":" + element.lineNumber
    }
}
