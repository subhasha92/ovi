package org.ovi.feature.register.view.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.InputType
import android.text.SpannableString
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ovi.R
import org.ovi.base.BaseActivity
import org.ovi.data.local.DataUtil
import org.ovi.data.local.PopupModel
import org.ovi.databinding.ActivityReviewSubmitBinding
import org.ovi.feature.profile.model.AnswersItem
import org.ovi.feature.register.model.*
import org.ovi.feature.register.viewmodel.MasterViewModel
import org.ovi.feature.register.viewmodel.RegisterViewModel
import org.ovi.network.ResultOf
import org.ovi.util.DatePicker
import org.ovi.util.extensions.*
import org.ovi.widget.*
import java.text.SimpleDateFormat
import java.util.*

const val ONBOARD = "onBoard"
const val REGISTER = "register"

class ReviewSubmitActivity : BaseActivity<ActivityReviewSubmitBinding>() {

    private val vm: RegisterViewModel by viewModel()
    private val masterVM: MasterViewModel by viewModel()

    private var editBtn = mutableListOf<View?>()
    private var editText = mutableListOf<View?>()
    private var ddDrop = mutableListOf<OviDropDown>()
    private var questions: OnBoardinResponse? = null
    var masterType: MasterDataType? = null
    var emailEdit: Boolean = false

    companion object {
        fun present(
            context: Context,
            onBoardinList: PutOnBoardingResRequest,
            registerValue: HashMap<String, String>,
        ) {
            val intent = Intent(context, ReviewSubmitActivity::class.java)
            intent.putExtra(ONBOARD, onBoardinList)
            intent.putExtra(REGISTER, registerValue)
            context.startActivity(intent)
        }
    }

    private val onBoardinList: PutOnBoardingResRequest? by lazy {
        intent.getParcelableExtra(ONBOARD) as PutOnBoardingResRequest?
    }

    private val registerList: HashMap<String, String>? by lazy {
        intent.getSerializableExtra(REGISTER) as HashMap<String, String>?
    }


    override fun setupView() {
//        Log.d(TAG, "setupView: $questions")

        binding.btnSubmit.setEnable(false)

        binding.cbSign.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.btnSubmit.setEnable(isChecked)
        }

        editBtn = getEditBtn()
        editText = getEditText()
        ddDrop = getDrop()
        setupText()
        binding.tvTextSigning.text =
            setTextSpan(getString(R.string.i_agree_to_the_terms_and_conditions_including_permission_to_publish))

        ddDrop.forEach {
            it.disableDropDown()
            it.iDropDownListener = ddListener
        }

        editText.forEach {
            when (it) {
                is OviLabeledEditText -> {
                    when (it) {
                        binding.include.incAddress.incBuilding.etText -> {}

                        else -> it.setEnable(false)
                    }
                }
                is OviAutocomplete -> {
                    it.addTextChangeListener(
                        1,
                        1,
                        ::callBack,
                        ::callbackLocation,
                        ::resetLocationField
                    )
                }
                is OviDropDown -> {
                    it.disableDropDown()
                    it.iDropDownListener = ddListener
                }
            }
            if (pref.showOnBoard) {
                binding.include.incOnboard.root.show()
                setUpDropDownItems()
            }
        }

        binding.include.incDob.etText.setOnIconClickListener {
            DatePicker.dateSetListener(
                binding.include.incDob.etText,
                1,
                binding.include.incRole.etText.getText()
            )
        }


        binding.toolbar.toolbarTitle = "Join OVI network"
        binding.toolbar.setBackListener { finish() }
        binding.toolbar.setEndIconListener { finish() }
        binding.btnSubmit.setOnClickListener(onClickListener)
        binding.tvTextSigning.setOnClickListener(onClickListener)

    }

    fun callBack(zipcode: String?, zip: Int) {
        masterVM.getZips(zipcode)
    }

    private fun callbackLocation(zipcode: String?) {
        getLocations(zipcode)
    }

    private fun getLocations(zipcode: String?) {
        masterVM.getLocation(zipcode)
    }

    private fun setupEmails() {
        with(binding.include.incEmail) {
            val listEmails = registerList?.get(RegKey.EMAIL.value)?.split("\n".toRegex())?.toList()
            incEmail.etText.label = getString(R.string.email)
            incEmail.etText.text = listEmails?.get(0)?.toString().toString()

            incEmail.etText.label = "Email 1"
            incEmail2.root.show()
            incEmail2.etText.label = "Email 2"
            if (listEmails?.size!! > 1)
                incEmail2.etText.text = listEmails[1]
            else
                incEmail2.etText.text = ""
            incEmail2.ivEdit.gone()
            incEmail3.ivEdit.gone()
            incEmail3.root.show()
            incEmail3.etText.label = "Email 3"
            if (listEmails.size > 2)
                incEmail3.etText.text = listEmails[2]
            else
                incEmail3.etText.text = ""


        }
    }

    private fun getDrop(): MutableList<OviDropDown> {
        with(binding.include.incOnboard) {
            return listOf<OviDropDown>(
                incEmployed.etText,
                incEducation.etText,
                incAdultLife.etText,
//                incHomeLess.etText,
//                incAlcoholAsses.etText,
//                incJail.etText,
                incChild.etText,
                incMedical.etText,
//                incFinacial.etText,
                incFosterCare.etText
            ) as MutableList<OviDropDown>
        }
    }

    private fun setUpDropDownItems() {
        binding.include.incRole.etText.loadPopupItems(DataUtil.getChooseRoles().map { it.content })
        binding.include.incRace.etText.loadPopupItems(DataUtil.getRace().map { it.content })
        binding.include.incEthnicity.etText.loadPopupItems(
            DataUtil.getEthnicity().map { it.content })
        binding.include.incGender.etText.loadPopupItems(DataUtil.getGender().map { it.content })
        if (pref.showOnBoard) {
            binding.include.incOnboard.incEmployed.etText.loadPopupItems(
                DataUtil.getYesNo().map { it.content })
            binding.include.incOnboard.incAdultLife.etText.loadPopupItems(
                DataUtil.getYesNo().map { it.content })
//            binding.include.incOnboard.incHomeLess.etText.loadPopupItems(
//                DataUtil.getYesNo().map { it.content })
//            binding.include.incOnboard.incAlcoholAsses.etText.loadPopupItems(
//                DataUtil.getYesNo().map { it.content })
//            binding.include.incOnboard.incJail.etText.loadPopupItems(
//                DataUtil.getYesNo().map { it.content })
            binding.include.incOnboard.incChild.etText.loadPopupItems(
                DataUtil.getYesNo().map { it.content })
            binding.include.incOnboard.incMedical.etText.loadPopupItems(
                DataUtil.getYesNo().map { it.content })
//            binding.include.incOnboard.incFinacial.etText.loadPopupItems(
//                DataUtil.getYesNo().map { it.content })
            binding.include.incOnboard.incFosterCare.etText.loadPopupItems(
                DataUtil.getYesNo().map { it.content })
        }
    }

    private val ddListener = object : IDropDownListener {
        override fun onDDClicked() {
        }

        override fun onOptionClicked(view: View, model: PopupModel) {
            when (view) {
                binding.include.incGender.etText.binding.root -> {
                    if (model.title == "Other") {
                        binding.include.incGender.etOther.show()
                    } else
                        binding.include.incGender.etOther.gone()
                }
            }

        }
    }

    private fun setTextSpan(text: String): SpannableString {

        val terms = "Terms and Conditions"
        val privacy = "Permission"

        val str = SpannableString(text).apply {
            setForegroundColorSpan(
                R.color.light_brown,
                this@ReviewSubmitActivity, text.indexOf(terms), text.indexOf(terms) + terms.length
            )

            setAbsoluteSpan(14, text.indexOf(terms), text.indexOf(terms) + terms.length)

            setForegroundColorSpan(
                R.color.light_brown,
                this@ReviewSubmitActivity,
                text.indexOf(privacy),
                text.indexOf(privacy) + privacy.length
            )
            setAbsoluteSpan(
                14, text.indexOf(privacy),
                text.indexOf(privacy) + privacy.length
            )
        }
        return str
    }

    private fun getEditText(): MutableList<View?> {
        with(binding.include) {
            return arrayListOf(
                incPhone.etText,
                incAddress.incAddress.etText,
                incAddress.incBuilding.etText,
                incAddress.incArea.etText,
                incAddress.incState.etText,
                incAddress.incCountry.etText,
                incAddress.incZip.etText,
                incEmail.incEmail.etText,
                incGender.etText,
                incEthnicity.etText,
                incRace.etText,
                incDob.etText,
                incName.etText,
                incRole.etText
            )
        }
    }

    override fun bindViewEvents() {
        super.bindViewEvents()
        editBtn.forEach {
            it?.setOnClickListener(onClickListener)
        }
        binding.tvTextSigning.setOnClickListener(onClickListener)
        binding.include.incRole.ivEdit.gone()
        binding.include.incRole.etText.hideDropDown()
    }

    private fun getEditBtn(): MutableList<View?> {
        with(binding.include) {
            return arrayListOf(
//                incOnboard.incFinacial.ivEdit,
                incOnboard.incMedical.ivEdit,
//                incOnboard.incJail.ivEdit,
                incPhone.ivEdit,
                incOnboard.incChild.ivEdit,
                incOnboard.incFosterCare.ivEdit,
//                incOnboard.incAlcoholAsses.ivEdit,
//                incOnboard.incHomeLess.ivEdit,
                incOnboard.incAdultLife.ivEdit,
                incOnboard.incEducation.ivEdit,
                incOnboard.incEmployed.ivEdit,
                incAddress.incAddress.ivEdit,
                incEmail.incEmail.ivEdit,
                incGender.ivEdit,
                incEthnicity.ivEdit,
                incRace.ivEdit,
                incDob.ivEdit,
                incName.ivEdit,
                incRole.ivEdit
            )
        }
    }


    override fun bindViewModel() {
        bindRegister()
        bindOnBoarding()
        bindMasterData()
        bindGetEducation()
        bindZipCode()
        bindLocation()
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
                        binding.include.incAddress.incArea.tvLabel.text =
                            it.value.data.rows[0]?.city.toString()
                        binding.include.incAddress.incState.tvLabel.text =
                            it.value.data.rows[0]?.state.toString()
                        binding.include.incAddress.incCountry.tvLabel.text =
                            it.value.data.rows[0]?.county.toString()
                    } else {
                        binding.include.incAddress.incArea.tvLabel.text =
                            "null"
                        binding.include.incAddress.incState.tvLabel.text =
                            "null"
                        binding.include.incAddress.incCountry.tvLabel.text =
                            "null"
                    }

                }
            }
        }
    }

    private fun resetLocationField() {

        binding.include.incAddress.incArea.tvLabel.text =
            ""
        binding.include.incAddress.incState.tvLabel.text =
            ""
        binding.include.incAddress.incCountry.tvLabel.text =
            ""
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
                    binding.include.incAddress.incZip.etText.setPopUpList(
                        this@ReviewSubmitActivity,
                        it.value.data as List<String>
                    )
                }
            }
        }
    }

    private fun bindGetEducation() {
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
                    questions = it.value
                    setEducation()
                }
            }
        }
    }

    private fun setEducation() {
        val education = questions?.data?.rows?.find { it?.id == 2 }
        val list = education?.choices?.map { it?.choice.toString() }
//        Log.d(TAG, "setEducation: $list")
        list?.let {
            binding.include.incOnboard.incEducation.etText.loadPopupItems(
                it
            )
        }
        binding.include.incOnboard.incEducation.etText.showDropDown()
    }

    private fun bindMasterData() {
        masterVM.masterData bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {}
                is ResultOf.Progress -> {
                    if (it.loading) binding.progressView.show() else binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    when (masterType) {
                        MasterDataType.RACE -> {
                            val list: List<String>? =
                                it.value.data?.rows?.map { it?.value.toString() }
                            list?.let { it1 -> binding.include.incRace.etText.loadPopupItems(it1) }

                        }
                        MasterDataType.ETHNICITY -> {

                            val list: List<String>? =
                                it.value.data?.rows?.map { it?.value.toString() }
                            list?.let { it1 ->
                                binding.include.incEthnicity.etText.loadPopupItems(
                                    it1
                                )
                            }

                        }
                        MasterDataType.ROLE -> {
                            val list: MutableList<String>? =
                                it.value.data?.rows?.map { it?.value.toString() } as MutableList<String>?
                            list?.remove("Admin")
                            list?.let { it1 -> binding.include.incRole.etText.loadPopupItems(it1) }

                        }
                        MasterDataType.GENDER -> {
                            val list: List<String>? =
                                it.value.data?.rows?.map { it?.value.toString() }
                            list?.let { it1 -> binding.include.incGender.etText.loadPopupItems(it1) }
                        }
                        null -> {

                        }
                        MasterDataType.LOCATIONS -> {}
                    }
                }
            }
        }
    }

    private fun bindOnBoarding() {
        vm.putonBoard bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
//                    showToast(it.message)
//                    Log.e(TAG, "bindViewModel: ${it.message}")
                }
                is ResultOf.Progress -> {
                    if (it.loading) binding.progressView.show() else binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    CongratsAccountActivity.present(this@ReviewSubmitActivity)
//                    finish()
                }
            }
        }
    }

    private fun bindRegister() {
        vm.register bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(it.message, OviSnackBar.SnackType.FAILURE, binding.btnSubmit)
//                    Log.e(TAG, "bindViewModel: ${it.message}")
                }
                is ResultOf.Progress -> {
                    if (it.loading) binding.progressView.show() else binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    if (it.value.statusCode == 201) {
                        if (pref.showOnBoard)
                            vm.putOnBoarding(
                                putOnBoardingResRequest = PutOnBoardingResRequest(
                                    answers = get10Questions()
                                )
                            )
                        else
                            CongratsAccountActivity.present(this@ReviewSubmitActivity)
                    }
                }
            }
        }
    }

    override fun getViewBinding() = ActivityReviewSubmitBinding.inflate(layoutInflater)

    @SuppressLint("SimpleDateFormat")
    override fun onClick(view: View) {
        checkCLickEvent(view)
        when (view) {
            binding.btnSubmit -> {
                if (isValid()) {
                    val tex = binding.include.incRole.etText.getText().trim().replace(" ", "_")
                    val register = RegisterRequest(
                        role = tex.lowercase(),
                        name = binding.include.incName.etText.text,
                        emails = getEmails(),
//                        youth_council_name = "Raun",
//                        shirt_size = "M",
                        joined_on = SimpleDateFormat("yyyy-MM-dd").format(Date()).toString(),
                        password = registerList?.getValue(RegKey.PASSWORD.value).toString()
                    )

                    if (binding.include.incEthnicity.etText.getText()
                            .isNotEmpty() && binding.include.incEthnicity.etText.getText() != "N/A"
                    )
                        register.ethnicity = binding.include.incEthnicity.etText.getText()

                    if (binding.include.incGender.etText.getText()
                            .isNotEmpty() && binding.include.incGender.etText.getText() != "N/A"
                    )
                        register.gender =
                            if (binding.include.incGender.etOther.isVisible()) binding.include.incGender.etOther.text.toString() else binding.include.incGender.etText.getText()

                    if (binding.include.incDob.etText.text.isNotEmpty() && binding.include.incDob.etText.text != "N/A")
                        register.birthday = binding.include.incDob.etText.text.getIsoDateFormat()

                    if (binding.include.incRace.etText.getText()
                            .isNotEmpty() && binding.include.incRace.etText.getText() != "N/A"
                    )
                        register.race = binding.include.incRace.etText.getText()

                    if (binding.include.incPhone.etText.text.trim()
                            .isNotEmpty() && binding.include.incPhone.etText.text.trim() != "N/A"
                    )
                        register.phone =
                            getString(R.string.prefix_number).plus(binding.include.incPhone.etText.text.trim())
                    if (binding.include.incAddress.incZip.etText.text.isNotEmpty() && binding.include.incAddress.incZip.etText.text != "N/A") {
                        val add = Address(
                            city = binding.include.incAddress.incArea.tvLabel.text,
                            state = binding.include.incAddress.incState.tvLabel.text,
                            county = binding.include.incAddress.incCountry.tvLabel.text,
                            zipcode = binding.include.incAddress.incZip.etText.text
                        )
                        if (binding.include.incAddress.incBuilding.etText.text.isNotEmpty() && binding.include.incAddress.incBuilding.etText.text != "N/A")
                            add.street = binding.include.incAddress.incBuilding.etText.text
                        register.address = add
                    }

                    vm.register(register)
                }
            }
            binding.tvTextSigning -> {
                startActivity(
                    Intent(
                        "android.intent.action.VIEW",
                        Uri.parse("https://onevoiceimpact.app/privacypolicy")
                    )
                )
            }
        }
    }

    private fun getEmails(): List<String?> {

        val listEmail = mutableListOf<String>()
        val listEmails = registerList?.get(RegKey.EMAIL.value)?.split("\n".toRegex())?.toList()

        if (emailEdit) {
            listEmail.add(binding.include.incEmail.incEmail.etText.text.trim())
            when (listEmails?.size) {
                1 -> {}
                2 -> {
                    if (binding.include.incEmail.incEmail2.etText.text.trim().isNotEmpty())
                        listEmail.add(binding.include.incEmail.incEmail2.etText.text.trim())
                }
                3 -> {
                    if (binding.include.incEmail.incEmail2.etText.text.trim().isNotEmpty())
                        listEmail.add(binding.include.incEmail.incEmail2.etText.text.trim())
                    if (binding.include.incEmail.incEmail3.etText.text.trim().isNotEmpty())
                        listEmail.add(binding.include.incEmail.incEmail3.etText.text.trim())
                }
            }

        } else
            listEmails?.let { listEmail.addAll(it) }
        return listEmail
    }

    private fun isValid(): Boolean {

//        if (binding.include.incGender.etOther.isVisible()) {
//            if (binding.include.incGender.etOther.text.toString().isEmpty()) {
//                binding.include.incGender.etOther.error = "Field can't be blank"
//                binding.include.incGender.etOther.requestFocus()
//                return false
//            }
//        }

//        editText.forEach {
//            when (it) {
//                is OviLabeledEditText -> {
//                    if (it.id == binding.include.incName.etText.id)
//                        if (it.text.isEmpty()) {
//                            it.setError()
//                            return false
//                        }
//                }
////                is OviDropDown -> {
////                    if (it.getText().isEmpty()) {
////                        it.setError("Field can't be blank")
////                        return false
////                    }
////                }
//            }
//        }

        when {
            binding.include.incAddress.incArea.tvLabel.text == "null" -> {
                binding.include.incAddress.incArea.tvLabel.setError("Value cant be null")
                return false
            }
            binding.include.incAddress.incState.tvLabel.text == "null" -> {
                binding.include.incAddress.incState.tvLabel.setError("Value cant be null")
                return false
            }
            binding.include.incAddress.incCountry.tvLabel.text == "null" -> {
                binding.include.incAddress.incCountry.tvLabel.setError("Value cant be null")
                return false
            }

        }


        if (binding.include.incName.etText.text.isEmpty()) {
            binding.include.incName.etText.setError("Field can't be blank")
            return false
        }
        if (binding.include.incPhone.etText.text.isEmpty()) {
            binding.include.incPhone.etText.setError("Field can't be blank")
            return false
        }

        if (emailEdit) {
            when {
                !binding.include.incEmail.incEmail.etText.text.checkEmail()
                -> {
                    binding.include.incEmail.incEmail.etText.setError("Email id is not Valid")
                    return false
                }
                binding.include.incEmail.incEmail2.root.isVisible() -> {
                    if (binding.include.incEmail.incEmail2.etText.text.isNotEmpty())
                        if (!binding.include.incEmail.incEmail2.etText.text.checkEmail()

                        ) {
                            binding.include.incEmail.incEmail2.etText.setError("Email id is not Valid")
                            return false
                        }
                    if (binding.include.incEmail.incEmail3.root.isVisible())
                        if (binding.include.incEmail.incEmail3.etText.text.isNotEmpty())
                            if (!binding.include.incEmail.incEmail3.etText.text.checkEmail()

                            ) {
                                binding.include.incEmail.incEmail3.etText.setError("Email id is not Valid")
                                return false
                            }
                }

            }
        }

        if (!binding.cbSign.isChecked) {
            showSnackBar(
                "Accept the Terms and Condition", OviSnackBar.SnackType.VALIDATION,
                binding.btnSubmit
            )
            return false
        }
        return true
    }

    private fun get10Questions(): MutableList<AnswersItem?> {
        var list: MutableList<AnswersItem?>? = null
        list = mutableListOf<AnswersItem?>()
        ddDrop.forEachIndexed { index, it ->
//            Log.e(TAG, "get10Questions: ${it.getText()}", )
            if (it.getText().isNotEmpty()) {
                val answer = AnswersItem(
                    text_value = it.getText(),
                    question_id = when (it) {
                        binding.include.incOnboard.incEmployed.etText -> {
                            1
                        }
                        binding.include.incOnboard.incEducation.etText -> {
                            2
                        }
                        binding.include.incOnboard.incAdultLife.etText -> {
                            3
                        }
                        binding.include.incOnboard.incChild.etText -> {
                            7
                        }
                        binding.include.incOnboard.incMedical.etText -> {
                            8
                        }
                        binding.include.incOnboard.incFosterCare.etText -> {
                            10
                        }
                        else -> {
                            0
                        }
                    }
                )
                if (answer.question_id != 0) {
                    if (questions != null)
                        answer.choice_id =
                            if (index == 1) questions?.data?.rows?.find { it?.id == 2 }?.choices?.find {
                                it?.choice.equals(
                                    binding.include.incOnboard.incEducation.etText.getText(),
                                    true
                                )
                            }?.id else 1
                    else
                        answer.choice_id = if (index == 1) DataUtil.getEducation().indexOfFirst {
                            it.content.equals(
                                binding.include.incOnboard.incEducation.etText.getText(),
                                true
                            )
                        }.plus(1) else 1

                    list.add(
                        answer
                    )
                }
            }
        }

        return list
    }

    private fun checkCLickEvent(view: View) {
        with(binding.include) {
            when (view) {
//                incOnboard.incFinacial.ivEdit -> {
//                    customAlertBox(incOnboard.incFinacial.etText, view)
//                }
                incOnboard.incMedical.ivEdit -> {
                    customAlertBox(incOnboard.incMedical.etText, view)
                }
//                incOnboard.incJail.ivEdit -> {
//                    customAlertBox(incOnboard.incJail.etText, view)
//                }
                incPhone.ivEdit -> {
                    customAlertBox(incPhone.etText, view)
                }
                incOnboard.incChild.ivEdit -> {
                    customAlertBox(incOnboard.incChild.etText, view)
                }
                incOnboard.incFosterCare.ivEdit -> {
                    customAlertBox(incOnboard.incFosterCare.etText, view)
                }
//                incOnboard.incAlcoholAsses.ivEdit -> {
//                    customAlertBox(incOnboard.incAlcoholAsses.etText, view)
//                }
//                incOnboard.incHomeLess.ivEdit -> {
//                    customAlertBox(incOnboard.incHomeLess.etText, view)
//                }
                incOnboard.incAdultLife.ivEdit -> {
                    customAlertBox(incOnboard.incAdultLife.etText, view)
                }
                incOnboard.incEducation.ivEdit -> {
                    customAlertBox(incOnboard.incEducation.etText, view)
                }
                incOnboard.incEmployed.ivEdit -> {
                    customAlertBox(incOnboard.incEmployed.etText, view)
                }
                incAddress.incAddress.ivEdit -> {
                    customAlertBox(incAddress.incAddress.etText, view)
                }
                incEmail.incEmail.ivEdit -> {
                    customAlertBox(incEmail.incEmail.etText, view)
                }
                incGender.ivEdit -> {
                    customAlertBox(incGender.etText, view)
                }
                incEthnicity.ivEdit -> {
                    customAlertBox(incEthnicity.etText, view)
                }
                incRace.ivEdit -> {
                    customAlertBox(incRace.etText, view)
                }
                incDob.ivEdit -> {
                    customAlertBox(incDob.etText, view)
                }
                incName.ivEdit -> {
                    customAlertBox(incName.etText, view)
                }
                incRole.ivEdit -> {
                    customAlertBox(incRole.etText, view)
                }
            }
        }
    }

    private fun setupText() {
        with(binding.include) {
            incRole.etText.setTvLabel("Your role")
            incRole.etText.setText(registerList?.getValue(RegKey.ROLE.value).toString())

            incName.etText.label = "Name"
            incName.etText.text = registerList?.getValue(RegKey.NAME.value).toString()

            incEmail.incEmail.etText.label = "Email"
            incEmail.incEmail.etText.text = registerList?.getValue(RegKey.EMAIL.value).toString()

            incDob.etText.label = getString(R.string.date_of_birth)
            incDob.etText.text = if (registerList?.containsKey(RegKey.BIRTHDAY.value) == true)
                registerList?.getValue(RegKey.BIRTHDAY.value).toString().setIsoDateFormat()
                    .toString()
            else "N/A"

            incRace.etText.setTvLabel(getString(R.string.race))
            incRace.etText.setText(
                if (registerList?.containsKey(RegKey.RACE.value) == true) registerList?.getValue(
                    RegKey.RACE.value
                ).toString() else "N/A"
            )

            incEthnicity.etText.setTvLabel(getString(R.string.ethnicity))
            incEthnicity.etText.setText(
                if (registerList?.containsKey(RegKey.ETHNICITY.value) == true) registerList?.getValue(
                    RegKey.ETHNICITY.value
                ).toString() else "N/A"
            )

            incGender.etText.setTvLabel(getString(R.string.gender_identity))
            incGender.etText.setText(
                if (registerList?.containsKey(RegKey.GENDER.value) == true) registerList?.getValue(
                    RegKey.GENDER.value
                ).toString() else
                    "N/A"
            )
            incGender.etOther.setText(
                if (registerList?.containsKey(RegKey.GENDER.value) == true) registerList?.getValue(
                    RegKey.GENDER.value
                ).toString() else "N/A"
            )

            incPhone.etText.label = getString(R.string.phone_number)
            incPhone.etText.setPrefixText(getString(R.string.prefix_number))
            incPhone.etText.text = if (registerList?.containsKey(RegKey.PHONE.value) == true)
                registerList?.getValue(RegKey.PHONE.value).toString().takeLast(10) else ""
            incPhone.etText.setMaxLength(10)

            incAddress.incAddress.etText.label = getString(R.string.address)
            incAddress.incAddress.etText.text =
                "${
                    if (registerList?.containsKey(RegKey.BUILDING.value) == true) registerList?.getValue(
                        RegKey.BUILDING.value
                    ).toString() else ""
                }\n" +
                        "${
                            if (registerList?.containsKey(RegKey.AREA.value) == true) registerList?.getValue(
                                RegKey.AREA.value
                            ).toString() else ""
                        }\n" +

                        "${
                            if (registerList?.containsKey(RegKey.STATE.value) == true) registerList?.getValue(
                                RegKey.STATE.value
                            ).toString() else ""
                        } ${
                            if (registerList?.containsKey(RegKey.COUNTRY.value) == true)
                                registerList?.getValue(RegKey.COUNTRY.value).toString() else ""
                        } ${
                            if (registerList?.containsKey(RegKey.ZIPCODE.value) == true)
                                registerList?.getValue(RegKey.ZIPCODE.value).toString() else ""
                        }"

            incAddress.incBuilding.etText.label = getString(R.string.building_no_street)
            incAddress.incBuilding.etText.text =
                if (registerList?.containsKey(RegKey.BUILDING.value) == true)
                    registerList?.getValue(RegKey.BUILDING.value).toString() else ""
            incAddress.incBuilding.ivEdit.gone()

            incAddress.incArea.tvLabel.label = getString(R.string.area_city)
            incAddress.incArea.tvLabel.text =
                if (registerList?.containsKey(RegKey.AREA.value) == true)
                    registerList?.getValue(RegKey.AREA.value).toString() else ""
            incAddress.incArea.ivEdit.gone()

            incAddress.incState.tvLabel.label = getString(R.string.state)
            incAddress.incState.tvLabel.text =
                if (registerList?.containsKey(RegKey.STATE.value) == true)
                    registerList?.getValue(RegKey.STATE.value).toString() else ""
            incAddress.incState.ivEdit.gone()

            incAddress.incCountry.tvLabel.label = getString(R.string.country)
            incAddress.incCountry.tvLabel.text =
                if (registerList?.containsKey(RegKey.COUNTRY.value) == true)
                    registerList?.getValue(RegKey.COUNTRY.value).toString() else ""
            incAddress.incCountry.ivEdit.gone()

            incAddress.incZip.etText.label = getString(R.string.zipcode)
            incAddress.incZip.etText.text =
                if (registerList?.containsKey(RegKey.ZIPCODE.value) == true)
                    registerList?.getValue(RegKey.ZIPCODE.value).toString() else ""
            incAddress.incZip.etText.setInputType(InputType.TYPE_CLASS_NUMBER)
            incAddress.incZip.etText.setMaxLength(5)
            incAddress.incZip.ivEdit.gone()


            if (pref.showOnBoard) {
                incOnboard.incEmployed.etText.setTvLabel(getString(R.string.are_you_employed_either_part))
                incOnboard.incEducation.etText.setTvLabel(getString(R.string.what_is_your_highest_educatiion))
                incOnboard.incAdultLife.etText.setTvLabel(getString(R.string.do_you_have_at_least_one_adult_in_your_life_other_than_the_person_assigned_to_your_foster_care_or_il_case_that_you_can_go_to_for_advice_or_emotional_support))
                incOnboard.incChild.etText.setTvLabel(getString(R.string.have_you_given_birth_to_a_child_or_i_am_a_father_to_a_child_that_has_been_born_in_the_last_two_years))
                incOnboard.incMedical.etText.setTvLabel(getString(R.string.do_you_have_a_medicaid_plan_or_some_other_health_insurance_coverage))
                incOnboard.incFosterCare.etText.setTvLabel(getString(R.string.are_you_participating_in_one_of_the_following_independent_living_programs_extended_foster_care_efc_postsecondary_education_services_and_support_pess_or_aftercare_services))
//                Log.e(TAG, "setupText: Answer : ${onBoardinList?.answers}", )
                onBoardinList?.answers?.forEachIndexed { index, answersItem ->
//                    Log.e(TAG, "setupText: ${answersItem?.question_id}", )
                    when (answersItem?.question_id) {
                        1 -> {
                            incOnboard.incEmployed.etText.setText(answersItem.text_value.toString())
                        }
                        2 -> {
                            incOnboard.incEducation.etText.setText(
                                answersItem.text_value.toString()
                            )
                        }
                        3 -> {

                            incOnboard.incAdultLife.etText.setText(
                                answersItem.text_value.toString()
                            )
                        }

//                incOnboard.incHomeLess.etText.setTvLabel(getString(R.string.have_you_been_homeless_at_least_once_in_the_past_two_years))
//                incOnboard.incHomeLess.etText.setText(onBoardinList?.answers?.get(3)?.text_value.toString())
//
//                incOnboard.incAlcoholAsses.etText.setTvLabel(
//                    getString(R.string.in_the_past_two_years_have_you_participated_in_an_alcohol_or_drug_abuse_assessment_counseling)
//                )
//                incOnboard.incAlcoholAsses.etText.setText(onBoardinList?.answers?.get(4)?.text_value.toString())
//
//                incOnboard.incJail.etText.setTvLabel(getString(R.string.have_you_ever_been_in_a_jail_prison_correctional_facility_or_juvenile_or_community_detention_facility_at_least_once_in_the_past_two_years))
//                incOnboard.incJail.etText.setText(onBoardinList?.answers?.get(5)?.text_value.toString())
                        7 -> {
                            incOnboard.incChild.etText.setText(answersItem.text_value.toString())
                        }
                        8 -> {
                            incOnboard.incMedical.etText.setText(answersItem.text_value.toString())
                        }

//                incOnboard.incFinacial.etText.setTvLabel(getString(R.string.do_you_feel_confident_in_your_abilities_to_live_independently_without_outside_financial_assistance))
//                incOnboard.incFinacial.etText.setText(onBoardinList?.answers?.get(8)?.text_value.toString())
                        10 -> {
                            incOnboard.incFosterCare.etText.setText(answersItem.text_value.toString())
                        }
                    }
                }
            }
        }
    }

    private fun customAlertBox(v: View, view: View) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Edit")

        builder.setMessage("Are your really want to edit details")

        builder.setPositiveButton("Yes") { dialogInterface, which ->
            if (v is OviLabeledEditText)
                when (v) {
                    binding.include.incAddress.incAddress.etText -> {
                        showEditableAddress()
                    }
                    binding.include.incEmail.incEmail.etText -> {
                        emailEdit = true
                        v.setEnable(true)
                        setupEmails()
                    }
                    binding.include.incDob.etText -> {
                        binding.include.incDob.etText.setEndIconDrawable()
//                        v.setEnable(true)
                    }
                    else -> v.setEnable(true)
                }
            if (v is OviDropDown) {
                when (v) {
                    binding.include.incRole.etText -> {
                        masterType = MasterDataType.ROLE
                        masterVM.getMaster(MasterDataType.ROLE.value)
                    }
                    binding.include.incRace.etText -> {
                        masterType = MasterDataType.RACE
                        masterVM.getMaster(MasterDataType.RACE.value)
                    }
                    binding.include.incGender.etText -> {
                        masterType = MasterDataType.GENDER
                        masterVM.getMaster(MasterDataType.GENDER.value)
                    }
                    binding.include.incEthnicity.etText -> {
                        masterType = MasterDataType.ETHNICITY
                        masterVM.getMaster(MasterDataType.ETHNICITY.value)
                    }
                    binding.include.incOnboard.incEducation.etText -> {
                        vm.getOnBoarding()
                    }

                    else -> {
                        v.showDropDown()
                    }
                }
                v.showDropDown()
            }
            view.gone()
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun showEditableAddress() {
        binding.include.incAddress.incAddress.root.gone()
        binding.include.incAddress.lLayAddress.show()
        with(binding.include.incAddress) {
            incArea.etText.gone()
            incArea.tvLabel.show()
            incState.etText.gone()
            incState.tvLabel.show()
            incCountry.etText.gone()
            incCountry.tvLabel.show()
            incArea.ivEdit.gone()
            incState.ivEdit.gone()
            incCountry.ivEdit.gone()
        }
    }
}