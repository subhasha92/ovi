package org.ovi.feature.profile.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.ovi.R
import org.ovi.base.BaseActivity
import org.ovi.data.local.DataUtil
import org.ovi.data.local.PopupModel
import org.ovi.databinding.ActivityEditProfileBinding
import org.ovi.feature.profile.model.*
import org.ovi.feature.profile.viewmodel.FileUploadViewModel
import org.ovi.feature.profile.viewmodel.ProfileViewModel
import org.ovi.feature.register.model.ZipcodeResponse
import org.ovi.feature.register.viewmodel.MasterViewModel
import org.ovi.network.ResultOf
import org.ovi.util.DatePicker.dateSetListener
import org.ovi.util.extensions.*
import org.ovi.widget.IDropDownListener
import org.ovi.widget.OviDropDown
import org.ovi.widget.OviSnackBar
import org.ovi.widget.OviSwitchEdit
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

const val PROFILE_DATA = "profile_responses"
const val RETURN_RESULT = "return_result"

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>() {

    private val vm: ProfileViewModel by viewModel { parametersOf(-1) }

    private var zip = 1
    private var zipUpdate = false
    private var locUpdate = false
    private val masterVM: MasterViewModel by viewModel()
    private val uploadVM: FileUploadViewModel by viewModel()

    private lateinit var agency: AgencyResponse
    private lateinit var county: ZipcodeResponse
    private lateinit var groups: GroupsResponse

    private var imageUri: Uri? = null

    private var imageLink: String? = null


    companion object {
        fun present(
            activityWeakReference: WeakReference<Activity>,
            profileResponse: GetProfileResponse
        ) {
            activityWeakReference.get()?.startActivity(
                Intent(
                    activityWeakReference.get(),
                    EditProfileActivity::class.java
                )
                    .putExtra(PROFILE_DATA, profileResponse)
            )
        }

        fun createIntent(
            activityWeakReference: WeakReference<Activity>,
            profileResponse: GetProfileResponse? = null
        ): Intent {
            return Intent(
                activityWeakReference.get(),
                EditProfileActivity::class.java
            ).putExtra(
                PROFILE_DATA, profileResponse
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getAgency()
        masterVM.getCounties()
        masterVM.getRace()
        masterVM.getEthnicity()
        masterVM.getGroups()
    }

    private val data by lazy {
        intent.getParcelableExtra(PROFILE_DATA) as GetProfileResponse?
    }

    private lateinit var editTextList: List<OviSwitchEdit>
    private lateinit var ddList: List<OviDropDown>
    private lateinit var dd10Question: List<OviDropDown>

    override fun setupView() {
        vm.getProfile()

        binding.btnDelete.setOnClickListener(onClickListener)
        editTextList = getETList()
        dd10Question = set10Question()
        editTextList.forEach {
            it.setEditModeVisiblity(false)
        }
        binding.incContact.stEmail2.setEditModeVisiblity(false)
        binding.incContact.stEmail3.setEditModeVisiblity(false)
        binding.incContact.stEmail2.show()
        binding.incContact.stEmail3.show()
        binding.incContact.stEmail2.editText?.setText("")
        binding.incContact.stEmail3.editText?.setText("")

        binding.incContact.stPhone.inputLayout.prefixText = getString(R.string.prefix_number)
        binding.incContact.stPhone.setMaxLength(10)
        setDDValues()
        setTextValue()


        binding.incAddress.stZip.enableAutoComplete(true)
        binding.incAddress.stZip.autoComplete.label = "Zipcode"
        binding.incAddress.stZip.autoComplete.setMaxLength(5)
        binding.incAddress.stZip.autoComplete.addTextChangeListener(
            1,
            1,
            ::callBack,
            ::callbackLocation,
            ::resetLocationField
        )

        binding.incOtherDetails.stZip.enableAutoComplete(true)
        binding.incOtherDetails.stZip.autoComplete.label = "Zipcode"
        binding.incOtherDetails.stZip.autoComplete.setMaxLength(5)
        binding.incOtherDetails.stZip.autoComplete.addTextChangeListener(
            1,
            2,
            ::callBack,
            ::callbackLocation,
            ::resetLocationField
        )

//        binding.incAddress.stCounty.inputLayout.editText?.isEnabled = false
//        binding.incAddress.stState.inputLayout.editText?.isEnabled = false
//        binding.incAddress.stArea.inputLayout.editText?.isEnabled = false


        binding.incOtherDetails.etJoinedOn.setEnable(false)
        binding.incOtherDetails.etDob.setEnable(false)

        binding.incAddress.stArea.setLabelTextColor(R.color.text_color_login, -1)

        if (pref.showOnBoard)
            binding.incOtherDetails.incQuest.root.show()


    }

    fun callBack(zipcode: String?, zip: Int) {
        this.zip = zip
        if (zip == 1)
            zipUpdate = true
        locUpdate = true
        masterVM.getZips(zipcode)
    }

    private fun callbackLocation(zipcode: String?) {
        if (zip == 1) {
            if (locUpdate) {
                locUpdate = false
                getLocations(zipcode)
            }
        } else {
            zip = 1
            binding.incAddress.stZip.editText?.setText(zipcode)
        }
        if (zipUpdate) {
            zipUpdate = false
            binding.incOtherDetails.stZip.editText?.setText(zipcode)
        }
    }

    private fun getLocations(zipcode: String?) {
        masterVM.getLocation(zipcode)
    }

    private fun set10Question(): List<OviDropDown> {
        with(binding.incOtherDetails.incQuest) {
            return listOf(
                ddEmployee,
                ddEducation,
                ddAdultInLife,
//                ddHomeless,
//                ddAlcohol,
//                ddJail,
                ddChild,
                ddMedical,
//                ddFinance,
                ddFoster
            )
        }
    }

    private fun getDDList(): List<OviDropDown> {
        with(binding)
        {
            return arrayListOf(
                incOtherDetails.ddRace,
                incOtherDetails.incQuest.ddFoster,
//                incOtherDetails.incQuest.ddFinance,
                incOtherDetails.incQuest.ddChild,
                incOtherDetails.incQuest.ddMedical,
//                incOtherDetails.incQuest.ddJail,
//                incOtherDetails.incQuest.ddAlcohol,
//                incOtherDetails.incQuest.ddHomeless,
                incOtherDetails.incQuest.ddAdultInLife,
                incOtherDetails.incQuest.ddEducation,
                incOtherDetails.incQuest.ddEmployee,
                incOtherDetails.ddEthnicity,
                incOtherDetails.ddShirtSize,
                incOtherDetails.ddGender,
                incOtherDetails.ddGroup,
                incOtherDetails.ddRole
            )
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun setTextValue() {
        loadImage(data?.data?.image_url)

        with(binding.incContact) {
            stName.editText?.setText(data?.data?.name)
            setEmails()
            stPhone.editText?.setText(data?.data?.phone?.takeLast(10))
        }
        with(binding.incAgency) {
            if (data?.data?.county == null && data?.data?.member_user?.agency?.name == null)
                stAgency.editText?.setText("")
            else if (data?.data?.member_user?.agency?.name != null && data?.data?.member_user?.agency?.name != "")
                stAgency.editText?.setText(data?.data?.member_user?.agency?.name)
            else
                stAgency.editText?.setText(data?.data?.county.toString())

            stYouthCouncil.editText?.setText(data?.data?.member_user?.youth_council_name)
        }
        with(binding.incOtherDetails) {
            if (data?.data?.birthday != null)
                etDob.editText?.setText(data?.data?.birthday.toString().setIsoDateFormat())
            ddGender.setText(data?.data?.gender)
            ddShirtSize.setText(data?.data?.member_user?.shirt_size)
            stZip.editText?.setText(if (data?.data?.address?.zipcode != null) data?.data?.address?.zipcode.toString() else "")
            ddRace.setText(data?.data?.member_user?.race)
            ddEthnicity.setText(data?.data?.member_user?.ethnicity)
            if (data?.data?.user_groups?.isNotEmpty() == true)
                ddGroup.setText(data?.data?.user_groups?.get(0)?.group?.name)
//            ddGroup.disableDropDown()
            if (data?.data?.joined_on != null)
                etJoinedOn.editText?.setText(data?.data?.joined_on.toString().setIsoDateFormat())
            else {
                etJoinedOn.editText?.setText(SimpleDateFormat("dd MMM, yyyy").format(Date()))
            }
            ddRole.setText(data?.data?.role.toString().replace("_", " ").capitalizeWords())
            if (data?.data?.member_user?.responses != null) {
                if (data?.data?.member_user?.responses?.isNotEmpty() == true) {
                    data?.data?.member_user?.responses?.forEach {
                        when (it?.question?.id) {
                            1 -> {
                                binding.incOtherDetails.incQuest.ddEmployee.setText(it.textValue.toString())
                            }
                            2 -> {
                                binding.incOtherDetails.incQuest.ddEducation.setText(it.textValue.toString())
                            }
                            3 -> {
                                binding.incOtherDetails.incQuest.ddAdultInLife.setText(it.textValue.toString())
                            }
//                            4 -> {
//                                binding.incOtherDetails.incQuest.ddHomeless.setText(it.textValue.toString())
//                            }
//                            5 -> {
//                                binding.incOtherDetails.incQuest.ddAlcohol.setText(it.textValue.toString())
//                            }
//                            6 -> {
//                                binding.incOtherDetails.incQuest.ddJail.setText(it.textValue.toString())
//                            }
                            7 -> {
                                binding.incOtherDetails.incQuest.ddChild.setText(it.textValue.toString())
                            }
                            8 -> {
                                binding.incOtherDetails.incQuest.ddMedical.setText(it.textValue.toString())
                            }
//                            9 -> {
//                                binding.incOtherDetails.incQuest.ddFinance.setText(it.textValue.toString())
//                            }
                            10 -> {
                                binding.incOtherDetails.incQuest.ddFoster.setText(it.textValue.toString())
                            }
                        }
                    }
                }
            }
        }
        with(binding.incAddress) {
            stBuilding.editText?.setText(data?.data?.address?.street)
            stArea.editText?.setText(data?.data?.address?.city)
            stArea.text = data?.data?.address?.city.toString()
//            stState.editText?.setText(data?.data?.address?.state)
            stState.text = data?.data?.address?.state.toString()
            stCounty.text = data?.data?.address?.county.toString()
            stZip.editText?.setText(if (data?.data?.address?.zipcode != null) data?.data?.address?.zipcode.toString() else "")
        }
    }

    private fun setEmails() {
        with(binding.incContact) {
            val emails = data?.data?.emails
            stEmail.label = "Email 1"
            stEmail.editText?.setText(emails?.get(0))
            when (emails?.size) {
                2 -> {
                    stEmail2.show()
                    stEmail2.editText?.setText(emails[1])
                }
                3 -> {
                    stEmail2.show()
                    stEmail2.editText?.setText(emails[1])
                    stEmail3.show()
                    stEmail3.editText?.setText(emails[2])
                }
                else -> {}
            }
        }

    }

    private fun setDDValues() {
        with(binding.incOtherDetails) {
            ddRole.loadPopupItems(DataUtil.getChooseRoles().map { it.content })
            ddShirtSize.loadPopupItems(DataUtil.getShirtSize().map { it.content })
            ddGender.loadPopupItems(DataUtil.getGender().map { it.content })
            incQuest.ddEmployee.loadPopupItems(DataUtil.getYesNo().map { it.content })
            incQuest.ddEducation.loadPopupItems(DataUtil.getEducation().map { it.content })
            incQuest.ddAdultInLife.loadPopupItems(DataUtil.getYesNo().map { it.content })
//            incQuest.ddHomeless.loadPopupItems(DataUtil.getYesNo().map { it.content })
//            incQuest.ddAlcohol.loadPopupItems(DataUtil.getYesNo().map { it.content })
//            incQuest.ddJail.loadPopupItems(DataUtil.getYesNo().map { it.content })
            incQuest.ddChild.loadPopupItems(DataUtil.getYesNo().map { it.content })
            incQuest.ddMedical.loadPopupItems(DataUtil.getYesNo().map { it.content })
//            incQuest.ddFinance.loadPopupItems(DataUtil.getYesNo().map { it.content })
            incQuest.ddFoster.loadPopupItems(DataUtil.getYesNo().map { it.content })
        }
    }

    override fun bindViewEvents() {
        super.bindViewEvents()
        binding.incContact.lLayTop.setOnClickListener(onClickListener)
        binding.incAgency.lLayTop.setOnClickListener(onClickListener)
        binding.incOtherDetails.lLayTop.setOnClickListener(onClickListener)
        binding.incAddress.lLayTop.setOnClickListener(onClickListener)
        binding.tvCancel.setOnClickListener(onClickListener)
        binding.btnSave.setOnClickListener(onClickListener)
        setClickEvent()
    }

    private fun setClickEvent() {
        with(binding.incOtherDetails) {
            etDob.setOnIconClickListener {
                dateSetListener(etDob, 1, ddRole.getText())
            }
//            etJoinedOn.setOnIconClickListener {
//                dateSetListener(etJoinedOn, 0)
//            }
            ddList = getDDList()
            ddList.forEach {
                it.iDropDownListener = ddListener
            }

            binding.incImage.ivUpload.setOnClickListener(onClickListener)
        }
    }

    private val ddListener = object : IDropDownListener {
        override fun onDDClicked() {
        }

        override fun onOptionClicked(view: View, model: PopupModel) {
//            Log.e(TAG, "onOptionClicked: ${view} : ${binding.incOtherDetails.ddGender.binding.root}", )
            when (view) {
                binding.incOtherDetails.ddGender.binding.root -> {
                    if (model.title == "Other") {
                        binding.incOtherDetails.etOtherGender.show()
                    } else
                        binding.incOtherDetails.etOtherGender.gone()
                }
                binding.incOtherDetails.ddShirtSize.binding.root -> {
                    if (model.title == "Other") {
                        binding.incOtherDetails.etOtherShirt.show()
                    } else
                        binding.incOtherDetails.etOtherShirt.gone()
                }
            }
        }
    }

    private fun getETList(): List<OviSwitchEdit> {
        with(binding) {
            return arrayListOf(
                incContact.stName,
                incContact.stEmail,
                incContact.stPhone,
                incAgency.stAgency,
                incAgency.stYouthCouncil,
                incAddress.stBuilding,
//                incAddress.stArea,
//                incAddress.stState,
//                incAddress.stCounty,
                incAddress.stZip
            )
        }
    }

    override fun bindViewModel() {
        bindProfile()
        bindAgency()
        bindCounty()
        bindRace()
        bindEthnicity()
        bindUpload()
        bindZipCode()
        bindLocation()
        bindGroups()
    }

    private fun bindGroups() {
        masterVM.groups bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE)
                }
                is ResultOf.Progress -> {}
                is ResultOf.Success -> {
                    groups = it.value
                    it.value.data?.rows?.map { it?.name }
                        ?.let { it1 -> binding.incOtherDetails.ddGroup.loadPopupItems(it1 as List<String>) }
                }
            }
        }
    }


    private fun bindLocation() {
        masterVM.location bindTo {
            when (it) {
                is ResultOf.Empty -> {
                }
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
                    if (it.value.data?.rows?.isEmpty() == false) {
                        binding.incAddress.stArea.text = it.value.data.rows[0]?.city.toString()
                        binding.incAddress.stState.text = it.value.data.rows[0]?.state.toString()
                        binding.incAddress.stCounty.text = it.value.data.rows[0]?.county.toString()
                    } else {
                        binding.incAddress.stArea.text = "null"
                        binding.incAddress.stState.text = "null"
                        binding.incAddress.stCounty.text = "null"
                    }
                }
            }
        }
    }

    private fun resetLocationField() {
        binding.incAddress.stArea.text = ""
        binding.incAddress.stState.text = ""
        binding.incAddress.stCounty.text = ""
    }

    private fun bindZipCode() {
        masterVM.ZipCodes bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE)
                }
                is ResultOf.Progress -> {}
                is ResultOf.Success -> {
                    if (zip == 1)
                        binding.incAddress.stZip.autoComplete.setPopUpList(
                            this@EditProfileActivity,
                            it.value.data as List<String>
                        )
                    else
                        binding.incOtherDetails.stZip.autoComplete.setPopUpList(
                            this@EditProfileActivity,
                            it.value.data as List<String>
                        )
                }
            }
        }
    }


    private fun bindUpload() {
        uploadVM.fileUpload bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
//                    Log.e(TAG, "bindUpload: ${it.message}")
                }
                is ResultOf.Progress -> {
                    if (it.loading) binding.progressView.show() else binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    loadImage(it.value)
                    imageLink = it.value
//                    binding.incImage.ivUserPic.setImageURI(imageUri)
                    showSnackBar("Success", OviSnackBar.SnackType.SUCCESS, binding.btnSave)
                }
            }
        }
    }

    fun loadImage(url: String?) {
        binding.incImage.ivUserPic.setImageWithListener(url) {
            if (it) {
                binding.incImage.tvCharacter?.show()
                binding.incImage.tvCharacter?.text = data?.data?.name?.first().toString()
            } else
                binding.incImage.tvCharacter?.gone()
        }
    }

    private fun bindEthnicity() {
        masterVM.ethnicity bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {}
                is ResultOf.Progress -> {}
                is ResultOf.Success -> {
                    binding.incOtherDetails.ddEthnicity.loadPopupItems(it.value.data?.rows?.map { it?.value }
                        ?.toList() as List<String>)
                }
            }
        }
    }

    private fun bindRace() {
        masterVM.race bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {}
                is ResultOf.Progress -> {}
                is ResultOf.Success -> {
                    binding.incOtherDetails.ddRace.loadPopupItems(it.value.data?.rows?.map { it?.value }
                        ?.toList() as List<String>)
                }
            }
        }
    }

    private fun bindCounty() {
        masterVM.counties bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {}
                is ResultOf.Progress -> {}
                is ResultOf.Success -> {
                    county = it.value
                    binding.incAddress.stCounty.oviDropDown.iDropDownListener = ddListener
                    binding.incAddress.stCounty.oviDropDown.loadPopupItems(county.data as List<String>)
                }
            }
        }
    }

    private fun bindAgency() {
        vm.agency bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {}
                is ResultOf.Progress -> {}
                is ResultOf.Success -> {
                    agency = it.value
                }
            }
        }
    }

    private fun bindProfile() {
        vm.putProfile bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE, binding.btnSave)
//                    Log.e(TAG, "bindProfile: ${it.message}")

                }
                is ResultOf.Progress -> {
                    if (it.loading) binding.progressView.show() else binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    showSnackBar("Success", OviSnackBar.SnackType.SUCCESS, binding.btnSave)
                    val result = Intent()
                    result.putExtra(RETURN_RESULT, true)
                    setResult(RESULT_OK, result)
                    finish()
                }
            }
        }
    }

    override fun getViewBinding() = ActivityEditProfileBinding.inflate(layoutInflater)

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!
                    imageUri = fileUri
                    uploadVM.uploadFile(fileUri, true)
//                    newVM.uploadFile(fileUri,true)
                }
                ImagePicker.RESULT_ERROR -> {
                    showToast(ImagePicker.getError(data))
                }
                else -> {
                    showToast("Task Cancelled")
                }
            }
        }

    override fun onClick(view: View) {

        with(binding) {
            when (view) {
                incImage.ivUpload -> {
                    ImagePicker.with(this@EditProfileActivity)
                        //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(
                            1080,
                            1080
                        )    //Final image resolution will be less than 1080 x 1080(Optional)
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                }
                incContact.lLayTop -> {
                    checkVisibility(incContact.root, 0)
                }
                incAgency.lLayTop -> {
                    checkVisibility(incAgency.root, 0)
                }
                incOtherDetails.lLayTop -> {
                    checkVisibility(incOtherDetails.root, 0)
                }
                incAddress.lLayTop -> {
                    checkVisibility(incAddress.root, 0)
                }
                tvCancel -> {
                    finish()
                }
                btnSave -> {
                    if (ifValid()) {
                        createRequest()
                    }
                }

            }
        }
    }


    private fun checkVisibility(view: View, i: Int) {
        with(binding) {
            when (view) {
                incAddress.root -> {
                    if (i == 1) {
                        incAddress.vwBtm.gone()
                        incAddress.lLayDrop.show()
                        incAddress.ivArrow.rotateAntiClockWise()
                    } else
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

                incContact.root -> {
                    if (i == 1) {
                        incContact.vwBtm.gone()
                        incContact.lLayDrop.show()
                        incContact.ivArrow.rotateAntiClockWise()
                    } else
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
                incAgency.root -> {
                    if (i == 1) {
                        incAgency.vwBtm.gone()
                        incAgency.lLayDrop.show()
                        incAgency.ivArrow.rotateAntiClockWise()
                    } else
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
                incOtherDetails.root -> {
                    if (i == 1) {
                        incOtherDetails.vwBtm.gone()
                        incOtherDetails.lLayDrop.show()
                        incOtherDetails.ivArrow.rotateAntiClockWise()
                    } else
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
            }
        }
    }

    private fun createRequest() {

        with(binding) {
            val request = EditProfileRequest(
                name = incContact.stName.text.toString(),
                emails = getEmails(),
                role = incOtherDetails.ddRole.getText().replace(" ", "_").lowercase(),
            )

            //check Groups
            (incOtherDetails.ddGroup.getText().isNotEmpty())
            request.groups =
                listOf(groups.data?.rows?.find { it?.name == incOtherDetails.ddGroup.getText() }?.id)
            //check birthday
            if (incOtherDetails.etDob.text.isNotEmpty())
                request.birthday = incOtherDetails.etDob.text.getIsoDateFormat()
            //check race
            if (incOtherDetails.ddRace.getText().isNotEmpty())
                request.race = incOtherDetails.ddRace.getText()
            //check ethinicity
            if (incOtherDetails.ddEthnicity.getText().isNotEmpty())
                request.ethnicity = incOtherDetails.ddEthnicity.getText()
            //check gender
            if (incOtherDetails.ddGender.getText().isNotEmpty())
                request.gender =
                    if (incOtherDetails.ddGender.getText() == "Other") incOtherDetails.etOtherGender.text.toString() else incOtherDetails.ddGender.getText()
            //CHeck phone
            if (incContact.stPhone.text.trim().isNotEmpty())
                request.phone =
                    getString(R.string.prefix_number).plus(incContact.stPhone.text.trim())
            //Check Address
            val add = AddressEditProfile()
            if (incAddress.stBuilding.text.isNotEmpty())
                add.street = incAddress.stBuilding.text
            if (incAddress.stArea.text.isNotEmpty())
                add.city = incAddress.stArea.text
            if (incAddress.stState.text.isNotEmpty())
                add.state = incAddress.stState.text
            if (incAddress.stCounty.text.isNotEmpty())
                add.county = incAddress.stCounty.text
            if (incAddress.stZip.autoComplete.text.isNotEmpty()) {
                add.zipcode = incAddress.stZip.autoComplete.text
                request.address = add
            }

            //check county or agency
            if (incAgency.stAgency.text.isNotEmpty())
                request.county = incAgency.stAgency.text

            //check youth council
            if (incAgency.stYouthCouncil.text.isNotEmpty())
                request.youth_council_name = incAgency.stYouthCouncil.text

            //check shirt Size
            if (incOtherDetails.ddShirtSize.getText().isNotEmpty())
                request.shirt_size =
                    if (incOtherDetails.ddShirtSize.getText() == "Other") incOtherDetails.etOtherShirt.text.toString() else incOtherDetails.ddShirtSize.getText()

            if (imageLink != null)
                request.image_url = imageLink

            if (binding.incOtherDetails.incQuest.root.isVisible())
                request.answers = getListofAnswers()
//            Log.d(TAG, "createRequest: ${getListofAnswers()}")
//            Log.d(TAG, "createRequest: $request")
            vm.putProfile(request)
        }
    }

    private fun getEmails(): List<String?>? {
        val emails = mutableListOf<String>()
        with(binding.incContact) {
            emails.add(stEmail.text)
            if (stEmail2.text.isNotEmpty())
                emails.add(stEmail2.text)
            if (stEmail3.text.isNotEmpty())
                emails.add(stEmail3.text)
        }
        return emails
    }

    private fun getListofAnswers(): List<AnswersItem> {
        val answerList = mutableListOf<AnswersItem>()
        with(binding) {

            dd10Question.forEachIndexed { index, it ->
                when {
                    it.getText().isNotEmpty() -> answerList.add(AnswersItem(
                        question_id = data?.data?.member_user?.responses?.get(index)?.question?.id,
                        text_value = it.getText(),
                        choice_id = if (index == 1) DataUtil.getEducation()
                            .indexOfFirst {
                                it.content.equals(
                                    incOtherDetails.incQuest.ddEducation.getText(),
                                    true
                                )
                            }
                            .plus(1) else 1
                    ))
                }
            }
        }
        return answerList
    }

    private fun ifValid(): Boolean {

//        if (binding.incOtherDetails.etOtherGender.isVisible()) {
//            if (binding.incOtherDetails.etOtherGender.text?.isEmpty() == true) {
//                binding.incOtherDetails.etOtherGender.error = "Field can't be blank"
//                checkVisibility(binding.incOtherDetails.root, 1)
//                binding.incOtherDetails.etOtherGender.requestFocus()
//                return false
//            }
//        }
//        if (binding.incOtherDetails.etOtherShirt.isVisible()) {
//            if (binding.incOtherDetails.etOtherShirt.text?.isEmpty() == true) {
//                binding.incOtherDetails.etOtherShirt.error = "Field can't be blank"
//                checkVisibility(binding.incOtherDetails.root, 1)
//                binding.incOtherDetails.etOtherShirt.requestFocus()
//                return false
//            }
//        }

//        editTextList.forEach {
//            if (it.id == binding.incContact.stName.id)
//                if (it.editText?.text.toString().isEmpty() || it.editText?.text.toString() == " ") {
//                    checkVisibility(it.parent.parent as View, 1)
//                    it.setError("Field can't be blank")
//                    return false
//                }
//        }

        when {
            binding.incAddress.stArea.text == "null" -> {
                checkVisibility(binding.incAddress.root, 1)
                binding.incAddress.stArea.setError("Value cant be null")
                return false
            }

            binding.incAddress.stState.text == "null" -> {
                checkVisibility(binding.incAddress.root, 1)
                binding.incAddress.stState.setError("Value cant be null")
                return false
            }

            binding.incAddress.stCounty.text == "null" -> {
                checkVisibility(binding.incAddress.root, 1)
                binding.incAddress.stCounty.setError("Value cant be null")
                return false
            }

        }


        if (binding.incContact.stPhone.editText?.text?.isEmpty() == true) {
            checkVisibility(binding.incContact.root, 1)
            binding.incContact.stPhone.setError("Field can't be blank")
            return false
        }
        if (binding.incContact.stName.editText?.text?.isEmpty() == true) {
            checkVisibility(binding.incContact.root, 1)
            binding.incContact.stName.setError("Field can't be blank")
            return false
        }


        when {
            !binding.incContact.stEmail.text.checkEmail()
            -> {
                binding.incContact.stEmail.setError("Email id is not Valid")
                checkVisibility(binding.incContact.root, 1)
                return false
            }
            binding.incContact.stEmail2.text.isNotEmpty() -> {
                if (!binding.incContact.stEmail2.text.checkEmail()) {
                    checkVisibility(binding.incContact.root, 1)
                    binding.incContact.stEmail2.setError("Email id is not Valid")
                    return false
                }
                if (binding.incContact.stEmail3.text.isNotEmpty())
                    if (!binding.incContact.stEmail3.editText?.text.toString().checkEmail()) {
                        checkVisibility(binding.incContact.root, 1)
                        binding.incContact.stEmail3.setError("Email id is not Valid")
                        return false
                    }
            }
        }

//        if (binding.incOtherDetails.ddShirtSize.getText().isEmpty()) {
//            checkVisibility(binding.incOtherDetails.root, 1)
//            binding.incOtherDetails.ddShirtSize.setError("Field can't be blank")
//            return false
//        }
//
//        if (binding.incOtherDetails.etDob.text.toString().isEmpty()) {
//            checkVisibility(binding.incOtherDetails.root, 1)
//            binding.incOtherDetails.etDob.setError("Field can't be blank")
//            return false
//        }
//        if (binding.incOtherDetails.etJoinedOn.text.toString().isEmpty()) {
//            checkVisibility(binding.incOtherDetails.root, 1)
//            binding.incOtherDetails.etJoinedOn.setError("Field can't be blank")
//            return false
//        }

//        when {
//            binding.incContact.stPhone.text.isEmpty() -> {
//                binding.incContact.stPhone.setError("Phone number is required")
//                return false
//            }
//
//            binding.incContact.stPhone.text.trim().length < 10 -> {
//                binding.incContact.stPhone.setError("Phone number should be minimum 10 digit")
//                return false
//            }
//
//            binding.incContact.stPhone.text.trim().length in 11..12 -> {
//                binding.incContact.stPhone.setError("Phone number should be with international code")
//                return false
//            }
//
//        }

        return true
    }


}

