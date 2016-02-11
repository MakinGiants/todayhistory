package com.makingiants.todayhistory.base

import android.app.ProgressDialog
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.Nullable
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.makingiants.today.api.error_handling.ApiException
import com.makingiants.todayhistory.R
import icepick.Icepick
import timber.log.Timber
import java.lang.ref.WeakReference

open class BaseActivityView : AppCompatActivity() {
    protected var mProgressWeakReference: WeakReference<ProgressDialog>? = null
    protected var mToolbar: Toolbar? = null

    //<editor-fold desc="Toolbar">
    protected fun activateToolbar(@StringRes title: Int): Toolbar? {
        activateToolbar()
        setTitle(title)
        return mToolbar
    }

    protected fun activateToolbar(): Toolbar? {
        if (mToolbar == null) {
            mToolbar = findViewById(R.id.toolbar) as Toolbar
            if (mToolbar != null) {
                setSupportActionBar(mToolbar)
            }
        }
        return mToolbar
    }
    //</editor-fold>

    //<editor-fold desc="Activity life cycle Overrides">

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val className = javaClass.getSimpleName()
        Timber.tag(className)
        Timber.d("(ActivityFlow) -> %s Created", className)
        Icepick.restoreInstanceState(this, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
    }

    @CallSuper
    override fun onResume() {
        val className = javaClass.getSimpleName()
        Timber.tag(className)
        Timber.d("(ActivityFlow) -> %s Resumed", className)
        super.onResume()
    }

    override fun onPause() {
        Timber.d("(ActivityFlow) == %s Paused", javaClass.getSimpleName())
        super.onPause()
    }

    override fun onStart() {
        Timber.d("(ActivityFlow) !! %s Started", javaClass.getSimpleName())
        super.onStart()
    }

    override fun onStop() {
        Timber.d("(ActivityFlow) :O %s Stopped", javaClass.getSimpleName())
        super.onStop()
    }

    override fun onDestroy() {
        Timber.d("(ActivityFlow) X( %s Destroyed", javaClass.getSimpleName())
        super.onDestroy()
    }

    //</editor-fold>

    //<editor-fold desc="Alert Dialogs">

    fun showToast(content: String) {
        Toast.makeText(this, content, Toast.LENGTH_LONG).show()
    }

    fun buildDialog(title: CharSequence, @Nullable message: CharSequence?): AlertDialog.Builder {
        val builder = AlertDialog.Builder(this).setTitle(title)

        if (message != null) {
            builder.setMessage(message)
        }

        return builder
    }
    //</editor-fold>

    //<editor-fold desc="Error Management">
    fun getErrorTitle(@Nullable title: String?): String {
        return title ?: getString(R.string.error_dialog_title)
    }

    fun getErrorMessage(@Nullable message: String?): String {
        return message ?: getString(R.string.error_dialog_message)
    }

    fun showError(throwable: Throwable) {
        var message: String? = null
        var title: String? = null
        if (throwable is ApiException) {
            message = throwable.message
            title = throwable.name
        } else {
            Timber.i("Error is not an ApiException.")
        }

        title = getErrorTitle(title)
        message = getErrorMessage(message)

        buildDialog(title, message).setPositiveButton(R.string.dialog_button_ok, null).show()
    }
    //</editor-fold>
}