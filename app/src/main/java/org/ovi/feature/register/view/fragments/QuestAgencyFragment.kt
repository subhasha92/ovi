package org.ovi.feature.register.view.fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.data.local.SelectionModelWithId
import org.ovi.databinding.FragmentQuestAgencyBinding
import org.ovi.feature.profile.model.EditProfileRequest
import org.ovi.feature.profile.viewmodel.ProfileViewModel
import org.ovi.feature.register.view.activity.AdditionalQuestActivity
import org.ovi.feature.register.view.adapter.RegListWithIdAdapter
import org.ovi.feature.register.viewmodel.MasterViewModel
import org.ovi.network.ResultOf
import org.ovi.util.HorzSpacingItemDecoration
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.onLoadMoreListener
import org.ovi.util.extensions.show
import org.ovi.util.extensions.verticalView
import org.ovi.widget.OviSnackBar

class QuestAgencyFragment : BaseFragment<FragmentQuestAgencyBinding>() {

    private val vm: ProfileViewModel by viewModel { parametersOf(1) }
    private val masterVM: MasterViewModel by viewModel()

    var page = 1
    var isLoading = false
    var isSearch = false
    var position = 0

    override fun onClick(view: View) {
        when (view) {
            binding.tvSkip -> movePager()
        }
    }

    private val listAgency = mutableListOf<SelectionModelWithId>()
    private val listCounties = mutableListOf<SelectionModelWithId>()

    private val countyAdapter = RegListWithIdAdapter(
        ::callback,
        listCounties as ArrayList<SelectionModelWithId>,
        AgencyCounty.COUNTY
    )
    private val agencyAdapter = RegListWithIdAdapter(
        ::callback,
        listAgency as ArrayList<SelectionModelWithId>,
        AgencyCounty.AGENCY
    )

    override fun getViewBinding() = FragmentQuestAgencyBinding.inflate(layoutInflater)

    override fun setupView() {
        with(binding) {
            tvTitle.text = getString(R.string.please_enter_your_agency)
            etSearch.show()
            tvSkip.show()
            setUpRadio()
            tvSkip.setOnClickListener(onClickListener)
            tvText.text =
                getString(R.string.you_should_have_the_list_of_agencies_but_let_me_know_if_you_need_the_information_again_thanks)
            rvRecycle.apply {
                verticalView(context)
                addItemDecoration(HorzSpacingItemDecoration(12))
                onLoadMoreListener {
//                    Log.e(TAG, "setupView: ${listCounties.size} : ${countyAdapter.itemCount}")
                    isLoading = true
                    page++
                    onLoadMore()
                }
            }

            etSearch.addTextChangedListener {
                runDelayed(500) {
                    Log.e(TAG, "setupView: ${binding.radioGroup.checkedRadioButtonId}")
                    if (binding.radioGroup.checkedRadioButtonId == binding.radioAgency.id) {
                        val adapter = binding.rvRecycle.adapter as RegListWithIdAdapter
                        adapter.filter.filter(it)
                    } else {
                        if (it?.isEmpty() == true) {
                            masterVM.cancel()
                            page = 1
                            listCounties.clear()
                            onLoadMore()
                        } else {
                            isSearch = true
                            searchCounty(it.toString())
                        }
                    }
                }
            }
        }
        onLoadMore()
    }

    private fun onLoadMore() {
        masterVM.getCounties(page = page)
    }

    private fun searchCounty(text: String?) {
        masterVM.getCounties(page = null, pageSize = null, search = text)
    }

    private fun setUpRadio() {
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            setUpAdapter()
        }
    }

    private fun callback(choice_id: Int?, text_value: String?, type: AgencyCounty) {
        when (type) {
            AgencyCounty.AGENCY -> {
                vm.putProfile(EditProfileRequest(agency_id = choice_id))
            }
            AgencyCounty.COUNTY -> {
                vm.putProfile(EditProfileRequest(county = text_value))
            }
        }

    }

    override fun bindViewModel() {
        bindAgency()
        bindCounties()
        bindUpdateDetails()
    }

    private fun movePager() {
        (requireActivity() as AdditionalQuestActivity).movePager()
    }

    private fun bindUpdateDetails() {
        vm.putProfile bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE)
//                    Log.d(TAG, "bindUpdateDetails: ${it.message}")
                }
                is ResultOf.Progress -> {
                    if (it.loading) binding.progressView.show() else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    movePager()
                }
            }
        }
    }

    private fun bindAgency() {
        vm.agency bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE)
//                    Log.e(TAG, "bindAgency: ${it.message}")
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    if (it.value.data?.rows != null) {
                        val sorted = it.value.data.rows
//                        Log.d(TAG, "bindAgency: $sorted")
                        listAgency.addAll(sorted.map {
                            SelectionModelWithId(
                                it?.name.toString(),
                                false,
                                it?.id.toString()
                            )
                        })
                        setUpAdapter()
                    }
                }
            }
        }
    }

    private fun bindCounties() {

        masterVM.counties bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                }
                is ResultOf.Progress -> {
                    if (isLoading) {
                        if (it.loading)
                            binding.pvRecycle.show()
                        else
                            binding.pvRecycle.gone()
                    }
                }
                is ResultOf.Success -> {
                    if (isSearch) {
                        listCounties.clear()
                        isSearch = false
                    }
                    position = listCounties.size
                    it.value.data?.forEachIndexed { index, s ->
                        listCounties.add(
                            SelectionModelWithId(
                                s.toString(), false,
                                index.toString()
                            )
                        )
                    }
                    if (isLoading)
                        notifyItems()
                    else
                        setUpAdapter()
                }
            }
        }
    }

    private fun notifyItems() {
        isLoading = false
        countyAdapter.notifyItemRangeChanged(position, listCounties.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpAdapter() {
        when (binding.radioGroup.checkedRadioButtonId) {
            binding.radioAgency.id -> {
//                Log.d(TAG, "setUpAdapter: $listAgency")
                binding.rvRecycle.apply {
                    verticalView(requireContext())
                    adapter = agencyAdapter

                }
                agencyAdapter.notifyDataSetChanged()
            }
            binding.radioCountry.id -> {
//                Log.d(TAG, "setUpAdapter: $listCounties")
                binding.rvRecycle.apply {
                    verticalView(requireContext())
                    adapter = countyAdapter
                }
                countyAdapter.notifyDataSetChanged()
            }
        }
    }
}

enum class AgencyCounty {
    AGENCY,
    COUNTY
}