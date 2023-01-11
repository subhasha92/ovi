package org.ovi.feature.home.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentHomeBinding
import org.ovi.feature.auth.view.LoginActivity
import org.ovi.feature.events.model.EventDetailsMode
import org.ovi.feature.events.view.activity.EventsDetailActivity
import org.ovi.feature.events.view.adapter.EventsListAdapter
import org.ovi.feature.events.viewmodel.EventsViewModel
import org.ovi.feature.home.model.DataItem
import org.ovi.feature.home.model.RowsItem
import org.ovi.feature.home.view.activity.HomeActivity
import org.ovi.feature.home.viewmodel.HomeViewModel
import org.ovi.feature.notification.view.NotificationActivity
import org.ovi.feature.profile.view.activity.RETURN_RESULT
import org.ovi.feature.register.view.activity.ChooseRoleActivity
import org.ovi.network.ResultOf
import org.ovi.util.HorzSpacingItemDecoration
import org.ovi.util.extensions.*
import org.ovi.widget.OviSnackBar
import java.lang.ref.WeakReference
import java.util.*


class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val vm: HomeViewModel by viewModel()
    private val eventVM: EventsViewModel by viewModel { parametersOf(0) }
    private val eventList = mutableListOf<RowsItem?>()
    private val userEventList = mutableListOf<RowsItem?>()
    private var barChartHasData = true

    override fun onClick(view: View) {
        when (view) {
            binding.start.incLogin.btnLogin -> {
                LoginActivity.present(requireContext())
            }
            binding.start.incLogin.tvJoinOvi -> {
                ChooseRoleActivity.present(requireContext())
            }
            binding.incErrorLayout.retryButton -> {
                callApi()
            }
        }
    }

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun setupView() {
        with(binding) {
            start.incLogin.btnLogin.setOnClickListener(onClickListener)
            start.incLogin.tvJoinOvi.setOnClickListener(onClickListener)
            checkRegisteredUser()
            start.incSelection.incTopSelect.widgetEvent.setOnClickListener {
                if (pref.isLoggedIn) {
                    findNavController().navigate(R.id.navigation_event)
                    (requireActivity() as HomeActivity).setItemSelected(R.id.navigation_event)
                } else
                    alertMessage()
            }
            start.incSelection.incTopSelect.widgetCommunication.setOnClickListener {
                if (pref.isLoggedIn)
                    NotificationActivity.present(WeakReference<Activity>(requireActivity()))
                else
                    alertMessage()
            }
            start.incSelection.incBottomSelect.widgetMyEvent.setOnClickListener {
                if (pref.isLoggedIn) {

                    findNavController().navigate(R.id.navigation_profile)
                    (requireActivity() as HomeActivity).moveToProfileOrEvent(1)
                    (requireActivity() as HomeActivity).setItemSelected(R.id.navigation_profile)
                } else
                    alertMessage()
            }
            start.incSelection.incBottomSelect.widgetMyProfile.setOnClickListener {
                if (pref.isLoggedIn) {
                    findNavController().navigate(R.id.navigation_profile)
                    (requireActivity() as HomeActivity).moveToProfileOrEvent(0)
                    (requireActivity() as HomeActivity).setItemSelected(R.id.navigation_profile)
                } else
                    alertMessage()
            }
            pref.imageUrl?.let { start.incSelection.incBottomSelect.widgetMyProfile.setImageUrl(it) }
//            bindViewModel()
            if ((requireActivity() as HomeActivity).getRefreshStatus())
                callApi()
            binding.incErrorLayout.retryButton.setOnClickListener(onClickListener)
        }
    }

    private fun checkRegisteredUser() {
        if (pref.isLoggedIn) {
            binding.start.incLogin.root.gone()
            binding.start.incSelection.incBottomSelect.root.show()
        } else {
            binding.start.incLogin.root.show()
            binding.start.incSelection.incBottomSelect.root.gone()
        }
    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        with(binding)
        {
            when {
                pref.isLoggedIn -> {
                    incRegisterUser.incNextEvent.tvTitle.text =
                        getString(R.string.select_your_next_event)
                    incRegisterUser.incNextEvent.rvViewList.apply {
                        verticalView(context)
                        adapter =
                            EventsListAdapter(::callBack, EventDetailsMode.REGISTER, userEventList)
                        addItemDecoration(HorzSpacingItemDecoration(6))
                    }
                }
                else
                -> {
                    incRegisterUser.root.gone()
                }
            }
            incUpcoming.tvTitle.text =
                if (pref.isLoggedIn) getString(R.string.upcoming_resources) else getString(R.string.upcoming_events_caps)
            incUpcoming.rvViewList.apply {
                verticalView(context)
                adapter = EventsListAdapter(
                    ::callBack,
                    if (pref.isLoggedIn) EventDetailsMode.CANCEL else EventDetailsMode.REGISTER,
                    eventList
                )
                addItemDecoration(HorzSpacingItemDecoration(6))
            }
        }
        callApi()
    }

    private fun callApi() {
        (requireActivity() as HomeActivity).setRefreshStatus(false)
        when {
            pref.isLoggedIn -> {
//                vm.getUserEvents()
                eventVM.getMyEvents()
//                vm.getUnregisteredEvent()
                vm.getReports()
            }
            else -> {
                vm.getEvents()
            }
        }

    }

    private fun setUpBarChart(data: List<DataItem?>) {

        with(binding.incRegisterUser.incReport) {

            if (data.isEmpty()) {
                binding.incRegisterUser.incReport.tvEventAttended.gone()
                barChart.setNoDataText("No Data Available")
                val paint: Paint = barChart.getPaint(Chart.PAINT_INFO)
                paint.color = requireContext().getOviColor(R.color.light_brown)
                paint.textSize = 40f
                barChartHasData = false
                barChart.invalidate()
                return
            }
            barChartHasData = true
            binding.incRegisterUser.incReport.tvEventAttended.show()
            var sum = 0
            data.forEach {
                sum += it?.event_count?.toInt()!!
            }
            binding.incRegisterUser.incReport.tvEventAttended.text =
                getString(R.string.event_attended_digit, sum)

            barChart.setDrawBarShadow(false)
            barChart.setDrawValueAboveBar(false)

            barChart.description.isEnabled = false

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
//            barChart.setMaxVisibleValueCount(10)

            // scaling can now only be done on x- and y-axis separately

            // scaling can now only be done on x- and y-axis separately
            barChart.setPinchZoom(true)
//            barChart.stopNestedScroll()

            barChart.setDrawGridBackground(false)

            barChart.legend.yOffset = 10f
//            barChart.legend.xOffset=10f


            val barDataSet = BarDataSet(barData(data), "No. of events attended")
            barDataSet.color = requireContext().getOviColor(R.color.light_brown)

            val barData = BarData(barDataSet)
            barChart.data = barData

            val xAxisLabel = arrayListOf<String>()

            data.forEach {
                it?.date.toString().getDateForBarChart()?.let { it1 -> xAxisLabel.add(it1) }
            }
//                arrayListOf(
//                "Mar 1", "Mar 15", "Mar 30"
//            )

            val yAxisLabel = arrayListOf<String>(
                "0", "10", "20", "30"
            )

            barChart.axisLeft.setDrawZeroLine(true)

//            barChart.axisLeft.setLabelCount(5, false)
//            barChart.axisLeft.valueFormatter = IndexAxisValueFormatter(yAxisLabel)

            barChart.axisRight.isEnabled = false

            val xAxis = barChart.xAxis

            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(xAxisLabel)
//                setCenterAxisLabels(true)
                setDrawAxisLine(true)
//                yOffset = 3f
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                isGranularityEnabled = true
//                axisMinimum=0f
            }

            barChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            barChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
//            barChart.setVisibleXRangeMaximum(5f)

            barData.barWidth = .25f
            barData.setDrawValues(true)

            barChart.axisLeft.axisMinimum = 0f
//            barChart.groupBars(0f, groupSpace, barSpace)
            barChart.invalidate()

        }

    }

    private fun barData(data: List<DataItem?>): ArrayList<BarEntry> {

        val list = arrayListOf<BarEntry>()

        data.forEachIndexed { index, dataItem ->
            dataItem?.event_count?.toFloat()?.let { BarEntry(index.toFloat(), it) }
                ?.let { list.add(it) }
        }
        return list
    }

    private fun callBack(eventMode: EventDetailsMode, eventId: Int) {
        if (pref.isLoggedIn)
            activityLauncher.launch(
                EventsDetailActivity.createIntent(
                    WeakReference<Activity>(
                        requireActivity()
                    ), eventMode, eventId
                )
            )
        else
            alertMessage()
    }

    override fun bindViewModel() {
        bindEventList()
//        bindUserEventList()
        bindReports()
        bindMyEvents()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindMyEvents() {
        eventVM.myEvents bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE)
//                    Log.d(TAG, "bindEventList: ${it.throwable?.message} : ${it.message}")
                    if (it.throwable?.message == "HTTP 502 Bad Gateway" || it.message == "No Internet Connection" || it.message == "Failed to connect to backend-prod.onevoiceimpact.app/54.81.112.63:443")
                        binding.incErrorLayout.root.show()
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {

//                    Log.e(TAG, "bindMyEvents: ${it.value}" )

                    val adapter =
                        binding.incUpcoming.rvViewList.adapter as EventsListAdapter
                    eventList.clear()
                    if (removeExpiredEvents(it.value.data?.upcoming?.rows, eventList))
                        binding.incUpcoming.root.show()
                    else
                        binding.incUpcoming.root.gone()

                    adapter.notifyDataSetChanged()
                    val adapter1 =
                        binding.incRegisterUser.incNextEvent.rvViewList.adapter as EventsListAdapter
                    userEventList.clear()
                    if (removeExpiredEvents(it.value.data?.unregistered?.rows, userEventList)) {
                        binding.incRegisterUser.root.show()
                        binding.incRegisterUser.incNextEvent.root.show()
                    } else {
                        binding.incRegisterUser.incNextEvent.root.gone()
                    }
                    adapter1.notifyDataSetChanged()
                    checkEmptyList()
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
//                Log.e(TAG, "removeExpiredEvents: ${rowsItem.event_start_date}")
                if (rowsItem.event_start_date.toString().getDate()?.after(Date()) == true) {
                    eventList.add(rowsItem)
                }
            }
        }
        return eventList.isNotEmpty()
    }

    private fun bindReports() {
        vm.reports bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
//                    Log.e(TAG, "bindReports: ${it.message}")
                    showSnackBar("Error in report fetching", OviSnackBar.SnackType.FAILURE)
                    if (it.throwable?.message == "HTTP 502 Bad Gateway" || it.message == "No Internet Connection")
                        binding.incErrorLayout.root.show()
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.incRegisterUser.incReport.progressView.show()
                    else
                        binding.incRegisterUser.incReport.progressView.gone()
                }
                is ResultOf.Success -> {
                    it.value.data?.let { it1 -> setUpBarChart(it1) }
                    binding.incRegisterUser.root.show()
                    binding.incRegisterUser.incReport.root.show()
                    checkEmptyList()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindUserEventList() {
//        Log.e(TAG, "bindUserEventList: ")
        vm.userEventsListHome bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE)
//                    Log.e(TAG, "bindUserEventList: ", )
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
                            binding.incRegisterUser.incNextEvent.root.gone()
                        else {
                            binding.incRegisterUser.root.show()
                            binding.incRegisterUser.incNextEvent.root.show()
                        }
                        val adapter =
                            binding.incRegisterUser.incNextEvent.rvViewList.adapter as EventsListAdapter
                        userEventList.clear()
                        userEventList.addAll(it.value.data.rows)
//                        Log.d(TAG, "bindUserEventList: ${it.value}")
                        adapter.notifyDataSetChanged()
                        checkEmptyList()
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindEventList() {
        vm.eventsListHome bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE)
//                    Log.d(TAG, "bindEventList: ${it.throwable?.message}")
                    if (it.throwable?.message == "HTTP 502 Bad Gateway" || it.message == "No Internet Connection")
                        binding.incErrorLayout.root.show()
//                    Log.e(TAG, "bindEventList: ${it.message}")
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    binding.incErrorLayout.root.gone()
                    if (it.value.data?.rows != null) {
                        binding.incUpcoming.root.show()

                        checkRegisteredUser()
                        val adapter =
                            binding.incUpcoming.rvViewList.adapter as? EventsListAdapter
                        eventList.clear()
                        eventList.addAll(it.value.data.rows)

                        adapter?.notifyDataSetChanged()

                        if (it.value.data.rows.isEmpty())
                            binding.incUpcoming.root.gone()
                        else
                            binding.incUpcoming.root.show()
                        checkEmptyList()
                    }
                }
            }
        }
    }

    private fun checkEmptyList() {
        if (!binding.incRegisterUser.incNextEvent.root.isVisible() && !binding.incUpcoming.root.isVisible() && !barChartHasData) {
            binding.incEmptyLayout.root.show()
            binding.incRegisterUser.incReport.root.gone()
            binding.start.root.gone()
            binding.incEmptyLayout.emptyMessage.text = getString(
                R.string.oops_currently_no_events_are_available_check_back_later,
                "events"
            )
        } else {
            binding.start.root.show()
            if (pref.isLoggedIn) {
                binding.incRegisterUser.root.show()
                binding.incRegisterUser.incReport.root.show()
            }
            binding.incEmptyLayout.root.gone()
            binding.incErrorLayout.root.gone()
        }

    }

    private fun alertMessage() {
        with(Dialog(requireContext())) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.custom_cofirmation_dialogue)

            val tvCancel = findViewById<AppCompatTextView>(R.id.tvNo)
            val tvYes = findViewById<AppCompatTextView>(R.id.tvYes)
            val title = findViewById<AppCompatTextView>(R.id.tvTitle)
            val messsage = findViewById<AppCompatTextView>(R.id.tvSubTitle)

            title.text = getString(R.string.registered_user_only)
            messsage.text = getString(
                R.string.please_login_to_view_event_details
            )

            tvCancel.setOnClickListener {
                dismiss()
            }
            tvYes.setOnClickListener {
                dismiss()
                LoginActivity.present(requireContext())
            }
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
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