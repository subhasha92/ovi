package org.ovi.feature.register.view.fragments

import android.annotation.SuppressLint
import android.view.View
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentRegDobBinding
import org.ovi.feature.register.model.RegKey
import org.ovi.feature.register.view.activity.RegisterActivity
import org.ovi.util.DatePicker
import org.ovi.util.extensions.getIsoDateFormat

class RegDobFragment : BaseFragment<FragmentRegDobBinding>() {


    @SuppressLint("SimpleDateFormat")
    override fun onClick(view: View) {
        when (view) {
            binding.btnNext -> {

                if (binding.etDate.text.isNotEmpty()) {

                    (requireActivity() as RegisterActivity).storeRegisterRequest(
                        RegKey.BIRTHDAY.value,
                        binding.etDate.text.getIsoDateFormat()!!
                    )
                    (requireActivity() as RegisterActivity).movePager()
                } else {
                    binding.etDate.setError("Date is required")
                }
            }
            binding.tvSkip -> {
                (requireActivity() as RegisterActivity).movePager()
            }
        }

    }

    override fun getViewBinding() = FragmentRegDobBinding.inflate(layoutInflater)

    override fun setupView() {
        binding.btnNext.setOnClickListener(onClickListener)
        binding.tvSkip.setOnClickListener(onClickListener)
    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        binding.etDate.setOnIconClickListener {
            binding.etDate.setError(null)
            val role= (requireActivity() as RegisterActivity).getRole()
            DatePicker.dateSetListener(binding.etDate, 1,role)
        }
        binding.etDate.setEnable(false)
    }

    override fun bindViewModel() {

    }
}