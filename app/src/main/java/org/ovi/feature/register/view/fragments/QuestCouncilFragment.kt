package org.ovi.feature.register.view.fragments

import android.view.View
import androidx.core.view.isNotEmpty
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentEnterCounsilBinding
import org.ovi.feature.profile.model.EditProfileRequest
import org.ovi.feature.profile.viewmodel.ProfileViewModel
import org.ovi.feature.register.model.RegisterRequest
import org.ovi.feature.register.view.activity.AdditionalQuestActivity
import org.ovi.feature.register.viewmodel.MasterViewModel
import org.ovi.feature.register.viewmodel.RegisterViewModel
import org.ovi.network.ResultOf
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.show
import org.ovi.widget.OviSnackBar

class QuestCouncilFragment:BaseFragment<FragmentEnterCounsilBinding>() {

    private val profVM: ProfileViewModel by viewModel{ parametersOf(0) }


    override fun onClick(view: View) {

        when(view){
            binding.include.btnNext-> {
                if (binding.include.etText.text.trim().isNotEmpty())
                    profVM.putProfile(EditProfileRequest(youth_council_name =binding.include.etText.text.trim() ))
                else
                    binding.include.etText.setError("Field can't be blank")
            }
            binding.include.tvSkip-> (requireActivity() as AdditionalQuestActivity).movePager()
        }

    }

    override fun getViewBinding()=FragmentEnterCounsilBinding.inflate(layoutInflater)

    override fun setupView() {
        with(binding.include){
            tvTitle.text=getString(R.string.please_enter_youth_council_name)
            tvText.text=getString(R.string.ovi_would_like_to_know_if_you_are_participating_in_a_youth_council_or_advisory_board)

            etText.label="Youth Council Name"
            etText.hint="Enter Council name"
            tvSkip.show()
            tvSkip.setOnClickListener(onClickListener)
            btnNext.setOnClickListener(onClickListener)
        }

    }

    override fun bindViewModel() {
        profVM.putProfile bindTo {
            when(it){
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message,OviSnackBar.SnackType.FAILURE)
                }
                is ResultOf.Progress -> {if (it.loading)
                binding.progressView.show()
                else
                binding.progressView.gone()}
                is ResultOf.Success -> {
                    (requireActivity() as AdditionalQuestActivity).movePager()
                }
            }
        }

    }

}