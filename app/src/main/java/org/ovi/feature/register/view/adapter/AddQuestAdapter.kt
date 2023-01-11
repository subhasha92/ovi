package org.ovi.feature.register.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.ovi.feature.register.view.fragments.*

class AddQuestAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {


    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0->QuestAgencyFragment()
            1->QuestCouncilFragment()
            2->QuestShirtSizeFragment()
            3->QuestUploadPicFragment()
//            4->QuestCurrentEventFragment()
            else -> QuestAgencyFragment()
        }
    }
}