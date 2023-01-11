package org.ovi.feature.survey.view.adapter

import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.callbackFlow
import org.ovi.data.local.SelectionModel
import org.ovi.data.local.SelectionModelWithId
import org.ovi.databinding.ItemSurveyRadiobuttonBinding
import org.ovi.util.extensions.*
import org.ovi.util.popup.popupMenu
import kotlin.reflect.KFunction2

class RadioButtonListAdapter(
    val dataList: List<SelectionModelWithId>,
    private val callback: KFunction2<Boolean, String, Unit>
) :
    RecyclerView.Adapter<RadioButtonListAdapter.VH>() {


    var string = ""
    var otherSelected = false

    var itemPosition = -1

    inner class VH(val binding: ItemSurveyRadiobuttonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.apply {
                applyMatchWrapContent()
            }
            binding.root.setOnClickListener {
                dataList.forEachIndexed { index, selectionModel ->
                    selectionModel.isSelected = index == absoluteAdapterPosition
                }
                itemPosition = absoluteAdapterPosition
                callback.invoke(true, dataList[absoluteAdapterPosition].id)
                binding.incEdit.etText.context.hideSoftKeyboard(binding.incEdit.etText)
//                openEdit(dataList[absoluteAdapterPosition])
                notifyDataSetChanged()
//                notifyItemChanged(absoluteAdapterPosition)
            }

        }

        fun bind(model: SelectionModelWithId) {
            binding.tvText.text = model.content
            binding.rbCheck.isChecked = model.isSelected
            binding.incEdit.etText.hint = model.content.toString()
            openEdit(model)
        }

        fun openEdit(selectionModel: SelectionModelWithId) {

            if (selectionModel.content.startsWith("other", true))
                otherSelected=selectionModel.isSelected
            if (binding.rbCheck.isChecked) {
                if (selectionModel.content.startsWith("other", true)) {
                    binding.incEdit.root.show()
                    binding.tvText.gone()
                    binding.incEdit.etText.forceAndShowKeyboard()
                    binding.incEdit.etText.requestFocusFromTouch()
                    binding.incEdit.etText.setOnClickListener{
                        binding.incEdit.etText.forceAndShowKeyboard()
                        binding.incEdit.etText.requestFocusFromTouch()
                    }
//                    binding.incEdit.etText.setOnFocusChangeListener { v, hasFocus ->
//                        if (!hasFocus)
//                            v.requestFocus()
//                    }
                    binding.incEdit.etText.addTextChangedListener {
                        string = it.toString()
                    }
                }
            } else {
                binding.incEdit.root.gone()
                binding.tvText.show()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemSurveyRadiobuttonBinding.inflate(parent.inflater(),parent,false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = dataList.size
}