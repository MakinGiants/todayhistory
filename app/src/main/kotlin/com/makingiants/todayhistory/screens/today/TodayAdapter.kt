package com.makingiants.todayhistory.screens.today

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.florent37.picassopalette.BitmapPalette
import com.github.florent37.picassopalette.PicassoPalette
import com.makingiants.today.api.repository.history.pojo.Event
import com.makingiants.todayhistory.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.today_item.view.*

class TodayAdapter(val picasso: Picasso) : RecyclerView.Adapter<TodayAdapter.SongViewHolder>() {
  private var events: Array<Event>? = null

  override fun onCreateViewHolder(container: ViewGroup, viewType: Int): SongViewHolder {
    val inflater = LayoutInflater.from(container.context)
    val root = inflater.inflate(R.layout.today_item, container, false)
    return SongViewHolder(root)
  }

  override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
    val event = events!![position]

    holder.itemView.titleTextView.text = event.title
    holder.itemView.dateTextView.text = event.date

    event.thumb?.src?.let {
      picasso.load(it)
          .placeholder(R.color.material_grey_600)
          .fit()
          .centerInside()
          .into(holder.itemView.image,
              PicassoPalette.with(it, holder.itemView.image)
                  .use(BitmapPalette.Profile.MUTED_DARK)
                  .intoBackground(holder.itemView.image))
    }
  }

  fun setEvents(events: List<Event>) {
    this.events = events.toTypedArray()
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int = events?.size ?: 0

  class SongViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)

}
