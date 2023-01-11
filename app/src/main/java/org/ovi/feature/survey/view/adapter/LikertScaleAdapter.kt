package org.ovi.feature.survey.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ovi.data.local.SelectionModelWithId
import org.ovi.databinding.ItemLikertScaleBinding
import org.ovi.util.extensions.applyMatchWrapContent
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.hide
import org.ovi.util.extensions.inflater
import kotlin.reflect.KFunction2
import kotlin.reflect.KFunction3

class LikertScaleAdapter(
    val dataList: List<SelectionModelWithId>,
    private val callback: KFunction3<Boolean, String, String, Unit>
) : RecyclerView.Adapter<LikertScaleAdapter.VH>() {

    inner class VH(val binding: ItemLikertScaleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.applyMatchWrapContent()
            binding.root.setOnClickListener {
                dataList.forEachIndexed { index, selectionModel ->
                    selectionModel.isSelected = index == absoluteAdapterPosition
                }
                callback.invoke(true, dataList[absoluteAdapterPosition].id,dataList[absoluteAdapterPosition].content)
                notifyItemRangeChanged(0,dataList.size)
//                notifyDataSetChanged()
            }
        }

        fun bind(selectionModelWithId: SelectionModelWithId) {
            binding.model = selectionModelWithId
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemLikertScaleBinding.inflate(parent.inflater()))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        setLine(holder, position)
        setLeftRight(holder, position)
        holder.bind(dataList[position])
    }

    private fun setLeftRight(holder: VH, position: Int) {
        if (position == 0)
            holder.binding.tvRightTitle.hide()
        else {
            if (position % 2 == 0)
                holder.binding.tvRightTitle.hide()
            else
                holder.binding.tvLeftTitle.hide()
        }
    }

    private fun setLine(holder: LikertScaleAdapter.VH, position: Int) {
        if (position == 0)
            holder.binding.vwLineTop.hide()

        if (position == dataList.size - 1)
            holder.binding.vwLineBottom.hide()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = dataList.size


}