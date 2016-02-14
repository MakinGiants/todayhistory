package com.makingiants.todayhistory.base

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
import timber.log.Timber

open class BaseActivityView : AppCompatActivity() {
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
    open fun showError(throwable: Throwable) {
        val message: String
        val title: String
        if (throwable is ApiException) {
            message = throwable.message ?: getString(R.string.error_dialog_message)
            title = throwable.name
        } else {
            title = getString(R.string.error_dialog_title)
            message = getString(R.string.error_dialog_message)
            Timber.i("Error is not an ApiException.")
        }

        buildDialog(title, message).setPositiveButton(R.string.dialog_button_ok, null).show()
    }
    //</editor-fold>
}
