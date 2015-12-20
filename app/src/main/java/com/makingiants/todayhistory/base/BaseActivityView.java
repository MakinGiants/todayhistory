package com.makingiants.todayhistory.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.makingiants.today.api.error_handling.ApiException;
import com.makingiants.todayhistory.R;
import icepick.Icepick;
import java.lang.ref.WeakReference;
import timber.log.Timber;

public class BaseActivityView extends AppCompatActivity {

  protected WeakReference<ProgressDialog> mProgressWeakReference;

  protected Toolbar mToolbar;

  //<editor-fold desc="Toolbar">
  protected Toolbar activateToolbar(@StringRes int title) {
    activateToolbar();
    setTitle(title);
    return mToolbar;
  }

  protected Toolbar activateToolbar() {
    if (mToolbar == null) {
      mToolbar = (Toolbar) findViewById(R.id.toolbar);
      if (mToolbar != null) {
        setSupportActionBar(mToolbar);
      }
    }
    return mToolbar;
  }
  //</editor-fold>

  //<editor-fold desc="Activity life cycle Overrides">

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String className = getClass().getSimpleName();
    Timber.tag(className);
    Timber.d("(ActivityFlow) -> %s Created", className);
    Icepick.restoreInstanceState(this, savedInstanceState);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Icepick.saveInstanceState(this, outState);
  }

  @Override
  public void setContentView(@LayoutRes int layout) {
    super.setContentView(layout);
    ButterKnife.bind(this);
  }

  @Override
  @CallSuper
  public void onResume() {
    String className = getClass().getSimpleName();
    Timber.tag(className);
    Timber.d("(ActivityFlow) -> %s Resumed", className);
    super.onResume();
  }

  @Override
  public void onPause() {
    Timber.d("(ActivityFlow) == %s Paused", getClass().getSimpleName());
    super.onPause();
  }

  @Override
  public void onStart() {
    Timber.d("(ActivityFlow) !! %s Started", getClass().getSimpleName());
    super.onStart();
  }

  @Override
  public void onStop() {
    Timber.d("(ActivityFlow) :O %s Stopped", getClass().getSimpleName());
    super.onStop();
  }

  @Override
  public void onDestroy() {
    Timber.d("(ActivityFlow) X( %s Destroyed", getClass().getSimpleName());
    super.onDestroy();
  }

  //</editor-fold>

  //<editor-fold desc="Alert Dialogs">

  public void showToast(String content) {
    Toast.makeText(this, content, Toast.LENGTH_LONG).show();
  }

  public AlertDialog.Builder buildDialog(CharSequence title, @Nullable CharSequence message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(title);

    if (message != null) {
      builder.setMessage(message);
    }

    return builder;
  }
  //</editor-fold>

  //<editor-fold desc="Error Management">
  public String getErrorTitle(@Nullable String title) {
    return title == null ? getString(R.string.error_dialog_title) : title;
  }

  public String getErrorMessage(@Nullable String message) {
    return message == null ? getString(R.string.error_dialog_message) : message;
  }

  public void showError(Throwable throwable) {
    String message = null, title = null;
    if (throwable instanceof ApiException) {
      ApiException apiException = (ApiException) throwable;
      message = apiException.getMessage();
      title = apiException.getName();
    } else {
      Timber.i("Error is not an ApiException.");
    }

    title = getErrorTitle(title);
    message = getErrorMessage(message);

    buildDialog(title, message).setPositiveButton(R.string.dialog_button_ok, null).show();
  }
  //</editor-fold>
}
