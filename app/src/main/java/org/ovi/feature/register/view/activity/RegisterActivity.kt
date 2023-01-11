package org.ovi.feature.register.view.activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ovi.base.BaseActivity
import org.ovi.databinding.ActivityRegisterBinding
import org.ovi.feature.profile.model.AnswersItem
import org.ovi.feature.register.model.OnBoardinResponse
import org.ovi.feature.register.model.PutOnBoardingResRequest
import org.ovi.feature.register.model.RegKey
import org.ovi.feature.register.model.RowsItem
import org.ovi.feature.register.view.adapter.ViewPagerRegAdapter
import org.ovi.feature.register.viewmodel.MasterViewModel
import org.ovi.feature.register.viewmodel.RegisterViewModel
import org.ovi.network.ResultOf
import org.ovi.util.extensions.gone


private const val ROLE="role"

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    private val vm: RegisterViewModel by viewModel()
    private val masterVM: MasterViewModel by viewModel()

    private val dataRowsItem = mutableListOf<RowsItem?>()
    private lateinit var onBoardingResponse: OnBoardinResponse

    private val onBoardinList = PutOnBoardingResRequest()
    val registerValue = HashMap<String, String>()

    val role by lazy {
        intent.getStringExtra(ROLE)
    }

    companion object {
        fun present(context: Context, choice_id: Int?, text_value: String) {
            context.startActivity(
                Intent(context, RegisterActivity::class.java).putExtra(
                    ROLE,
                    text_value
                )
            )
        }
    }

    fun movePager() {
        if (binding.viewPager.currentItem == binding.viewPager.adapter?.itemCount!! - 1)
            ReviewSubmitActivity.present(this@RegisterActivity, onBoardinList, registerValue)
        else
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
    }

    private fun goBackPager() {
        if (binding.viewPager.currentItem == 0)
            finish()
        else
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true)
    }

    override fun setupView() {
        onBoardinList.answers = mutableListOf()
        if (pref.showOnBoard)
            vm.getOnBoarding()
        registerValue.put(RegKey.ROLE.value, role.toString())
        binding.toolbar.setBackListener { goBackPager() }
        binding.toolbar.setEndIconListener { finish() }
        if (!pref.showOnBoard)
            setViewPager(dataRowsItem)
    }

    private fun setViewPager(dataRowsItem: MutableList<RowsItem?>?) {
        with(binding.viewPager) {
            adapter = ViewPagerRegAdapter(
                supportFragmentManager,
                lifecycle,
                pref.showOnBoard,
                dataRowsItem
            )
            isUserInputEnabled = false
            offscreenPageLimit = if (pref.showOnBoard) 9 + dataRowsItem?.size!! else 9
        }
        binding.vpIndicator.attachToPager(binding.viewPager)
    }

    override fun bindViewModel() {
        bindOnBoard()
    }

    fun getValue(i: Int): RowsItem {
        return dataRowsItem.find { it?.position == i }!!
    }

    @JvmName("getRole1")
    fun getRole():String{
        return role.toString()
    }

    private fun bindOnBoard() {
        vm.onBoard bindTo {
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
                    if (it.value.statusCode == 201) {
                        onBoardingResponse = it.value
                        val sorted = it.value.data?.rows?.sortedBy { it?.id }
                        dataRowsItem.addAll(sorted as MutableList)
                        setViewPager(dataRowsItem)
                    }
                }
                else -> {}
            }
        }
    }

    fun storeRegisterRequest(key: String, value: String) {
        registerValue.put(key, value)
//        Log.e(TAG, "storeRegisterRequest: $key : $value")
    }

    fun callOnBoardResponeAPi(answer: AnswersItem) {
        Log.e(TAG, "callOnBoardResponeAPi: $answer", )
        if (onBoardinList.answers?.isEmpty() == true)
            onBoardinList.answers?.add(answer)
        else {
            if (onBoardinList.answers?.filter { it?.question_id == answer.question_id }?.size == 1) {
                onBoardinList.answers?.find { it?.question_id == answer.question_id }?.choice_id =
                    answer.choice_id
                onBoardinList.answers?.find { it?.question_id == answer.question_id }?.text_value =
                    answer.text_value
            } else {
                onBoardinList.answers?.add(answer)
            }
        }

    }

    override fun getViewBinding() = ActivityRegisterBinding.inflate(layoutInflater)

    override fun onClick(view: View) {

    }

}