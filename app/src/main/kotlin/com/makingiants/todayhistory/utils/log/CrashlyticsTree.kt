package com.makingiants.todayhistory.utils.log

import android.content.Context
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.core.CrashlyticsCore
import com.makingiants.todayhistory.BuildConfig
import io.fabric.sdk.android.Fabric
import timber.log.Timber

class CrashlyticsTree(context: Context, properties: Map<String, String>) : Timber.Tree() {
    init {
        Fabric.with(context, Crashlytics(), Answers())

        val core = Crashlytics.getInstance().core

        for (entry in properties.entries) {
            core.setString(entry.key, entry.value)
        }
    }

    override fun log(priority: Int, tag: String, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE && "release" != BuildConfig.BUILD_TYPE) {
            //Dont log verbose messages and avoid not release builds to send crash to crashlytics
            return
        }

        val core = Crashlytics.getInstance().core
        core.log(Log.ERROR, tag, message)

        if (t != null && priority == Log.ERROR) {
            core.logException(t)
        }
    }
}
