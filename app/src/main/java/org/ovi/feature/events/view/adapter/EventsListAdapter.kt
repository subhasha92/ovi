package org.ovi.feature.events.view.adapter

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ovi.R
import org.ovi.databinding.ItemEventListBinding
import org.ovi.feature.events.model.EventDetailsMode
import org.ovi.feature.home.model.RowsItem
import org.ovi.util.extensions.applyMatchWrapContent
import org.ovi.util.extensions.getFormatedValue
import org.ovi.util.extensions.inflater
import org.ovi.util.extensions.setImage
import kotlin.reflect.KFunction2

class EventsListAdapter(
    val callBack: KFunction2<EventDetailsMode, Int, Unit>,
    val eventMode: EventDetailsMode,
    private val eventList: MutableList<RowsItem?>
) : RecyclerView.Adapter<EventsListAdapter.VH>() {

    inner class VH(private val binding: ItemEventListBinding) :
        RecyclerView.ViewHolder(binding.root) {


        @SuppressLint("SimpleDateFormat")
        fun bind(item: RowsItem) {
            binding.tvTitle.text = item.name
            binding.tvSubTitle.text = item.description
            if (item.event_start_date != null) {
                binding.tvDate.text = item.event_start_date.toString().getFormatedValue("MMM")
                binding.tvDateValue.text = item.event_start_date.toString().getFormatedValue("dd")
            }
//            Log.d(TAG, "bind: ${item.image_url}")
            binding.ivEvents.setImage(item.image_url.toString(), R.drawable.tmp_event_image)
            binding.root.setOnClickListener {
                item.id?.toInt()?.let { it1 -> callBack.invoke(eventMode, it1) }
            }
        }

        init {
            binding.root.applyMatchWrapContent()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemEventListBinding.inflate(parent.inflater()))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        eventList[position]?.let { holder.bind(it) }
    }

    override fun getItemCount() = eventList.size
}