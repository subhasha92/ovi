package org.ovi.feature.register.view.fragments

import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.data.local.SelectionModel
import org.ovi.databinding.FragmentShirtSizeBinding
import org.ovi.feature.profile.model.EditProfileRequest
import org.ovi.feature.profile.viewmodel.ProfileViewModel
import org.ovi.feature.register.view.activity.AdditionalQuestActivity
import org.ovi.feature.register.view.adapter.RegListAdapter
import org.ovi.feature.register.viewmodel.MasterViewModel
import org.ovi.network.ResultOf
import org.ovi.util.HorzSpacingItemDecoration
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.show
import org.ovi.util.extensions.verticalView
import org.ovi.widget.OviSnackBar

class QuestShirtSizeFragment : BaseFragment<FragmentShirtSizeBinding>() {

    private val vm: ProfileViewModel by viewModel { parametersOf(0) }

    private val mastVM: MasterViewModel by viewModel()

    private var shirtList: ArrayList<SelectionModel>? = null

    override fun onClick(view: View) {
        when (view) {
            binding.include.tvSkip -> (requireActivity() as AdditionalQuestActivity).movePager()
            binding.include.btnNext -> {
                if (binding.include.etOther.text?.isNotEmpty() == true)
                    vm.putProfile(EditProfileRequest(shirt_size = binding.include.etOther.text.toString()))
                else
                    binding.include.etOther.error = "Field can't be blank"
            }
        }
    }

    override fun getViewBinding() = FragmentShirtSizeBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        mastVM.getMaster("shirt_size")
    }

    override fun setupView() {

        with(binding.include) {
            tvTitle.text = getString(R.string.please_enter_shirt_size)
            tvText.text =
                getString(R.string.we_d_like_to_know_your_size_for_ordering_swag_promotional_products_like_t_shirts_and_polos)
            tvSkip.show()
            tvSkip.setOnClickListener(onClickListener)
            rvRecycle.apply {
                verticalView(context)
//                adapter = RegListAdapter(::callback, DataUtil.getShirtSize())
                addItemDecoration(HorzSpacingItemDecoration(12))
            }
            etOther.setHint("Type your shirt size")
        }
        binding.include.btnNext.setOnClickListener(onClickListener)
    }

    private fun callback(choice_id: Int?, text_value: String) {

        if (text_value == "Other") {
            binding.include.etOther.show()
            binding.include.btnNext.show()
        } else {
            binding.include.etOther.gone()
            binding.include.btnNext.gone()
            vm.putProfile(EditProfileRequest(shirt_size = text_value))
        }
    }

    override fun bindViewModel() {
        vm.putProfile bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE)
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    (requireActivity() as AdditionalQuestActivity).movePager()
                }
            }
        }

        mastVM.masterData bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE)
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    shirtList =
                        it.value.data?.rows?.map {
                            SelectionModel(
                                it?.value.toString(),
                                false
                            )
                        } as ArrayList<SelectionModel>?

                    binding.include.rvRecycle.adapter = RegListAdapter(::callback, shirtList)
                }
            }
        }

    }
}