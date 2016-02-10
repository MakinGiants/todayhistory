package com.makingiants.todayhistory.utils

import android.content.Context
import android.net.ConnectivityManager
import timber.log.Timber

interface NetworkChecker {
    fun isNetworkConnectionAvailable(): Boolean
}

open class NetworkCheckerImpl(context: Context) : NetworkChecker {

    private val mConnectivityManager: ConnectivityManager

    init {
        mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    /**
     * https://gist.github.com/A7maDev/427694dcae675435ce53
     */
    override fun isNetworkConnectionAvailable(): Boolean {
        try {
            val networkInfo = mConnectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected && networkInfo.isAvailable
        } catch (e: Exception) {
            Timber.e(e, "Getting internet connection status")
        }

        return false
    }
}
