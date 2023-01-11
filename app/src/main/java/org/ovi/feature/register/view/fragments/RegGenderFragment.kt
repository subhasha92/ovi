package org.ovi.feature.register.view.fragments

import android.annotation.SuppressLint
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.data.local.SelectionModel
import org.ovi.databinding.FragmentRegGenderBinding
import org.ovi.feature.register.model.MasterDataType
import org.ovi.feature.register.model.RegKey
import org.ovi.feature.register.view.activity.RegisterActivity
import org.ovi.feature.register.view.adapter.RegListAdapter
import org.ovi.feature.register.viewmodel.MasterViewModel
import org.ovi.network.ResultOf
import org.ovi.util.HorzSpacingItemDecoration
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.show
import org.ovi.util.extensions.verticalView

class RegGenderFragment : BaseFragment<FragmentRegGenderBinding>() {

    private val genderList = mutableListOf<SelectionModel>()
    private val vm: MasterViewModel by viewModel()

    override fun onClick(view: View) {
        when (view) {
            binding.incRecycle.btnNext -> {
                if (binding.incRecycle.etOther.text?.isNotEmpty() == true) {
                    (requireActivity() as RegisterActivity).storeRegisterRequest(
                        RegKey.GENDER.value,
                        binding.incRecycle.etOther.text.toString()
                    )
                    (requireActivity() as RegisterActivity).movePager()
                } else
                    binding.incRecycle.etOther.error = "Field can't be blank"
            }
            binding.incRecycle.tvSkip->{
                (requireActivity() as RegisterActivity).movePager()
            }
        }
    }

    override fun getViewBinding() = FragmentRegGenderBinding.inflate(layoutInflater)

    override fun setupView() {
        binding.incRecycle.btnNext.setOnClickListener(onClickListener)
        binding.incRecycle.tvSkip.show()
        binding.incRecycle.tvSkip.setOnClickListener(onClickListener)
    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        with(binding.incRecycle) {
            tvTitle.text = getString(R.string.to_which_gender_identify_do_you_most_identifiy)
            tvText.text =
                getString(R.string.we_would_like_to_know_how_you_would_describe_yourself_using_the_options_below_this_will_help_us_when_we_evaluate_the_program)
            etSearch.gone()
            rvRecycle.apply {
                verticalView(context)
                adapter = RegListAdapter(::callback, genderList as ArrayList<SelectionModel>)
                addItemDecoration(HorzSpacingItemDecoration(12))
            }
            etOther.setHint("Type your gender")
            vm.getMaster(MasterDataType.GENDER.value)
        }

    }

    private fun callback(choice_id: Int?, text_value: String) {
        if (text_value == "Other") {
            binding.incRecycle.etOther.show()
            binding.incRecycle.btnNext.show()

        } else {
            binding.incRecycle.etOther.gone()
            binding.incRecycle.btnNext.gone()
            (requireActivity() as RegisterActivity).storeRegisterRequest(
                RegKey.GENDER.value,
                text_value
            )
            (requireActivity() as RegisterActivity).movePager()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun bindViewModel() {

        vm.masterData bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {}
                is ResultOf.Progress -> {
                    if (it.loading) binding.progressView.show() else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    val sortedValue = it.value.data?.rows?.sortedBy { it?.id }
//                    Log.e(TAG, "bindViewModel: $sortedValue" )
                    sortedValue?.forEach {
                        genderList.add(SelectionModel(it?.value.toString(), false))
                    }
                    (binding.incRecycle.rvRecycle.adapter as RegListAdapter).notifyDataSetChanged()

                }
            }
        }

    }
}