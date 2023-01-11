package org.ovi.feature.events.view.activity

import android.R.attr.label
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.SpannableString
import android.view.View
import android.view.Window
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatTextView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.ovi.R
import org.ovi.base.BaseActivity
import org.ovi.databinding.ActivityEventsDetailBinding
import org.ovi.feature.auth.view.LoginActivity
import org.ovi.feature.events.model.EventDetailsMode
import org.ovi.feature.events.model.InvitationStatus
import org.ovi.feature.events.model.ParticularEventResponse
import org.ovi.feature.events.viewmodel.EventsViewModel
import org.ovi.feature.profile.view.activity.RETURN_RESULT
import org.ovi.feature.survey.view.activity.SurveyActivity
import org.ovi.feature.survey.view.activity.SurveyType
import org.ovi.network.ResultOf
import org.ovi.util.extensions.*
import org.ovi.widget.OviSnackBar
import java.lang.ref.WeakReference


const val EVENT_ID = "eventID"

class EventsDetailActivity : BaseActivity<ActivityEventsDetailBinding>() {

    private val vm: EventsViewModel by viewModel { parametersOf(0) }

    private var meetingLink: String? = null

    private var data: ParticularEventResponse? = null

    var callMode: EventDetailsMode? = null

    companion object {
        fun present(
            activityWeakReference: WeakReference<Activity>,
            eventMode: EventDetailsMode,
            eventId: Int
        ) {
            activityWeakReference.get()?.startActivity(
                Intent(
                    activityWeakReference.get(),
                    EventsDetailActivity::class.java
                ).putExtra(EventDetailsMode.MODE.toString(), eventMode)
                    .putExtra(EVENT_ID, eventId)
            )
        }

        fun createIntent(
            activityWeakReference: WeakReference<Activity>,
            eventMode: EventDetailsMode,
            eventId: Int
        ): Intent {
            return Intent(
                activityWeakReference.get(),
                EventsDetailActivity::class.java
            ).putExtra(EventDetailsMode.MODE.toString(), eventMode)
                .putExtra(EVENT_ID, eventId)
        }
    }

    override fun preInit() {
        if (!pref.isLoggedIn) {
            alertMessage()
//            LoginActivity.present(this)
        }
    }

    private val mode by lazy {
        intent.getSerializableExtra(EventDetailsMode.MODE.toString()) as EventDetailsMode
    }
    private val eventId by lazy {
        intent.getIntExtra(EVENT_ID, -1)
    }

    override fun setupView() {
        callMode = mode
        if (eventId != -1)
            getEvent()
        binding.toolbar.setBackListener {
            finish()
        }
        binding.incSurveyBtn.tvTakePost.setOnClickListener(onClickListener)
        binding.incSurveyBtn.tvTakePre.setOnClickListener(onClickListener)
        binding.tvTakePre.setOnClickListener(onClickListener)
        binding.incText.tvCopyLink.setOnClickListener(onClickListener)
        binding.incText.ltLink.setOnClickListener(onClickListener)
    }


    private fun getEvent() {
        vm.getParticularEvent(eventId)
    }

    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data?.getBooleanExtra(RETURN_RESULT, false) == true) {
                    recreate()
                }
            }
        }

    override fun bindViewEvents() {
        super.bindViewEvents()
        binding.btnRegister.setOnClickListener(onClickListener)
        binding.tvCancel.setOnClickListener(onClickListener)
    }

    private fun setMode(mode: EventDetailsMode) {
        when (mode) {
            EventDetailsMode.REGISTER -> {
                binding.btnRegister.show()
                binding.tvCancel.gone()
                binding.tvTakePre.gone()
            }
            EventDetailsMode.CANCEL -> {
                binding.btnRegister.gone()
                binding.tvCancel.show()
                if (data?.data?.pre_survey_id != null && data?.data?.pre_survey_id != "")
                    binding.tvTakePre.show()
                checkSurveyAttended()
            }
            EventDetailsMode.ACCEPT -> {
                binding.btnRegister.text = getString(R.string.accept_invite)
            }
            EventDetailsMode.ATTENDED -> {
                binding.btnRegister.gone()
                binding.tvCancel.gone()
                binding.tvTakePre.gone()
                binding.incSurveyBtn.root.show()
                if (data?.data?.pre_survey_id != null && data?.data?.pre_survey_id != "")
                    binding.incSurveyBtn.tvTakePre.show()
                if (data?.data?.post_survey_id != null && data?.data?.post_survey_id != "")
                    binding.incSurveyBtn.tvTakePost.show()
                checkSurveyAttended()
            }
            else -> {}
        }
    }

    override fun bindViewModel() {
        bindParticularEvent()
        bindRegisterEvent()
        bindUpdateEvent()
    }

    private fun bindUpdateEvent() {
        vm.updateEvent bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {

                    if (it.code == "HTTP 404 Not Found") nonEligibledialogue()
//                    showSnackBar(
//                        it.message,
//                        OviSnackBar.SnackType.FAILURE
//                    )

                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    showSnackBar(
                        it.value.message,
                        OviSnackBar.SnackType.SUCCESS
                    )
                    val result = Intent()
                    result.putExtra(RETURN_RESULT, true)
                    setResult(RESULT_OK, result)

//                    Log.e(TAG, "bindUpdateEvent: $callMode")
                    when (callMode) {
                        EventDetailsMode.REGISTER -> {
//                            setMode(EventDetailsMode.CANCEL)
                            callMode = EventDetailsMode.CANCEL
                            getEvent()
                        }
                        EventDetailsMode.CANCEL -> {
                            if (data?.data?.participants?.isEmpty() == true || data?.data?.participants?.get(
                                    0
                                )?.invitedOn == null
                            ) {
                                callMode = EventDetailsMode.REGISTER
//                                setMode(EventDetailsMode.REGISTER)
                                getEvent()
                            } else {
                                callMode = EventDetailsMode.ACCEPT
                                getEvent()
//                                setMode(EventDetailsMode.ACCEPT)
                            }
                        }
                        EventDetailsMode.ACCEPT -> {
                            callMode = EventDetailsMode.CANCEL
                            getEvent()
//                            setMode(EventDetailsMode.CANCEL)
                        }
                        EventDetailsMode.MODE -> {}
                        EventDetailsMode.ATTENDED -> {}
                        else -> {}
                    }
                    underLineText()

                }
            }
        }
    }

    private fun bindRegisterEvent() {

        vm.registerFOREvent bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
//                    Log.d(TAG, "bindUpdateEvent: ${it.code}")
                    if (it.code == "HTTP 404 Not Found") nonEligibledialogue()
//                    showSnackBar(
//                        it.message,
//                        OviSnackBar.SnackType.FAILURE,
//                        binding.btnRegister
//                    )
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else
                        binding.progressView.gone()
                }
                is ResultOf.Success -> {
                    showSnackBar(
                        it.value.message,
                        OviSnackBar.SnackType.SUCCESS,
                        binding.btnRegister
                    )
                    val result = Intent()
                    result.putExtra(RETURN_RESULT, true)
                    setResult(RESULT_OK, result)
                    callMode = EventDetailsMode.CANCEL
                    getEvent()
//                    setMode()
                }
            }
        }

    }

    private fun bindParticularEvent() {
        vm.particularEvent bindTo {
            when (it) {
                is ResultOf.Empty -> {}
                is ResultOf.Failure -> {
                    showSnackBar(
                        it.message,
                        OviSnackBar.SnackType.FAILURE,
                        binding.btnRegister
                    )
                }
                is ResultOf.Progress -> {
                    if (it.loading)
                        binding.progressView.show()
                    else {
                        binding.progressView.gone()
                    }
                }
                is ResultOf.Success -> {
                    if (it.value.data != null) {
                        binding.scrollView.show()
                        data = it.value
                        setupValue(it.value)
                    } else
                        binding.incNoEvent.root.show()
                }
            }
        }
    }

    private fun copyLink() {
        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label.toString(), meetingLink)
        clipboard.setPrimaryClip(clip)
        showToast("copied to clipboard")
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupValue(value: ParticularEventResponse) {
        setMode(callMode!!)
        if (value.data?.type.equals("offline", true))
            binding.incText.tvCopyLink.gone()

        with(binding) {
            incText.tvTitle.text = value.data?.name
            incText.tvSubTitle.text = value.data?.description
            incText.ltStatus.text = value.data?.status.toString()
            incText.ltType.text = value.data?.type.toString()
            binding.incImage.ivImage.setImage(value.data?.imageUrl, R.drawable.tmp_event_image)

            incText.ltLink.text =
                if (value.data?.link.toString().contains("google")) "Google Link" else "Zoom link"
            underLineText()
            meetingLink = value.data?.link.toString()
            if (value.data?.event_start_date != null) {
                incText.ltDate.text =
                    value.data.event_start_date.getFormatedDateToDisplay().toString()
                if (value.data.event_end_date != null)
                    incText.ltTime.text =
                        "${value.data.event_start_date.getTimeIn12hrsFormat()} ~ ${value.data.event_end_date.getTimeIn12hrsFormat()}"
            }
            incText.ltGift.text =
                if (value.data?.gift != null && value.data.gift != "") value.data.gift else "N/A"
            incText.ltEventOwner.text = value.data?.createdBy?.user?.name.toString()
            incText.ltYouthofage.text =
                if (value.data?.ageFrom != null && value.data.ageTo != null) "${(value.data.ageFrom as Double).toInt()} to ${(value.data.ageTo as Double).toInt()} Years" else "N/A"
            incText.ltFrom.text =
                if (value.data?.county != null && value.data.county != "") value.data.county.toString() else "N/A"
        }


    }

    private fun checkSurveyAttended() {
        if (data?.data?.preQuizTaken == true) {
            if (binding.tvTakePre.isVisible()) {
                binding.tvTakePre.hasAttended(binding.tvTextSurveyPre)
                binding.tvTextSurveyPre.text =
                    binding.root.context.getOviString(R.string.pre_survey_taken)
            }

            if (binding.incSurveyBtn.tvTakePre.isVisible()) {
                binding.incSurveyBtn.tvTakePre.hasAttended(binding.incSurveyBtn.tvTextSurveyPre)
                binding.incSurveyBtn.tvTextSurveyPre.text =
                    binding.root.context.getOviString(R.string.pre_survey_taken)
            }
        } else {
            if (binding.tvTakePre.isVisible()) {
                binding.tvTakePre.notAttended(binding.tvTextSurveyPre)
                binding.tvTextSurveyPre.text =
                    binding.root.context.getOviString(R.string.take_pre_survey)
            }

            if (binding.incSurveyBtn.tvTakePre.isVisible()) {
                binding.incSurveyBtn.tvTakePre.notAttended(binding.incSurveyBtn.tvTextSurveyPre)
                binding.incSurveyBtn.tvTextSurveyPre.text =
                    binding.root.context.getOviString(R.string.take_pre_survey)
            }

        }

        if (data?.data?.postQuizTaken == true) {
            if (binding.incSurveyBtn.tvTakePost.isVisible()) {
                binding.incSurveyBtn.tvTakePost.hasAttended(binding.incSurveyBtn.tvTextSurveyPost)
                binding.incSurveyBtn.tvTextSurveyPost.text =
                    binding.root.context.getOviString(R.string.post_survey_taken)

            }
        } else {
            if (binding.incSurveyBtn.tvTakePost.isVisible()) {
                binding.incSurveyBtn.tvTakePost.notAttended(binding.incSurveyBtn.tvTextSurveyPost)
                binding.incSurveyBtn.tvTextSurveyPost.text =
                    binding.root.context.getOviString(R.string.take_post_survey)

            }
        }
    }

    override fun getViewBinding() = ActivityEventsDetailBinding.inflate(layoutInflater)

    private fun underLineText() {
        if (!binding.btnRegister.isVisible()) {
            binding.incText.ltLink.setUnderlineText()
        } else

            binding.incText.ltLink.removeUnderlineText()
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnRegister -> {
                if (binding.btnRegister.text.equals("REGISTER"))
                    confirmationMessage(EventDetailsMode.REGISTER)
                else
                    confirmationMessage(EventDetailsMode.ACCEPT)
            }
            binding.tvCancel -> {
                confirmationMessage(EventDetailsMode.CANCEL)
            }
            binding.incText.tvCopyLink -> {
                copyLink()
            }
            binding.incText.ltLink -> {
                if (!binding.btnRegister.isVisible()) {
                    if (data?.data?.link != null) {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data?.data?.link))
                        startActivity(browserIntent)
                    } else
                        showSnackBar("No Link Available", OviSnackBar.SnackType.VALIDATION)
                } else
                    showSnackBar("kindly register for the event", OviSnackBar.SnackType.VALIDATION)
            }
            binding.incSurveyBtn.tvTakePre -> {

                if (data?.data?.pre_survey_id != null)
                    SurveyActivity.present(
                        this,
                        data?.data?.pre_survey_id!!,
                        data?.data?.id,
                        SurveyType.PRE.value
                    )
            }
            binding.incSurveyBtn.tvTakePost -> {

                if (data?.data?.post_survey_id != null)
                    SurveyActivity.present(
                        this,
                        data?.data?.post_survey_id!!,
                        data?.data?.id,
                        SurveyType.POST.value
                    )
            }
            binding.tvTakePre -> {
//                Log.e(TAG, "onClick: ${data?.data?.id}")
                if (data?.data?.pre_survey_id != null)
                    SurveyActivity.present(
                        this,
                        data?.data?.pre_survey_id!!,
                        data?.data?.id,
                        SurveyType.PRE.value
                    )
            }

        }
    }

    private fun callApi(modeType: EventDetailsMode) {
        when (modeType) {
            EventDetailsMode.REGISTER -> {
                callMode = EventDetailsMode.REGISTER
                vm.registerForEvents(eventId)
            }
            EventDetailsMode.CANCEL -> {
                callMode = EventDetailsMode.CANCEL
                vm.updateEvent(eventId, InvitationStatus.CANCEL)
            }
            EventDetailsMode.ACCEPT -> {
                callMode = EventDetailsMode.ACCEPT
                vm.updateEvent(eventId, InvitationStatus.ACCEPTED)
            }
            else -> {}
        }
    }


    private fun confirmationMessage(type: EventDetailsMode) {
        with(Dialog(this)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.custom_cofirmation_dialogue)

            val tvCancel = findViewById<AppCompatTextView>(R.id.tvNo)
            val tvYes = findViewById<AppCompatTextView>(R.id.tvYes)
            val title = findViewById<AppCompatTextView>(R.id.tvTitle)
            val messsage = findViewById<AppCompatTextView>(R.id.tvSubTitle)

            when (type) {
                EventDetailsMode.REGISTER -> {
                    title.text = getString(R.string.confirm_registration)
                    val text = getString(
                        R.string.please_confirm_whether_you_want_to_register_for_event_name,
                        binding.incText.tvTitle.text
                    )
                    messsage.text = setTypeFace(text)
                }
                EventDetailsMode.CANCEL -> {
                    title.text = getString(R.string.confirm_cancellation)
                    val text = getString(
                        R.string.please_confirm_whether_you_want_to_cancel_participation_in_event_name,
                        binding.incText.tvTitle.text
                    )
                    messsage.text = setTypeFace(text)
                    tvCancel.setTextColor(getOviColor(R.color.light_blue))
                    tvYes.setTextColor(getOviColor(R.color.red_600))
                }
                EventDetailsMode.ACCEPT -> {
                    title.text = getString(R.string.confirm_accepting_the_invite)
                    val text = getString(
                        R.string.please_confirm_whether_you_want_to_accept_invite_for_event_name,
                        binding.incText.tvTitle.text
                    )
                    messsage.text = setTypeFace(text)
                }
                else -> {}
            }
            tvCancel.setOnClickListener {
                dismiss()
            }
            tvYes.setOnClickListener {
                dismiss()
                callApi(type)
            }
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }

    private fun setTypeFace(text: String): CharSequence {
        val subText = binding.incText.tvTitle.text.toString()
//        Log.e(TAG, "setTypeFace: $text : $subText" )
        val str = SpannableString(text).apply {
            setForegroundColorSpan(
                R.color.light_blue,
                this@EventsDetailActivity,
                text.indexOf(subText),
                text.indexOf(subText) + subText.length
            )
            setCustomTypefaceSpanBold(
                R.font.opensans_semibold,
                this@EventsDetailActivity,
                text.indexOf(subText),
                text.indexOf(subText) + subText.length
            )
        }
        return str

    }

    private fun nonEligibledialogue() {
        with(Dialog(this)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.custom_not_eligible_dialogue)

            val tvOkay = findViewById<AppCompatTextView>(R.id.tvOkay)

            tvOkay.setOnClickListener {
                dismiss()
            }

            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }

    private fun alertMessage() {
        with(Dialog(this)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.custom_cofirmation_dialogue)

            val tvCancel = findViewById<AppCompatTextView>(R.id.tvNo)
            val tvYes = findViewById<AppCompatTextView>(R.id.tvYes)
            val title = findViewById<AppCompatTextView>(R.id.tvTitle)
            val messsage = findViewById<AppCompatTextView>(R.id.tvSubTitle)

            title.text = getString(R.string.registered_user_only)
            messsage.text = getString(
                R.string.please_login_to_view_event_details
            )

            tvCancel.setOnClickListener {
                dismiss()
                recreate()
            }
            tvYes.setOnClickListener {
//                dismiss()
                activityLauncher.launch(LoginActivity.createIntent(WeakReference<Activity>(this@EventsDetailActivity)))
            }
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }

}

