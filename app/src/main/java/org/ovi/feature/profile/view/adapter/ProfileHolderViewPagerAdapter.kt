package org.ovi.feature.profile.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.ovi.feature.profile.view.fragment.EventTrackFragment
import org.ovi.feature.profile.view.fragment.MyProfileFragment

class ProfileHolderViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    var fragment:Fragment?=null

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{fragment= MyProfileFragment()
                fragment as MyProfileFragment
            }
            1->{fragment= EventTrackFragment()
                fragment as EventTrackFragment
            }
            else -> {
                fragment!!
            }
        }
    }

    @JvmName("getFragment1")
    fun getFragment(): Fragment? {
        return fragment
    }


}