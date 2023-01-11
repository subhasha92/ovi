package org.ovi.feature.register.view.fragments

import android.view.View
import androidx.fragment.app.activityViewModels
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentRegZipcodeBinding
import org.ovi.feature.register.model.RegKey
import org.ovi.feature.register.view.activity.RegisterActivity
import org.ovi.feature.register.viewmodel.MasterViewModel
import org.ovi.feature.register.viewmodel.RegisterViewModel
import org.ovi.network.ResultOf
import org.ovi.util.extensions.gone
import org.ovi.widget.OviSnackBar

class RegZipCodeFragment : BaseFragment<FragmentRegZipcodeBinding>() {

    private val vm: RegisterViewModel by activityViewModels()
    private val masterVM: MasterViewModel by viewModel()

    override fun onClick(view: View) {
        when (view) {
            binding.btnNext -> {
                if (binding.etText.text.isNotEmpty()) {
                    if (binding.etText.text.length > 4) {
                        vm.setZip(binding.etText.text.toString())
                        masterVM.getLocation(binding.etText.text)
                    } else
                        binding.etText.setError("Zip code should be 5 digit")
                } else {
                    binding.etText.setError("Zip code can't be blank")
                }
            }
            binding.tvSkip -> {
                (requireActivity() as RegisterActivity).movePager()
            }
        }
    }

    override fun getViewBinding() = FragmentRegZipcodeBinding.inflate(layoutInflater)

    private fun onValidZipcode() {
        (requireActivity() as RegisterActivity).storeRegisterRequest(
            RegKey.ZIPCODE.value,
            binding.etText.text
        )
        (requireActivity() as RegisterActivity).movePager()
    }

    override fun setupView() {
        with(binding)
        {

            etText.addTextChangeListener(1, 1, ::callBack)
            btnNext.setOnClickListener(onClickListener)
            tvSkip.setOnClickListener(onClickListener)
//            tvSkip.gone()
        }
    }

    fun callBack(zipcode: String?, zip: Int) {
        masterVM.getZips(zipcode)
    }

    override fun bindViewModel() {

        masterVM.ZipCodes bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
//                    Log.d(TAG, "bindViewModel: ${it.message}")
                }
                is ResultOf.Progress -> {}
                is ResultOf.Success -> {
                    binding.etText.setPopUpList(
                        requireContext(),
                        it.value.data as List<String>
                    )
                }
            }
        }

        masterVM.location bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {}
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    if (it.value.data?.rows?.isNotEmpty() == true) {
                        with(it.value.data.rows) {
                            when {
                                this[0]?.county == null -> {
                                    showSnackBar(
                                        "Please enter valid zipcode",
                                        OviSnackBar.SnackType.FAILURE
                                    )
                                }
                                this[0]?.state == null -> {
                                    showSnackBar(
                                        "Please enter valid zipcode",
                                        OviSnackBar.SnackType.FAILURE
                                    )
                                }
                                this[0]?.city == null -> {
                                    showSnackBar(
                                        "Please enter valid zipcode",
                                        OviSnackBar.SnackType.FAILURE
                                    )
                                }
                                else -> {
                                    onValidZipcode()
                                }
                            }
                        }

                    } else
                        showSnackBar(
                            "Please enter valid zipcode",
                            OviSnackBar.SnackType.FAILURE
                        )

                }
            }
        }

    }
}