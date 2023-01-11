package org.ovi.feature.register.view.activity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import org.ovi.base.BaseActivity
import org.ovi.data.local.DataUtil
import org.ovi.data.local.SelectionModel
import org.ovi.databinding.ActivityChooseRoleBinding
import org.ovi.feature.register.model.MasterDataType
import org.ovi.feature.register.model.RowsItemMaster
import org.ovi.feature.register.view.adapter.RegListAdapter
import org.ovi.feature.register.viewmodel.MasterViewModel
import org.ovi.network.ResultOf
import org.ovi.util.HorzSpacingItemDecoration
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.verticalView
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChooseRoleActivity : BaseActivity<ActivityChooseRoleBinding>() {

    private val vm:MasterViewModel by viewModel()

    private val dataList= mutableListOf<SelectionModel>()
    private lateinit var valuItems:List<RowsItemMaster>

    companion object {
        fun present(context: Context) {
            context.startActivity(Intent(context, ChooseRoleActivity::class.java))
        }
    }

    override fun setupView() {
        binding.toolbar.setEndIconListener { finish() }
        binding.rvChooseRole.apply {
            verticalView(context)
            adapter = RegListAdapter(::callback, dataList as ArrayList<SelectionModel>)
            addItemDecoration(HorzSpacingItemDecoration(12))
        }
        vm.getMaster(MasterDataType.ROLE.value)
    }

    private fun callback(choice_id: Int?, text_value: String) {
        pref.showOnBoard= valuItems.find {it.value==text_value}?.id==28
//        Log.e(TAG, "callback: $tex" )
        RegisterActivity.present(this@ChooseRoleActivity, choice_id, text_value)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun bindViewModel() {

        vm.masterData bindTo {
            when(it){
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {}
                is ResultOf.Progress -> {
                    if  (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {

                    val roles=it.value.data?.rows?.sortedBy { it?.id }
                    roles?.forEach {
                        if (it?.id!=27)
                            dataList.add(SelectionModel(it?.value.toString(),false))
                    }
                    val adapter=binding.rvChooseRole.adapter as RegListAdapter
                    adapter.notifyDataSetChanged()
                    valuItems= it.value.data?.rows as List<RowsItemMaster>
                }
            }
        }
    }
    override fun getViewBinding() = ActivityChooseRoleBinding.inflate(layoutInflater)

    override fun onClick(view: View) {

    }
}