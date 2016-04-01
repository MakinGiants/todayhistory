package com.makingiants.todayhistory.today

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

        mPicasso.load(event.imageUrl)
                .placeholder(R.color.material_grey_600)
                .fit()
                .centerInside()
                .into(holder.itemView.image,
                        PicassoPalette.with(event.imageUrl, holder.itemView.image)
                                .use(BitmapPalette.Profile.MUTED_DARK)
                                .intoBackground(holder.itemView.image))

    }

    fun setEvents(events: List<Event>) {
        mEvents = events.toTypedArray()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mEvents?.size ?: 0

    class SongViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
