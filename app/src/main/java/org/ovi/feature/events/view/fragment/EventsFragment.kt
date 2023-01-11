package org.ovi.feature.events.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentEventsBinding
import org.ovi.feature.events.model.EventDetailsMode
import org.ovi.feature.events.view.activity.EventsDetailActivity
import org.ovi.feature.events.view.adapter.EventsListAdapter
import org.ovi.feature.events.viewmodel.EventsViewModel
import org.ovi.feature.home.model.RowsItem
import org.ovi.feature.home.view.activity.HomeActivity
import org.ovi.feature.profile.view.activity.RETURN_RESULT
import org.ovi.feature.profile.view.fragment.ProfileHolderFragment
import org.ovi.network.ResultOf
import org.ovi.util.HorzSpacingItemDecoration
import org.ovi.util.extensions.*
import org.ovi.widget.OviSnackBar
import java.lang.ref.WeakReference
import java.util.*


class EventsFragment : BaseFragment<FragmentEventsBinding>() {

    private val vm: EventsViewModel by viewModel { parametersOf(-1) }

    private val eventList = mutableListOf<RowsItem?>()
    private val userEventList = mutableListOf<RowsItem?>()
    private val registerEventList = mutableListOf<RowsItem?>()

    override fun onClick(view: View) {

    }

    private fun callApi() {
        (requireActivity() as HomeActivity).setRefreshStatus(false)
        vm.getMyEvents()
    }

    override fun getViewBinding() = FragmentEventsBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        super.onFragmentCreated()

        with(binding) {
            incRvUpcoming.tvTitle.text = getString(R.string.your_upcoming_event)
            incRvInvitedFor.tvTitle.text = getString(R.string.you_are_invited_for)
            incRvNextEvent.tvTitle.text = getString(R.string.select_your_next_event)

            incRvUpcoming.rvViewList.apply {
                verticalView(context)
                adapter = EventsListAdapter(::callBack, EventDetailsMode.CANCEL, userEventList)
                addItemDecoration(HorzSpacingItemDecoration(6))
            }

            incRvInvitedFor.rvViewList.apply {
                verticalView(context)
                adapter = EventsListAdapter(::callBack, EventDetailsMode.ACCEPT, registerEventList)
                addItemDecoration(HorzSpacingItemDecoration(6))
            }

            incRvNextEvent.rvViewList.apply {
                verticalView(context)
                adapter = EventsListAdapter(::callBack, EventDetailsMode.REGISTER, eventList)
                addItemDecoration(HorzSpacingItemDecoration(6))
            }
        }

    }

    override fun setupView() {
        if ((requireActivity() as HomeActivity).getRefreshStatus())
            callApi()
    }

    private fun callBack(eventMode: EventDetailsMode, eventId: Int) {
        activityLauncher.launch(
            EventsDetailActivity.createIntent(
                WeakReference<Activity>(
                    requireActivity()
                ), eventMode, eventId
            )
        )
    }

    override fun bindViewModel() {
//        bindEventList()
//        bindUserEventList()
//        bindRegisteredEvents()
        bindMyEvents()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindMyEvents() {
        vm.myEvents bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE)
                    if (it.throwable?.message == "HTTP 502 Bad Gateway" || it.message == "No Internet Connection")
                        binding.incErrorLayout.root.show()
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    binding.incErrorLayout.root.gone()
                    val adapter1 =
                        binding.incRvUpcoming.rvViewList.adapter as EventsListAdapter
                    userEventList.clear()
                    if (removeExpiredEvents(it.value.data?.upcoming?.rows, userEventList))
                        binding.incRvUpcoming.root.show()
                    else
                        binding.incRvUpcoming.root.gone()

                    adapter1.notifyDataSetChanged()

                    val adapter2 =
                        binding.incRvInvitedFor.rvViewList.adapter as EventsListAdapter
                    registerEventList.clear()
                    if (removeExpiredEvents(it.value.data?.invited?.rows, registerEventList))
                        binding.incRvInvitedFor.root.show()
                    else
                        binding.incRvInvitedFor.root.gone()

                    adapter2.notifyDataSetChanged()

                    val adapter3 =
                        binding.incRvNextEvent.rvViewList.adapter as EventsListAdapter
                    eventList.clear()
                    if (removeExpiredEvents(it.value.data?.unregistered?.rows, eventList))
                        binding.incRvNextEvent.root.show()
                    else
                        binding.incRvNextEvent.root.gone()

                    adapter3.notifyDataSetChanged()

                    checkEmptyList()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindRegisteredEvents() {
        vm.registeredEvents bindTo {
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
                    if (it.value.data?.rows != null) {

                        binding.incRvInvitedFor.root.show()
//                        Log.e(TAG, "bindRegisteredEvents: ${it.value}")
                        val adapter =
                            binding.incRvInvitedFor.rvViewList.adapter as EventsListAdapter
                        registerEventList.clear()
                        registerEventList.addAll(it.value.data.rows)
                        adapter.notifyDataSetChanged()
                        checkEmptyList()
                    }
                }
            }
        }
    }

    private fun removeExpiredEvents(
        rows: List<RowsItem?>?,
        eventList: MutableList<RowsItem?>
    ): Boolean {
        rows?.forEachIndexed { index, rowsItem ->
            if (rowsItem?.event_start_date != null) {
//                Log.d(TAG, "removeExpiredEvents: ${rowsItem.event_start_date} : ${rowsItem.event_start_date.toString().getDate()}")
                if (rowsItem.event_start_date.toString().getDate()?.after(Date()) == true)
                    eventList.add(rowsItem)
            }
        }
        return eventList.isNotEmpty()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindUserEventList() {
        vm.userEventsList bindTo {
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
                    if (it.value.data?.rows != null) {

                        binding.incRvUpcoming.root.show()
//                        Log.e(TAG, "bindUserEventList: ${it.value}")
                        val adapter =
                            binding.incRvUpcoming.rvViewList.adapter as EventsListAdapter
                        userEventList.clear()
                        userEventList.addAll(it.value.data.rows)
                        adapter.notifyDataSetChanged()
                        checkEmptyList()
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindEventList() {
        vm.eventsList bindTo {
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
                    if (it.value.data?.rows != null) {
                        if (it.value.data.rows.isEmpty())
                            binding.incRvNextEvent.root.gone()
                        else
                            binding.incRvNextEvent.root.show()
//                        Log.e(TAG, "bindEventList: ${it.value}")
                        val adapter =
                            binding.incRvNextEvent.rvViewList.adapter as EventsListAdapter
                        eventList.clear()
                        eventList.addAll(it.value.data.rows)
                        adapter.notifyDataSetChanged()
                        checkEmptyList()
                    }
                }
            }
        }
    }

    private fun checkEmptyList() {
        if (!binding.incRvUpcoming.root.isVisible() && !binding.incRvNextEvent.root.isVisible() && !binding.incRvInvitedFor.root.isVisible()) {
            binding.incEmptyLayout.root.show()
            binding.incEmptyLayout.emptyMessage.text = getString(
                R.string.oops_currently_no_events_are_available_check_back_later,
                "events"
            )
        } else
            binding.incEmptyLayout.root.gone()
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