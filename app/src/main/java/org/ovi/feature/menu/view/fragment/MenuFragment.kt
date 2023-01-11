package org.ovi.feature.menu.view.fragment


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.View
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.fragment.findNavController
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentMenuBinding
import org.ovi.feature.auth.view.LoginActivity
import org.ovi.feature.home.view.activity.HomeActivity
import org.ovi.feature.menu.view.adapter.MenuListAdapter
import org.ovi.feature.notification.view.NotificationActivity
import org.ovi.feature.profile.view.activity.ChangePasswordActivity
import org.ovi.util.extensions.verticalView
import java.lang.ref.WeakReference


class MenuFragment : BaseFragment<FragmentMenuBinding>() {

    override fun onClick(view: View) {

    }

    override fun getViewBinding() = FragmentMenuBinding.inflate(layoutInflater)

    override fun setupView() {

        binding.rvMenuList.apply {
            verticalView(context)
            adapter = MenuListAdapter(::callback)
        }
        setVersionText()
    }

    private fun setVersionText() {
        binding.tvVersion.text = getString(R.string.version, getString(R.string.version_code))
//        binding.tvVersion.setOnClickListener { SurveyActivity.present(requireContext()) }
    }

    private fun callback(menu: String) {
        when (menu) {

            getString(R.string.home) -> {
                findNavController().navigate(R.id.navigation_home)
                (requireActivity() as HomeActivity).setItemSelected(R.id.navigation_home)
            }
            getString(R.string.profile) -> {
                findNavController().navigate(R.id.navigation_profile)
                (requireActivity() as HomeActivity).moveToProfileOrEvent(0)
                (requireActivity() as HomeActivity).setItemSelected(R.id.navigation_profile)
            }
            getString(R.string.my_events) -> {
                findNavController().navigate(R.id.navigation_profile)
                (requireActivity() as HomeActivity).moveToProfileOrEvent(1)
                (requireActivity() as HomeActivity).setItemSelected(R.id.navigation_profile)
            }
            getString(R.string.change_password) -> {
                ChangePasswordActivity.present(WeakReference(requireActivity()))
            }
            getString(R.string.upcoming_events) -> {
                findNavController().navigate(R.id.navigation_event)
                (requireActivity() as HomeActivity).setItemSelected(R.id.navigation_event)
            }
            getString(R.string.notification) -> {
                NotificationActivity.present(WeakReference(requireActivity()))
            }
            getString(R.string.about_us) -> {
                startActivity(
                    Intent(
                        "android.intent.action.VIEW",
                        Uri.parse("https://selflesslovefoundation.org/")
                    )
                )
            }
            getString(R.string.sign_out) -> {
                alertDialogue()
            }
        }
    }

    private fun doOnLogOut() {
//        showToast(requireContext(),"log Out")
        pref.logOut()
        LoginActivity.present(requireContext())
        requireActivity().finishAffinity()
    }

    private fun alertDialogue() {
        with(Dialog(requireContext())) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.custom_signout_dialogue)

            val tvCancel = findViewById<AppCompatTextView>(R.id.tvCancel)
            val tvSignOut = findViewById<AppCompatTextView>(R.id.tvSignOut)

            tvCancel.setOnClickListener {
                dismiss()
            }
            tvSignOut.setOnClickListener {
                dismiss()
                doOnLogOut()
//                (context as Activity).finish()
            }
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }

    override fun bindViewModel() {

    }
}