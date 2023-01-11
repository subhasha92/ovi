package org.ovi.feature.profile.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentEventTrackBinding
import org.ovi.feature.events.model.EventDetailsMode
import org.ovi.feature.events.view.activity.EventsDetailActivity
import org.ovi.feature.events.viewmodel.EventsViewModel
import org.ovi.feature.home.model.Data
import org.ovi.feature.home.model.RowsItem
import org.ovi.feature.home.view.activity.HomeActivity
import org.ovi.feature.profile.view.activity.RETURN_RESULT
import org.ovi.feature.profile.view.adapter.EventTrackAdapter
import org.ovi.feature.profile.viewmodel.ProfileViewModel
import org.ovi.network.ResultOf
import org.ovi.util.HorzSpacingItemDecoration
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.show
import org.ovi.util.extensions.verticalView
import org.ovi.widget.OviSnackBar
import java.lang.ref.WeakReference


class EventTrackFragment : BaseFragment<FragmentEventTrackBinding>() {

    private val eventVM: EventsViewModel by viewModel { parametersOf(0) }
    private val profileVM: ProfileViewModel by viewModel { parametersOf(-1) }

    private val rowItems = mutableListOf<RowsItem>()

    override fun onClick(view: View) {

    }

    override fun getViewBinding() = FragmentEventTrackBinding.inflate(layoutInflater)

    override fun setupView() {
        binding.include.rvEventList.apply {
            verticalView(context)
            addItemDecoration(HorzSpacingItemDecoration(22))
        }
//        binding.swipe.setOnRefreshListener {
//            callApi()
//            binding.swipe.isRefreshing = false
//        }
    }

    fun refresh() {
        callApi()
    }

    private fun callApi() {
        eventVM.getMyEvents()
//        profileVM.getProfile()
    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        callApi()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun bindViewModel() {
        bindMyEvents()
        profileVM.getProfile bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
//                    Log.e(TAG, "bindViewModel: ${it.message}")

                }
                is ResultOf.Progress -> {
                }
                is ResultOf.Success -> {
                    binding.include.tvListDetails.text = getString(
                        R.string.invited_5_accepted_4_registered_4_attended_4,
                        it.value.data?.events_invited,
                        it.value.data?.events_accepted,
                        it.value.data?.events_registered,
                        it.value.data?.events_attended
                    )
                }
            }
        }
    }

    private fun bindMyEvents() {
        eventVM.myEvents bindTo {
            when (it) {
                is ResultOf.Empty -> {

                }
                is ResultOf.Failure -> {
//                    Log.e(TAG, "bindViewModel: ${it.message}")
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE)
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else {
                        binding.progressView.gone()
                        binding.include.root.show()
                    }
                }
                is ResultOf.Success -> {
                    if (it.value.data != null) {
                        setValue(it.value.data)

                    }
                }
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setValue(data: Data) {
        var countInvited = 0
        var countAccepted = 0
        var countRegisterd = 0
        var countAttended = 0
//        Log.d(TAG, "setValue: $data")
        rowItems.clear()
        if (data.invited?.rows != null) {
            countInvited = data.invited.count!!
            rowItems.addAll(data.invited.rows as MutableList<RowsItem>)
        }

        if (data.accepted?.rows != null) {
            countAccepted = data.accepted.count!!
            rowItems.addAll(data.accepted.rows as MutableList<RowsItem>)
        }

        if (data.registered?.rows != null) {
            countRegisterd = data.registered.count!!
            rowItems.addAll(data.registered.rows as MutableList<RowsItem>)
        }

        if (data.attended?.rows != null) {
            countAttended = data.attended.count!!
            rowItems.addAll(data.attended.rows as MutableList<RowsItem>)
        }

        binding.include.tvListDetails.text = getString(
            R.string.invited_5_accepted_4_registered_4_attended_4,
            countInvited,
            countAccepted,
            countRegisterd,
            countAttended
        )

        binding.include.rvEventList.adapter = EventTrackAdapter(rowItems, ::callBack)
        (binding.include.rvEventList.adapter as EventTrackAdapter).notifyDataSetChanged()

    }
    fun callBack(mode: EventDetailsMode, id: Int) {
        activityLauncher.launch(
            EventsDetailActivity.createIntent(
                WeakReference<Activity>(
                    requireActivity()
                ), mode, id
            )
        )
    }

    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data?.getBooleanExtra(RETURN_RESULT, false) == true) {
                    callApi()
                    (requireActivity() as HomeActivity).setRefreshStatus(
                        true
                    )
                }
            }
        }
}