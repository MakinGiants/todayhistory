package todayhistory;

import java.util.Calendar;

public class DateManager {

  public int getTodayDay() {
    Calendar cal = Calendar.getInstance();
    return cal.get(Calendar.DAY_OF_MONTH);
  }

  public int getTodayMonth() {
    Calendar cal = Calendar.getInstance();
    return cal.get(Calendar.MONTH);
  }
}
