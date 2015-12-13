package todayhistory;

import com.makingiants.today.api.repository.history.pojo.Event;
import java.util.List;

public interface TodayView {
  void showEvents(List<Event> events);

  void showProgress();

  void dismissProgress();

  void showError(String message, String title);
}
