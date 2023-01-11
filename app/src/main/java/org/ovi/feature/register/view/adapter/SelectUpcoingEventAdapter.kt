package org.ovi.feature.register.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ovi.data.local.SelectionModel
import org.ovi.databinding.ItemCurrentOpenEventBinding
import org.ovi.databinding.ItemRoundedBlueBorderBinding
import org.ovi.util.extensions.applyMatchWrapContent
import org.ovi.util.extensions.inflater

class SelectUpcoingEventAdapter : RecyclerView.Adapter<SelectUpcoingEventAdapter.VH>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectUpcoingEventAdapter.VH {
        return VH(ItemCurrentOpenEventBinding.inflate(parent.inflater()))
    }

    override fun onBindViewHolder(holder: SelectUpcoingEventAdapter.VH, position: Int) {

    }

    override fun getItemCount() = 3


    inner class VH(val binding: ItemCurrentOpenEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding.root) {
                applyMatchWrapContent()
                setOnClickListener {

                }

            }
        }
    }


}

