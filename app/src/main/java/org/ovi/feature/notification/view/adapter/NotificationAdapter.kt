package org.ovi.feature.notification.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.format.DateFormat
import android.text.util.Linkify
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ovi.R
import org.ovi.databinding.ItemNotificationBinding
import org.ovi.feature.notification.model.RowsItem
import org.ovi.util.extensions.applyMatchWrapContent
import org.ovi.util.extensions.getOviColor
import org.ovi.util.extensions.inflater
import org.ovi.util.extensions.setImage
import java.text.SimpleDateFormat
import java.util.*


class NotificationAdapter(private val list: List<RowsItem>, val callback: (Int) -> Unit) :
    RecyclerView.Adapter<NotificationAdapter.VH>() {

    inner class VH(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.applyMatchWrapContent()
        }

        fun bind(data: RowsItem) {
            if (data.is_read == false) {
                binding.root.setBackgroundColor(binding.root.context.getOviColor(R.color.extra_light_brown))
            }
            binding.tvTitle.text = data.subject
//            binding.tvSubTitle.movementMethod=LinkMovementMethod.getInstance()

//            binding.tvSubTitle.text="hello world how are you are you oasdfasdf asdfasdf asdfasdf asdfasdf asdfasdf www.google.com asdfasdfasdfasdf"
            binding.tvSubTitle.text = data.body
            Linkify.addLinks(binding.tvSubTitle, Linkify.WEB_URLS)
//
            if (data.createdAt != null)
                binding.tvDate.text = getDate(binding.tvDate.context, data.createdAt)

            binding.ivImageUser.setImage(data.media?.get(0), R.drawable.tmp_event_image)

            itemView.setOnClickListener {
                if (data.is_read == false) {
                    data.is_read = true
                    list[absoluteAdapterPosition].is_read = true
                    callback.invoke(data.id!!)
                    notifyItemChanged(absoluteAdapterPosition)
                }
                if (data.destination != null && data.destination != "") {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data.destination))
                    itemView.context.startActivity(
                        browserIntent
                    )
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDate(context: Context?, createdAt: String?): CharSequence? {
        val format = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US
        )
        format.timeZone = TimeZone.getTimeZone("UTC")
        val dt = format.parse(createdAt.toString())
        return getFormattedDate(context, dt!!)
    }

    private fun getFormattedDate(context: Context?, date: Date): String? {
//        Log.e(TAG, "getFormattedDate: $date")
        val smsTime = Calendar.getInstance()
        smsTime.time = date
//        Log.e(TAG, "getFormattedDate: Sms Time:${smsTime.time}")
        val now = Calendar.getInstance()
        val timeFormatString = "h:mm aa"
        val dateTimeFormatString = "MMM-dd-yyyy"
        val HOURS = (60 * 60 * 60).toLong()
        return if (now[Calendar.DATE] == smsTime[Calendar.DATE]) {
            "Today "
        } else if (now[Calendar.DATE] - smsTime[Calendar.DATE] == 1) {
            "Yesterday "
        } else if (now[Calendar.YEAR] == smsTime[Calendar.YEAR]) {
            DateFormat.format(dateTimeFormatString, smsTime).toString()
        } else {
            DateFormat.format("MMM-dd-yyyy", smsTime).toString()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemNotificationBinding.inflate(parent.inflater()))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

}