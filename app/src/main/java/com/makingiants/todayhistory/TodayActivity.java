package com.makingiants.todayhistory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.makingiants.today.api.repository.history.HistoryRepository;
import com.makingiants.today.api.repository.history.pojo.Event;
import com.makingiants.todayhistory.utils.Transformer;

public class TodayActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_today);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    HistoryRepository historyRepository = new HistoryRepository();
    historyRepository.get(10, 12)
        .compose(Transformer.applyIoSchedulers())
        .subscribe(events -> {
          for (Event event : events) {
            Log.d("TodayActivity", event.getTitle());
          }
        }, error -> Log.d("TodayActivity", error.getLocalizedMessage()));
  }
}
