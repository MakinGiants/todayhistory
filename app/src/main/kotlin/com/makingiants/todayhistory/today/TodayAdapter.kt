package com.makingiants.todayhistory.today

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makingiants.today.api.repository.history.pojo.Event
import com.makingiants.todayhistory.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.today_item.view.*

class TodayAdapter(private val mPicasso: Picasso) : RecyclerView.Adapter<TodayAdapter.SongViewHolder>() {
    private var mEvents: Array<Event>? = null

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): SongViewHolder {
        val inflater = LayoutInflater.from(container.context)
        val root = inflater.inflate(R.layout.today_item, container, false)
        return SongViewHolder(root)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val event = mEvents!![position]


        holder.itemView.titleTextView.text = event.title
        holder.itemView.dateTextView.text = event.date

        mPicasso.load(event.imageUrl).fit().into(holder.itemView.image)

        //mPicasso.load(event.getImageUrl())
        //    .fit()
        //    .into(holder.headerImage, PicassoPalette.with(event.getImageUrl(), holder.headerImage)
        //        .use(PicassoPalette.Profile.VIBRANT)
        //        .intoBackground(holder.textLinearLayout)
        //        .intoTextColor(holder.dateTextView, PicassoPalette.Swatch.BODY_TEXT_COLOR)
        //        .intoTextColor(holder.titleTextView, PicassoPalette.Swatch.TITLE_TEXT_COLOR));
    }

    fun setEvents(events: List<Event>) {
        mEvents = events.toTypedArray()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = if (mEvents == null) 0 else mEvents!!.size

    class SongViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
