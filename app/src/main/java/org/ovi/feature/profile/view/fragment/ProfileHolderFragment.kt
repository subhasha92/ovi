package org.ovi.feature.profile.view.fragment

import android.annotation.SuppressLint
import android.os.Handler
import android.view.View
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentProfileHolderBinding
import org.ovi.feature.profile.view.adapter.ProfileHolderViewPagerAdapter
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.show


class ProfileHolderFragment : BaseFragment<FragmentProfileHolderBinding>() {


    override fun onClick(view: View) {

    }

    fun onApiError(state: Boolean) {
        if (state) {
            binding.incErrorLayout.root.show()
            binding.frameLayout.gone()
            binding.incRadio.root.gone()
        } else {
            binding.incErrorLayout.root.gone()
            binding.frameLayout.show()
            binding.incRadio.root.show()
        }

    }

    override fun getViewBinding() = FragmentProfileHolderBinding.inflate(layoutInflater,null,false)

    override fun setupView() {

//        binding.incRadio.radioGroup.setOnCheckedChangeListener { group, checkedId ->
//            when (checkedId) {
//                binding.incRadio.radio1.id -> {
////                    if (binding.frameLayout.currentItem != 0)
//                    binding.frameLayout.currentItem = 0
//                }
//                binding.incRadio.radio2.id -> {
////                    if (binding.frameLayout.currentItem != 1)
//                    binding.frameLayout.currentItem = 1
//                }
//            }
//        }
    }

    private fun setViewPager() {
        binding.frameLayout.adapter =
            ProfileHolderViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.frameLayout.isUserInputEnabled = false
        binding.frameLayout.isSaveEnabled = false
        binding.frameLayout.offscreenPageLimit = 2
    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        binding.incRadio.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.incRadio.radio1.id -> {
//                    if (binding.frameLayout.currentItem != 0)
                    binding.frameLayout.currentItem = 0
                }
                binding.incRadio.radio2.id -> {
//                    if (binding.frameLayout.currentItem != 1)
                    binding.frameLayout.currentItem = 1
                    switchProEvent(1)
                }
            }
        }
        setViewPager()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun switchProEvent(type: Int) {
        if (binding.frameLayout.adapter == null)
            setViewPager()
        when (type) {
            0 -> {
                Handler().postDelayed({
                    binding.frameLayout.setCurrentItem(0, false)
                }, 300)
                binding.frameLayout.adapter?.notifyDataSetChanged()
                binding.frameLayout.currentItem = 0
                binding.incRadio.radio1.isChecked = true
//                Log.d(TAG, "switchProEvent: ${binding.frameLayout.currentItem}")
            }
            1 -> {
                Handler().postDelayed({
                    binding.frameLayout.setCurrentItem(1, false)
                }, 300)
                binding.frameLayout.adapter?.notifyDataSetChanged()
                binding.frameLayout.currentItem = 1
                binding.incRadio.radio2.isChecked = true

                val adapter = binding.frameLayout.adapter as ProfileHolderViewPagerAdapter
                if (adapter.getFragment() == null)
                    Handler().postDelayed({
                        val fragment = adapter.getFragment() as EventTrackFragment
                        fragment.refresh()
                    }, 200)
                else {
                    val fragment = adapter.getFragment() as EventTrackFragment
                    fragment.refresh()
                }

//                Log.d(TAG, "switchProEvent: ${binding.frameLayout.currentItem}")
            }
        }
    }

    override fun bindViewModel() {

    }
}