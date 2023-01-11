package org.ovi.feature.survey.view.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import org.ovi.data.local.SelectionModelWithId
import org.ovi.databinding.ItemCheckboxBinding
import org.ovi.util.extensions.*
import kotlin.reflect.KFunction3


class CheckBoxListAdapter(
    val selectionType: CheckBoxSelectionType,
    val dataList: List<SelectionModelWithId>,
    private val callback: KFunction3<Boolean, List<String>, List<String>, Unit>
) :
    RecyclerView.Adapter<CheckBoxListAdapter.VH>() {

    val selectedText = mutableListOf<String>()
    val selectedId = mutableListOf<String>()
    var string = ""
    var otherSelected = false

    inner class VH(val binding: ItemCheckboxBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(selectionModel: SelectionModelWithId) {
            binding.incEdit.etText.hint = selectionModel.content
            binding.tvText.text = selectionModel.content
            binding.cbCheck.isChecked = selectionModel.isSelected

            binding.root.setOnClickListener {
                when (selectionType) {
                    CheckBoxSelectionType.SINGLE -> {
                        selectedText.clear()
                        selectedText.add(dataList[absoluteAdapterPosition].content)
                        selectedId.clear()
                        selectedId.add(dataList[absoluteAdapterPosition].id)
                        callback.invoke(true, selectedText, selectedId)
                        dataList.forEachIndexed { index, model ->
                            model.isSelected = index == absoluteAdapterPosition
                        }
//                        binding.cbCheck.isChecked=selectionModel.isSelected
                        notifyDataSetChanged()

                    }
                    CheckBoxSelectionType.MULTIPLE -> {
                        selectionModel.isSelected =
                            !selectionModel.isSelected
//                        binding.cbCheck.setOnCheckedChangeListener { buttonView, isChecked ->
//                            selectionModel.isSelected = isChecked
//
//                        }
                        openEdit(selectionModel)
                        binding.cbCheck.isChecked = selectionModel.isSelected

                        callback.invoke(true, selectedText, selectedId)
//                        notifyItemChanged(absoluteAdapterPosition)


                    }
                }
            }
        }

        fun openEdit(selectionModel: SelectionModelWithId) {
//            binding.root.context.hideSoftKeyboard(binding.root)
//            Log.e(TAG, "openEdit: ${selectionModel.content}")
            if (selectionModel.content.startsWith("other", true))
                otherSelected = selectionModel.isSelected
            if (selectionModel.isSelected) {
                selectedText.add(dataList[absoluteAdapterPosition].content)
                selectedId.add(dataList[absoluteAdapterPosition].id)
                if (selectionModel.content.startsWith("other", true)) {
                    binding.incEdit.root.show()
                    binding.tvText.gone()

                    binding.incEdit.etText.forceAndShowKeyboard()
                    binding.incEdit.etText.requestFocusFromTouch()
//                    binding.incEdit.etText.setOnFocusChangeListener { v, hasFocus ->
//                        if (!hasFocus)
//                            v.requestFocus()
//                    }
                    binding.incEdit.etText.addTextChangedListener {
                        string = it.toString()
                    }

                }
            } else {
                selectedText.remove(dataList[absoluteAdapterPosition].content)
                selectedId.remove(dataList[absoluteAdapterPosition].id)
                binding.incEdit.root.gone()
                binding.tvText.show()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemCheckboxBinding.inflate(parent.inflater(), parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(dataList[position])
//        notifyItemChanged(position)
    }

    override fun getItemCount() = dataList.size

}