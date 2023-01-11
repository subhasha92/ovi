package org.ovi.feature.notification.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ovi.R
import org.ovi.base.BaseActivity
import org.ovi.databinding.ActivityNotificationBinding
import org.ovi.feature.notification.model.NotificationReadRequest
import org.ovi.feature.notification.model.RowsItem
import org.ovi.feature.notification.view.adapter.NotificationAdapter
import org.ovi.feature.notification.viewmodel.NotificationViewModel
import org.ovi.network.ResultOf
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.onLoadMoreListener
import org.ovi.util.extensions.show
import org.ovi.util.extensions.verticalView
import org.ovi.widget.OviSnackBar
import java.lang.ref.WeakReference

class NotificationActivity : BaseActivity<ActivityNotificationBinding>() {


    private val list = mutableListOf<RowsItem>()
    private val vm: NotificationViewModel by viewModel()

    private var isLoading = false
    private var page = 1
    private var pageSize = 10
    private var position = 0

    companion object {
        fun present(activityWeakReference: WeakReference<Activity>) {
            activityWeakReference.get()?.startActivity(
                Intent(
                    activityWeakReference.get(),
                    NotificationActivity::class.java
                )
            )
        }
    }

    override fun setupView() {
        binding.toolbar.setBackListener { finish() }
        binding.rvNotification.apply {
            verticalView(context)
            adapter = NotificationAdapter(list, ::callback)
            onLoadMoreListener {
                if (!isLoading) {
                    isLoading = true
                    page++
                    callApi()
                }
            }
        }
        callApi()
    }

    fun callback(id: Int) {
        vm.readNotification(NotificationReadRequest(listOf(id)))
    }

    fun callApi() {
        vm.getNotification(page, pageSize)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun bindViewModel() {

        bindReadNoti()

        vm.noti bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE, binding.rvNotification)
                }
                is ResultOf.Progress -> {
                    if (isLoading) {
                        if (it.loading)
                            binding.pvRecycle.show()
                        else
                            binding.pvRecycle.gone()
                    } else {
                        if (it.loading)
                            binding.progressView.show()
                        else {
                            binding.progressView.gone()
                            binding.pvRecycle.gone()
                        }
                    }
                }
                is ResultOf.Success -> {
                    if (it.value.data?.rows != null)
                        if (it.value.data.rows.isNotEmpty()) {
                            if (!isLoading)
                                list.clear()
                            position = list.size
                            list.addAll(it.value.data.rows as MutableList<RowsItem>)

                            if (isLoading) {
                                isLoading = false
                                (binding.rvNotification.adapter as NotificationAdapter).notifyItemRangeChanged(
                                    position,
                                    list.size
                                )
                            } else
                                (binding.rvNotification.adapter as NotificationAdapter).notifyDataSetChanged()

                        } else {
                            if (page > 1 && isLoading) {
                                page--
                                isLoading = false
                            }
                            if (list.isEmpty())
                                showEmptyLayout()
                        }
                }
            }
        }

    }

    private fun bindReadNoti() {
        vm.read bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
//                    Log.e(TAG, "bindReadNoti: ${it.message}", )
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
//                    callApi()
                }
            }
        }
    }

    private fun showEmptyLayout() {
        binding.incEmptyLayout.root.show()
        binding.incEmptyLayout.emptyMessage.text = getString(
            R.string.oops_currently_no_events_are_available_check_back_later,
            "notification"
        )
    }


    override fun getViewBinding() = ActivityNotificationBinding.inflate(layoutInflater)

    override fun onClick(view: View) {

    }
}