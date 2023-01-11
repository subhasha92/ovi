package org.ovi.feature.register.view.fragments

import android.annotation.SuppressLint
import android.view.View
import androidx.core.widget.addTextChangedListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.data.local.SelectionModel
import org.ovi.databinding.FragmentRegEnterRaceBinding
import org.ovi.feature.register.model.RegKey
import org.ovi.feature.register.view.activity.RegisterActivity
import org.ovi.feature.register.view.adapter.RegListAdapter
import org.ovi.feature.register.viewmodel.MasterViewModel
import org.ovi.network.ResultOf
import org.ovi.util.HorzSpacingItemDecoration
import org.ovi.util.extensions.show
import org.ovi.util.extensions.verticalView

class RegEnterRaceFragment : BaseFragment<FragmentRegEnterRaceBinding>() {

    private val vm: MasterViewModel by viewModel()
    private var listData = arrayListOf<SelectionModel>()

    override fun onClick(view: View) {
        when (view) {
            binding.incRecycle.tvSkip -> {
                (requireActivity() as RegisterActivity).movePager()
            }
        }
    }

    override fun getViewBinding() = FragmentRegEnterRaceBinding.inflate(layoutInflater)
    override fun setupView() {


    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        binding.incRecycle.tvTitle.text = getString(R.string.please_enter_race)
        binding.incRecycle.tvText.text =
            getString(R.string.we_would_like_to_know_how_you_would_describe_yourself_using_the_options_below_this_will_help_us_when_we_evaluate_the_program)

        binding.incRecycle.etSearch.show()
        binding.incRecycle.tvSkip.show()
        binding.incRecycle.tvSkip.setOnClickListener(onClickListener)


        binding.incRecycle.rvRecycle.apply {
            verticalView(context)
            adapter = RegListAdapter(::callback, listData)
            addItemDecoration(HorzSpacingItemDecoration(12))
        }
        vm.getRace()

        binding.incRecycle.etSearch.addTextChangedListener {
            val adapter = binding.incRecycle.rvRecycle.adapter as RegListAdapter
            adapter.filter.filter(it)
        }
    }

    private fun callback(choice_id: Int?, text_value: String) {
        (requireActivity() as RegisterActivity).storeRegisterRequest(RegKey.RACE.value, text_value)
        (requireActivity() as RegisterActivity).movePager()
    }

    override fun bindViewModel() {
        bindRace()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindRace() {
        vm.race bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {}
                is ResultOf.Progress -> {}
                is ResultOf.Success -> {
                    val list = it.value.data?.rows?.map {
                        SelectionModel(it?.value.toString(), false)
                    } as ArrayList
                    listData.clear()
                    listData.addAll(list)
                    val adapter = binding.incRecycle.rvRecycle.adapter as RegListAdapter
                    adapter.notifyDataSetChanged()
                }
            }

        }

    }
}