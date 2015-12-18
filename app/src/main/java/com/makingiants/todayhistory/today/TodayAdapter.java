package com.makingiants.todayhistory.today;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.makingiants.today.api.repository.history.pojo.Event;
import com.makingiants.todayhistory.R;
import java.util.List;

public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.SongViewHolder> {
  private Event[] mEvents;

  @Override
  public SongViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(container.getContext());
    View root = inflater.inflate(R.layout.today_item, container, false);
    return new SongViewHolder(root);
  }

  @Override
  public void onBindViewHolder(SongViewHolder holder, int position) {
    Event event = mEvents[position];

    holder.titleTextView.setText(event.getTitle());
    holder.dateTextView.setText(event.getDate());
  }

  public void setEvents(List<Event> events) {
    mEvents = events.toArray(new Event[events.size()]);
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return mEvents == null ? 0 : mEvents.length;
  }

  public static class SongViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.item_today_text_title) TextView dateTextView;
    @Bind(R.id.item_today_text_date) TextView titleTextView;

    SongViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
