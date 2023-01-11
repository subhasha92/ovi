package org.ovi.feature.register.view.fragments

import android.view.View
import android.view.WindowManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ovi.base.BaseFragment
import org.ovi.data.local.PopupModel
import org.ovi.databinding.FragmentRegConfirmAddressBinding
import org.ovi.feature.profile.model.CountiesResponse
import org.ovi.feature.register.model.RegKey
import org.ovi.feature.register.model.RowsItemLocation
import org.ovi.feature.register.view.activity.RegisterActivity
import org.ovi.feature.register.viewmodel.MasterViewModel
import org.ovi.feature.register.viewmodel.RegisterViewModel
import org.ovi.network.ResultOf
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.hideSoftKeyboard
import org.ovi.widget.IDropDownListener


class RegConfirmAddresFragment : BaseFragment<FragmentRegConfirmAddressBinding>() {

    private val vm: RegisterViewModel by sharedViewModel()
    private val masterVM: MasterViewModel by viewModel()


    private val cityState = mutableListOf<RowsItemLocation>()
    private lateinit var county: CountiesResponse

    override fun onClick(view: View) {
        when (view) {
            binding.btnNext -> {
                if (isValid()) {
                    (requireActivity() as RegisterActivity).storeRegisterRequest(
                        RegKey.BUILDING.value,
                        binding.etBuilding.text
                    )
                    (requireActivity() as RegisterActivity).storeRegisterRequest(
                        RegKey.AREA.value,
                        binding.etArea.text
                    )
                    (requireActivity() as RegisterActivity).storeRegisterRequest(
                        RegKey.STATE.value,
                        binding.etState.text
                    )
                    (requireActivity() as RegisterActivity).storeRegisterRequest(
                        RegKey.COUNTRY.value,
                        binding.etCountry.text
                    )
                    (requireActivity() as RegisterActivity).storeRegisterRequest(
                        RegKey.ZIPCODE.value,
                        binding.etZip.text
                    )

                    (requireActivity() as RegisterActivity).movePager()
                }
            }
            binding.tvSkip -> {
                (requireActivity() as RegisterActivity).movePager()
            }
        }
    }

    fun callBack(zipcode: String?, Zip: Int) {
        masterVM.getZips(zipcode)
    }

    override fun getViewBinding() = FragmentRegConfirmAddressBinding.inflate(layoutInflater)

    override fun setupView() {
        with(binding)
        {
            btnNext.setOnClickListener(onClickListener)
            etZip.addTextChangeListener(1, 1, ::callBack, ::callbackLocation, ::resetCallBack)

            tvSkip.setOnClickListener(onClickListener)
            etBuilding.setOnFocusChange {
//                Log.d(TAG, "setupView: true")
                this@RegConfirmAddresFragment.hideSoftKeyboard()
            }
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            etZip.setOnFocusChange { this@RegConfirmAddresFragment.hideSoftKeyboard() }
            root.setOnFocusChangeListener { v, hasFocus ->

                this@RegConfirmAddresFragment.hideSoftKeyboard()
            }
            requireActivity().setFinishOnTouchOutside(true)

        }
//        setDDValues()

    }

    private fun resetCallBack() {
        binding.etArea.text = ""
        binding.etState.text = ""
        binding.etCountry.text = ""

    }

    private fun callbackLocation(zipcode: String?) {
        getLocations(zipcode)
    }


    private fun isValid(): Boolean {
        return when {
            binding.etBuilding.text.isEmpty() -> {
                binding.etBuilding.setError("field required")
                false
            }
            binding.etArea.text.isEmpty() || binding.etArea.text == "null" -> {
                binding.etArea.setError("field required")
                false
            }
            binding.etState.text.isEmpty() || binding.etState.text == "null" -> {
                binding.etState.setError("field required")
                false
            }
            binding.etCountry.text.isEmpty() || binding.etCountry.text == "null" -> {
                binding.etCountry.setError("field required")
                false
            }
            binding.etZip.text.isEmpty() -> {
                binding.etZip.setError("field required")
                false
            }

            else -> true
        }

    }

    private val ddListener = object : IDropDownListener {
        override fun onDDClicked() {
        }

        override fun onOptionClicked(view: View, model: PopupModel) {
        }
    }

    private fun getLocations(zipcode: String?) {
        vm.getLocation(zipcode)
    }

    override fun bindViewModel() {
        vm.zipcode bindTo {
            binding.etZip.text = it.toString()
//           getLocations(it)
        }
        bindLocation()
//        bindCounties()
        bindZipcodes()
    }

    private fun bindZipcodes() {
        masterVM.ZipCodes bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {}
                is ResultOf.Progress -> {}
                is ResultOf.Success -> {
                    binding.etZip.setPopUpList(
                        requireContext(),
                        it.value.data as List<String>
                    )
                }
            }

        }
    }

//    private fun bindCounties() {
//        profVm.counties bindTo {
//            when (it) {
//                is ResultOf.Empty -> {}
//                is ResultOf.Failure -> {}
//                is ResultOf.Progress -> {}
//                is ResultOf.Success -> {
//                    county = it.value
//                    binding.etCountry.iDropDownListener = ddListener
//                    binding.etCountry.loadPopupItems(county.data?.rows?.map { it?.county }
//                        ?.toList() as List<String>)
//                }
//            }
//
//        }
//    }

    private fun bindLocation() {
        vm.location bindTo {
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
                    cityState.clear()
                    if (it.value.data?.rows != null) {
                        cityState.addAll(it.value.data.rows as MutableList<RowsItemLocation>)
                    }
                    loadDDValues()
                }
            }
        }
    }

    private fun loadDDValues() {
        val city = cityState.map { it.city }.sortedBy { it }.distinct()
        val state = cityState.map { it.state }.distinct().sortedBy { it }
        val county = cityState.map { it.county }.distinct().sortedBy { it.toString() }
//        city.let { binding.etArea.loadPopupItems(it as List<String>) }
//        state.let { binding.etState.loadPopupItems(it as List<String>) }

        binding.etArea.text = if (city.isNotEmpty()) city[0].toString() else "null"
        binding.etState.text = if (state.isNotEmpty()) state[0].toString() else "null"
        binding.etCountry.text = if (county.isNotEmpty()) county[0].toString() else "null"

    }
}