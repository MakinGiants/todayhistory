package com.makingiants.todayhistory.utils.base

import android.support.annotation.Nullable
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.makingiants.today.api.error_handling.ApiError
import com.makingiants.todayhistory.R

open class BaseActivityView : AppCompatActivity() {
  protected var mToolbar: Toolbar? = null

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

  open fun showError(apiError: ApiError) =
      buildDialog(apiError.getTitle(this), apiError.getMessage(this))
          .setPositiveButton(R.string.dialog_button_ok, null)
          .show()

}
