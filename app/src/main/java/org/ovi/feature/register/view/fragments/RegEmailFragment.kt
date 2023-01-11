package org.ovi.feature.register.view.fragments

import android.text.InputType
import android.view.View
import android.widget.LinearLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentRegEmailBinding
import org.ovi.feature.register.model.RegKey
import org.ovi.feature.register.model.RegisterRequest
import org.ovi.feature.register.view.activity.RegisterActivity
import org.ovi.feature.register.viewmodel.ValidationViewModel
import org.ovi.network.ResultOf
import org.ovi.util.extensions.*
import org.ovi.widget.OviLabeledEditText
import org.ovi.widget.OviSnackBar

class RegEmailFragment : BaseFragment<FragmentRegEmailBinding>() {

    private val vm: ValidationViewModel by viewModel()

    private var count = 0
    val editList = mutableListOf<View>()
    override fun onClick(view: View) {
        when (view) {
            binding.tvAddEt -> {
                if (count < 2)
                    addEmail()
            }
            binding.btnNext -> {
                if (isValid())
                    checkEmail()
            }
        }
    }

    private fun onSuccess(exist: Boolean, duplicateEmails: List<String?>?) {
        if (!exist) {
            (requireActivity() as RegisterActivity).storeRegisterRequest(
                RegKey.EMAIL.value,
                getEmails()
            )
//                    Log.e(TAG, "onClick: "+ editList.get(0).findViewById<OviLabeledEditText>(R.id.etText).text)
            (requireActivity() as RegisterActivity).movePager()
        } else
            showSnackBar(
                duplicateEmails.toJsonString() + getString(R.string.email_already_exist),
                OviSnackBar.SnackType.FAILURE,
                binding.btnNext
            )
    }

    private fun checkEmail() {
        vm.validEmail(RegisterRequest(emails = getEmails().split("\n".toRegex()).toList()))
    }

    private fun getEmails(): String {
        val list = mutableListOf<String>()
        list.add(binding.etText.text.trim())
        val regex = "\n"
        if (count != 0) {
            if (count == 1) {
                if (editList[0]
                        .findViewById<OviLabeledEditText>(R.id.etText).text.trim().isNotEmpty()
                ) {
                    list.add(
                        editList[0]
                            .findViewById<OviLabeledEditText>(R.id.etText).text.trim()
                    )
                }
            } else {
                editList[0]
                    .findViewById<OviLabeledEditText>(R.id.etText).text.trim().let {
                        if (it.isNotEmpty())
                            list.add(
                                it
                            )
                    }
                editList[1]
                    .findViewById<OviLabeledEditText>(R.id.etText).text.trim().let {
                        if (it.isNotEmpty())
                            list.add(
                                it
                            )
                    }
            }
        }
        return list.joinToString(regex)
    }

    override fun getViewBinding() = FragmentRegEmailBinding.inflate(layoutInflater)

    override fun setupView() {
        binding.tvAddEt.setOnClickListener(onClickListener)
        binding.btnNext.setOnClickListener(onClickListener)
    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        with(binding) {
            tvTitle.text = getString(R.string.please_enter_email)
            tvAddEt.show()
            etText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
            etText.label = "Email"
            binding.btnNext.setOnClickListener(onClickListener)

        }

    }

    private fun addEmail() {
        val ctx = binding.root.context
        val layParam: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layParam.topMargin = 20.px
        val view = ctx.getInflatedLayout(R.layout.ovi_edittext)
        val edtText = view?.findViewById<OviLabeledEditText>(R.id.etText)
        binding.lLay.addView(view, layParam)
        editList.add(view as View)
        count++
        if (count == 2)
            binding.tvAddEt.gone()
    }

    override fun bindViewModel() {

        vm.valid bindTo {
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
                    it.value.data?.exists?.let { it1 ->
                        onSuccess(
                            it1,
                            it.value.data.duplicateEmails
                        )
                    }
                }
            }
        }

    }

    private fun isValid(): Boolean {

        when {
            binding.etText.text.isEmpty() -> {
                binding.etText.setError("Email id is required")
                return false
            }
            !binding.etText.text.checkEmail() -> {
                binding.etText.setError("Email id is not valid")
                return false
            }
        }

        when (editList.size) {
            1 -> {
                if (editList.get(0)
                        .findViewById<OviLabeledEditText>(R.id.etText).text.isNotEmpty()
                ) {
                    if (!
                        editList.get(0)
                            .findViewById<OviLabeledEditText>(R.id.etText).text.checkEmail()

                    ) {
                        editList.get(0)
                            .findViewById<OviLabeledEditText>(R.id.etText)
                            .setError("Email id is not valid")
                        return false
                    }
                }
            }
            2 -> {
                if (editList.get(0)
                        .findViewById<OviLabeledEditText>(R.id.etText).text.isNotEmpty()
                ) {
                    if (!editList.get(0)
                            .findViewById<OviLabeledEditText>(R.id.etText).text.checkEmail()
                    ) {
                        editList.get(0)
                            .findViewById<OviLabeledEditText>(R.id.etText)
                            .setError("Email id is not valid")
                        return false
                    }
                }

                if (editList.get(1)
                        .findViewById<OviLabeledEditText>(R.id.etText).text.isNotEmpty()
                ) {
                    if (!
                        editList.get(1)
                            .findViewById<OviLabeledEditText>(R.id.etText).text
                            .checkEmail()
                    ) {
                        editList.get(1)
                            .findViewById<OviLabeledEditText>(R.id.etText)
                            .setError("Email id is not valid")
                        return false
                    }
                }
            }
        }
        return true
    }

}