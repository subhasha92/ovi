package org.ovi.feature.menu.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ovi.data.local.DataUtil
import org.ovi.databinding.ItemMenuBinding
import org.ovi.util.extensions.applyMatchWrapContent
import org.ovi.util.extensions.inflater

class MenuListAdapter(val callBack: (String) -> Unit) : RecyclerView.Adapter<MenuListAdapter.VH>() {

    private val menuList by lazy {
        DataUtil.getOptionMenu()
    }

    inner class VH(val binding: ItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.applyMatchWrapContent()
        }

        fun bind(menu: String) {
            binding.tvTitle.text = menu
            binding.root.setOnClickListener {
                callBack.invoke(menu)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListAdapter.VH {
        return VH(ItemMenuBinding.inflate(parent.inflater()))

    }

    override fun onBindViewHolder(holder: MenuListAdapter.VH, position: Int) {
        holder.bind(menuList[position])
    }

    override fun getItemCount() = menuList.size
}