package org.ovi.feature.register.view.fragments

import android.view.View
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentSelectUpcomingEventBinding
import org.ovi.feature.register.view.activity.AdditionalQuestActivity
import org.ovi.feature.register.view.adapter.SelectUpcoingEventAdapter
import org.ovi.util.HorzSpacingItemDecoration
import org.ovi.util.extensions.verticalView

class QuestCurrentEventFragment : BaseFragment<FragmentSelectUpcomingEventBinding>() {


    override fun onClick(view: View) {
        when (view) {
            binding.btnNext -> {
                (requireActivity() as AdditionalQuestActivity).movePager()
            }
            binding.tvSkip -> {
                (requireActivity() as AdditionalQuestActivity).movePager()
            }
        }

    }

    override fun getViewBinding() = FragmentSelectUpcomingEventBinding.inflate(layoutInflater)


    override fun setupView() {
        binding.btnNext.setOnClickListener(onClickListener)
        binding.tvSkip.setOnClickListener(onClickListener)

        binding.rvList.apply {
            verticalView(
                context
            )
            adapter = SelectUpcoingEventAdapter()
            addItemDecoration(HorzSpacingItemDecoration(10))
        }

    }

    override fun bindViewModel() {

    }
}