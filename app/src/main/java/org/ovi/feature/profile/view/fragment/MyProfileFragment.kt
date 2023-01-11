package org.ovi.feature.profile.view.fragment

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.view.Window
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatTextView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.ovi.R
import org.ovi.base.BaseFragment
import org.ovi.databinding.FragmentMyProfileBinding
import org.ovi.feature.auth.view.LoginActivity
import org.ovi.feature.auth.viewmodel.AuthViewModel
import org.ovi.feature.profile.model.DataProfile
import org.ovi.feature.profile.model.GetProfileResponse
import org.ovi.feature.profile.view.activity.EditProfileActivity
import org.ovi.feature.profile.view.activity.RETURN_RESULT
import org.ovi.feature.profile.viewmodel.ProfileViewModel
import org.ovi.network.ResultOf
import org.ovi.util.extensions.*
import org.ovi.widget.OviSnackBar
import java.lang.ref.WeakReference
import java.util.*


class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>() {

    private val vm: ProfileViewModel by viewModel { parametersOf(-1) }
    private val authVM: AuthViewModel by viewModel()
    private lateinit var profileResponse: GetProfileResponse

    override fun onClick(view: View) {
        with(binding) {
            when (view) {
                incContact.lLayTop -> {
                    if (incContact.lLayDrop.isVisible()) {
                        incContact.vwBtm.show()
                        incContact.lLayDrop.gone()
                        incContact.ivArrow.rotateClockWise()

                    } else {
                        incContact.vwBtm.gone()
                        incContact.lLayDrop.show()
                        incContact.ivArrow.rotateAntiClockWise()
                    }
                }
                incAgency.lLayTop -> {
                    if (incAgency.lLayDrop.isVisible()) {
                        incAgency.vwBtm.show()
                        incAgency.lLayDrop.gone()
                        incAgency.ivArrow.rotateClockWise()

                    } else {
                        incAgency.vwBtm.gone()
                        incAgency.lLayDrop.show()
                        incAgency.ivArrow.rotateAntiClockWise()
                    }
                }
                incOtherDetails.lLayTop -> {
                    if (incOtherDetails.lLayDrop.isVisible()) {
                        incOtherDetails.vwBtm.show()
                        incOtherDetails.lLayDrop.gone()
                        incOtherDetails.ivArrow.rotateClockWise()

                    } else {
                        incOtherDetails.vwBtm.gone()
                        incOtherDetails.lLayDrop.show()
                        incOtherDetails.ivArrow.rotateAntiClockWise()
                    }
                }
                incAddress.lLayTop -> {
                    if (incAddress.lLayDrop.isVisible()) {
                        incAddress.vwBtm.show()
                        incAddress.lLayDrop.gone()
                        incAddress.ivArrow.rotateClockWise()

                    } else {
                        incAddress.vwBtm.gone()
                        incAddress.lLayDrop.show()
                        incAddress.ivArrow.rotateAntiClockWise()
                    }
                }
                ivEdit -> {
                    activityResultLauncher.launch(
                        EditProfileActivity.createIntent(
                            WeakReference<Activity>(
                                requireActivity()
                            ), profileResponse
                        )
                    )
                }
                tvDelete -> {
                    alertDialogue()
                }
            }
        }
    }

    override fun bindViewEvents() {
        super.bindViewEvents()

        binding.incContact.lLayTop.setOnClickListener(onClickListener)
        binding.incAgency.lLayTop.setOnClickListener(onClickListener)
        binding.incOtherDetails.lLayTop.setOnClickListener(onClickListener)
        binding.incAddress.lLayTop.setOnClickListener(onClickListener)
        binding.ivEdit.setOnClickListener(onClickListener)
    }

    override fun getViewBinding() = FragmentMyProfileBinding.inflate(layoutInflater)

    override fun setupView() {
//        callApi()
        binding.tvDelete.setOnClickListener(onClickListener)
        setUnderLineText()
    }

    private fun callApi() {
        vm.getProfile()
    }

    private fun setUnderLineText() {

        val text = getString(R.string.delete_account_upper)
        val spanString = SpannableString(text)
        spanString.setSpan(UnderlineSpan(), 0, spanString.length, 0)
        binding.tvDelete.text = spanString
    }

    override fun bindViewModel() {
        bindDelete()
        vm.getProfile bindTo {
            when (it) {
                is ResultOf.Progress -> {
                    if (it.loading) {
                        binding.progressView.show()
                        binding.scrollView.gone()
                    } else {
                        binding.progressView.gone()
                        binding.scrollView.show()
                    }
                }
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE)
//                    Log.d(TAG, "bindViewModel: ${it.message}")
                    if (it.throwable?.message == "HTTP 502 Bad Gateway" || it.message == "No Internet Connection") {
                        (this.parentFragment as ProfileHolderFragment?)?.onApiError(true)
                    }
                }
                is ResultOf.Success -> {
                    (this.parentFragment as ProfileHolderFragment?)?.onApiError(false)
                    profileResponse = it.value
                    setUpValues(it.value.data)
//                    Log.d(TAG, "bindViewModel: $profileResponse")
                }
            }
        }
    }

    private fun bindDelete() {
        authVM.delete bindTo {
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
                    alertSuccessDeleteDialogue()
                }
            }
        }
    }


    private fun setUpValues(data: DataProfile?) {

        with(binding) {
            scrollView.show()
            incImage.ivUpload.gone()
            incImage.ivUserPic.setImageWithListener(data?.image_url) {
                if (it) {
                    incImage.tvCharacter?.show()
                    incImage.tvCharacter?.text = data?.name?.first().toString()
                } else
                    incImage.tvCharacter?.gone()
            }
        }

        with(binding.incContact) {
            stName.text = data?.name.toString()
            setEmails(data?.emails)
            stPhone.text = data?.phone.toString()
        }

        with(binding.incAgency) {
            if (data?.county == null && data?.member_user?.agency?.name == null)
                stAgency.editText?.setText("")
            else if (data.member_user?.agency?.name != null && data.member_user.agency.name != "")
                stAgency.editText?.setText(data.member_user.agency.name)
            else
                stAgency.editText?.setText(data.county.toString())

            stYouthCouncil.editText?.setText(data?.member_user?.youth_council_name)
        }

        with(binding.incOtherDetails) {
            if (pref.showOnBoard)
                incQuest.root.show()

            stRole.text = data?.role.toString().replace("_", " ")
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            stName.text = data?.name.toString()
            stDob.text = data?.birthday?.toString() ?: "N/A"
            stRace.text = data?.member_user?.race?.toString() ?: "N/A"
            stZip.text = data?.address?.zipcode ?: "N/A"
            stEthnicity.text = data?.member_user?.ethnicity?.toString() ?: "N/A"
            stGender.text = data?.gender?.toString() ?: "N/A"
            stShirtSize.text = data?.member_user?.shirt_size ?: "N/A"
            stEmail.text = data?.emails?.get(0)?.toString() ?: "N/A"
            stPhone.text = data?.phone?.toString() ?: "N/A"
            stJoined.text = data?.joined_on?.toString() ?: ""
            if (data?.user_groups?.isNotEmpty() == true)
                stGroup.text = data.user_groups[0]?.group?.name ?: ""
            else
                stGroup.text = "N/A"
            if (data?.member_user?.responses != null) {
                if (data.member_user.responses.isNotEmpty()) {
                    data.member_user.responses.forEach {
                        when (it?.question?.id) {
                            1 -> {
                                binding.incOtherDetails.incQuest.stEmployee.text =
                                    it.textValue.toString()
                            }
                            2 -> {
                                binding.incOtherDetails.incQuest.stEducation.text =
                                    it.textValue.toString()
                            }
                            3 -> {
                                binding.incOtherDetails.incQuest.stAdultInLife.text =
                                    it.textValue.toString()
                            }
//                            4 -> {
//                                binding.incOtherDetails.incQuest.stHomeless.text =
//                                    it.textValue.toString()
//                            }
//                            5 -> {
//                                binding.incOtherDetails.incQuest.stAlcohol.text =
//                                    it.textValue.toString()
//                            }
//                            6 -> {
//                                binding.incOtherDetails.incQuest.stJail.text =
//                                    it.textValue.toString()
//                            }
                            7 -> {
                                binding.incOtherDetails.incQuest.stChild.text =
                                    it.textValue.toString()
                            }
                            8 -> {
                                binding.incOtherDetails.incQuest.stMedical.text =
                                    it.textValue.toString()
                            }
//                            9 -> {
//                                binding.incOtherDetails.incQuest.stFinance.text =
//                                    it.textValue.toString()
//                            }
                            10 -> {
                                binding.incOtherDetails.incQuest.stFoster.text =
                                    it.textValue.toString()
                            }


                        }
                    }
                }
            }
        }

        with(binding.incAddress) {
            stBuilding.text = data?.address?.street ?: "N/A"
            stArea.text = data?.address?.city ?: "N/A"
            stState.text = data?.address?.state ?: "N/A"
            stCounty.text = data?.address?.county ?: "N/A"
            stZip.text = data?.address?.zipcode ?: "N/A"

        }

    }

    private fun setEmails(emails: List<String?>?) {
        with(binding.incContact) {
            stEmail.text = emails?.get(0).toString()
            when (emails?.size) {
                1 -> {
                    stEmail2.gone()
                    stEmail2.text = ""
                    stEmail3.gone()
                    stEmail3.text = ""
                }
                2 -> {
                    stEmail.label = "Email 1"
                    stEmail2.show()
                    stEmail2.text = emails[1].toString()
                    stEmail3.gone()
                }
                3 -> {
                    stEmail.label = "Email 1"
                    stEmail2.show()
                    stEmail2.text = emails[1].toString()
                    stEmail3.show()
                    stEmail3.text = emails[2].toString()
                }
            }
        }


    }

    private var activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data?.getBooleanExtra(RETURN_RESULT, false) == true)
                callApi()
        }
    }

    private fun alertDialogue() {
        with(Dialog(requireContext())) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.dialogue_delete_account)

            val tvCancel = findViewById<AppCompatTextView>(R.id.tvNo)
            val tvSignOut = findViewById<AppCompatTextView>(R.id.tvYes)

            tvCancel.setOnClickListener {
                dismiss()
            }
            tvSignOut.setOnClickListener {
                dismiss()
                authVM.delete()
//                (context as Activity).finish()
            }
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }

    private fun alertSuccessDeleteDialogue() {
        with(Dialog(requireContext())) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.dialogue_delete_success)

            val tvOkay = findViewById<AppCompatTextView>(R.id.tvOkay)

            tvOkay.setOnClickListener {
                dismiss()
                LoginActivity.present(requireContext())
                requireActivity().finishAffinity()
            }

            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }


}