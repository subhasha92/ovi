package org.ovi.feature.register.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.ovi.feature.register.model.RowsItem
import org.ovi.feature.register.view.fragments.*
import org.ovi.feature.survey.model.QuestionType

class ViewPagerRegAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val showOnBoard: Boolean,
    private val dataRowsItem: MutableList<RowsItem?>?
) :
    FragmentStateAdapter(fm, lifecycle) {


    override fun getItemCount() = if (showOnBoard) 9 + dataRowsItem?.size!! else 9

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RegEnterNameFragment()
            1 -> RegDobFragment()
            2 -> RegEnterRaceFragment()
            3 -> RegEthnicityFragment()
            4 -> RegGenderFragment()
            5 -> RegEmailFragment()
            6 -> RegPhoneFragment()
            7 -> RegZipCodeFragment()
            8 -> RegConfirmAddresFragment()
//            9 -> RegEmployedFragment()
//            10 -> RegEducationFragment()
//            11 -> RegAdultInLifeFragment()
////            12 -> RegHomelessFragment()
////            13 -> RegAlcoholAssesmentFragment()
////            14 -> RegJailFragment()
//            12 -> RegChildFragment()
//            13 -> RegMedicalFragment()
////            17 -> RegFinancialFragment()
//            14 -> RegFosterCareFragment()
            else -> {
                when (dataRowsItem?.get(position - 9)?.type) {
                    QuestionType.YES_OR_NO.type -> {
                        val frg = RegEmployedFragment()
                        dataRowsItem[position - 9]?.position?.toInt()?.let { frg.setPosition(it) }
                        return frg
                    }
                    QuestionType.DROPDOWN.type -> {
                        val frg = RegEducationFragment()
                        dataRowsItem[position - 9]?.position?.toInt()?.let { frg.setPosition(it) }
                        return frg

                    }
                    else -> {
                        RegEmployedFragment()
                    }
                }
            }
        }
    }
}