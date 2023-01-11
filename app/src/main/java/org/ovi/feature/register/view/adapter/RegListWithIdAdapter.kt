package org.ovi.feature.register.view.adapter

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import org.ovi.data.local.SelectionModel
import org.ovi.data.local.SelectionModelWithId
import org.ovi.databinding.ItemRoundedBlueBorderBinding
import org.ovi.feature.register.view.fragments.AgencyCounty
import org.ovi.util.extensions.applyMatchWrapContent
import org.ovi.util.extensions.inflater
import kotlin.reflect.KFunction3

class RegListWithIdAdapter(
    val callback: KFunction3<Int?, String?, AgencyCounty, Unit>,
    val dataList: ArrayList<SelectionModelWithId>,
    val type: AgencyCounty
) :
    RecyclerView.Adapter<RegListWithIdAdapter.VH>(), Filterable {

    private var filteredList: ArrayList<SelectionModelWithId>? = dataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegListWithIdAdapter.VH {
        return VH(ItemRoundedBlueBorderBinding.inflate(parent.inflater()))
    }

    override fun onBindViewHolder(holder: RegListWithIdAdapter.VH, position: Int) {
        filteredList?.get(position)?.content?.let {
            SelectionModel(
                it,
                filteredList?.get(position)!!.isSelected
            )
        }
            ?.let { holder.bind(it) }
    }

    override fun getItemCount() = filteredList?.size ?: 0


    inner class VH(val binding: ItemRoundedBlueBorderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding.root) {
                applyMatchWrapContent()
                setOnClickListener {
//                    Log.e(TAG, "type: $type ", )
                    binding.tvTitle.isSelected = !binding.tvTitle.isSelected
                    dataList.forEachIndexed { index, selectionModel ->
                        selectionModel.isSelected = index == absoluteAdapterPosition
                    }
                    val position = dataList[absoluteAdapterPosition].id
                    notifyItemRangeChanged(
                        0,
                        dataList.size
                    )
//                    Log.e("TAG", "position : $position ")
                    callback.invoke(
                        position.toInt(),
                        binding.tvTitle.text.toString(),
                        type
                    )


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
            val filteredListcut = mutableListOf<SelectionModelWithId?>()
            if (constraint?.length == 0) {
                filteredListcut.addAll(dataList)
            } else {
                val filterPattern = constraint.toString().trim()
                filteredListcut.clear()
                for (item in dataList) {
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
                filteredList = results.values as ArrayList<SelectionModelWithId>
                notifyDataSetChanged()
            } else {
                notifyDataSetChanged()
            }
        }
    }

}