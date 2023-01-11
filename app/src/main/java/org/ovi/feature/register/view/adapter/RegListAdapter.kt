package org.ovi.feature.register.view.adapter

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import org.ovi.data.local.SelectionModel
import org.ovi.databinding.ItemRoundedBlueBorderBinding
import org.ovi.util.extensions.applyMatchWrapContent
import org.ovi.util.extensions.inflater
import kotlin.reflect.KFunction2

class RegListAdapter(
    val callback: KFunction2<Int?, String, Unit>,
    val dataList: List<SelectionModel>?
) :
    RecyclerView.Adapter<RegListAdapter.VH>(), Filterable {

    private var filteredList: List<SelectionModel>? = dataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegListAdapter.VH {
        return VH(ItemRoundedBlueBorderBinding.inflate(parent.inflater()))
    }

    override fun onBindViewHolder(holder: RegListAdapter.VH, position: Int) {
        holder.bind(filteredList?.get(position)!!)
    }

    override fun getItemCount() = filteredList?.size?:0

    inner class VH(val binding: ItemRoundedBlueBorderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding.root) {
                applyMatchWrapContent()
                setOnClickListener {
                    binding.tvTitle.isSelected = !binding.tvTitle.isSelected
                    dataList?.forEachIndexed { index, selectionModel ->
                        selectionModel.isSelected = index == absoluteAdapterPosition
                    }
                    val position = absoluteAdapterPosition.plus(1)
                    callback.invoke(
                        position,
                        binding.tvTitle.text.toString()
                    )
                    notifyDataSetChanged()

                }
            }
        }

        fun bind(s: SelectionModel) {
            binding.model = s
            binding.executePendingBindings()
        }
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredListcut = mutableListOf<SelectionModel?>()
            if (constraint?.length == 0) {
                filteredListcut.addAll(dataList!!)
            } else {
                val filterPattern = constraint.toString().trim()
                filteredListcut.clear()
                for (item in dataList!!) {
                    if (item.content.contains(filterPattern, true)) {
                        filteredListcut.add(item)
//                        Log.e(TAG, "performFiltering: $item")
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredListcut
            results.count = filteredListcut.size
            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.count!! > 0) {
                filteredList = results.values as ArrayList<SelectionModel>
                notifyDataSetChanged()
            } else {
                notifyDataSetChanged()
            }
        }
    }

}