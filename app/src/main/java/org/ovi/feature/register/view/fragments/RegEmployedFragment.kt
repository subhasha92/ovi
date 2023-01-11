package org.ovi.feature.register.view.fragments

import android.view.View
import org.ovi.base.BaseFragment
import org.ovi.data.local.DataUtil
import org.ovi.databinding.FragmentRegEmployedBinding
import org.ovi.feature.profile.model.AnswersItem

import org.ovi.feature.register.view.activity.RegisterActivity
import org.ovi.feature.register.view.adapter.RegListAdapter
import org.ovi.util.HorzSpacingItemDecoration
import org.ovi.util.extensions.show
import org.ovi.util.extensions.verticalView

class RegEmployedFragment : BaseFragment<FragmentRegEmployedBinding>() {


    private var position = 0

    private val data by lazy {
        (requireActivity() as RegisterActivity).getValue(position)
    }

    override fun onClick(view: View) {

        when (view) {
            binding.include.tvSkip -> {
                (requireActivity() as RegisterActivity).movePager()
            }
        }

    }

    fun setPosition(pos: Int) {
        position = pos
    }


    override fun getViewBinding() = FragmentRegEmployedBinding.inflate(layoutInflater)

    override fun setupView() {
        with(binding.include) {
            tvTitle.text = data.question
            rvRecycle.apply {
                verticalView(context)
                adapter = RegListAdapter(::callback, DataUtil.getYesNo())
                addItemDecoration(HorzSpacingItemDecoration(12))
            }
            tvSkip.show()
            tvSkip.setOnClickListener(onClickListener)
        }

    }

    private fun callback(choice_id: Int?, text_value: String) {
        val answer =
            AnswersItem(text_value = text_value, choice_id = choice_id, question_id = position)
        (requireActivity() as RegisterActivity).callOnBoardResponeAPi(answer)
        (requireActivity() as RegisterActivity).movePager()
    }

    override fun bindViewModel() {

    }
}