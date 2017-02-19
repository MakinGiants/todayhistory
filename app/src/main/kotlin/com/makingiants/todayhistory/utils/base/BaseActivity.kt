package com.makingiants.todayhistory.utils.base

import android.support.annotation.Nullable
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.makingiants.today.api.error_handling.ApiException
import com.makingiants.todayhistory.*
import timber.log.Timber

open class BaseActivity : AppCompatActivity() {

  val activityComponent: MyActivityComponent by lazy {
    DaggerMyActivityComponent.builder()
        .myApplicationComponent((application as TodayApp).applicationComponent)
        .myActivityModule(MyActivityModule(this))
        .build()
  }

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
