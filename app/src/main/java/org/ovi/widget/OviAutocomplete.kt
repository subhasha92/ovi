package org.ovi.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import org.ovi.R
import org.ovi.databinding.WidgetOviAutocompleteBinding
import org.ovi.util.extensions.getOviDrawable
import org.ovi.util.extensions.getOviFont
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.show
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2

class OviAutocomplete : ConstraintLayout {
    private val binding by lazy {
        WidgetOviAutocompleteBinding.inflate(LayoutInflater.from(context), this, true)
    }

    var adapter:ArrayAdapter<String>?=null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun setPopUpList(context: Context, list: List<String>) {
         adapter =
            ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list)
        binding.etAuto.setAdapter(adapter)
        if(!binding.etAuto.isPopupShowing)
            binding.etAuto.showDropDown()
        binding.autoProgress.gone()
    }

    var label: String = ""
        set(value) {
            field = value
            binding.tvLabel.text = value
        }
        get() = binding.tvLabel.text.toString()

    val inputLayout = binding.etInput
    val ivOption = binding.ivOption
    var hint: String = ""
        set(value) {
            field = value
            binding.etAuto.hint = hint
        }
    var text: String = ""
        set(value) {
            field = value
            binding.etAuto.setText(value)
        }
        get() = binding.etAuto.text.toString()

    fun setEnable(state: Boolean) {
        binding.etAuto.isFocusable = state
        binding.etAuto.isEnabled = state
        binding.etAuto.isCursorVisible = state
        binding.etAuto.isFocusableInTouchMode = state
    }

    fun setOnIconClickListener(callback: () -> Unit) {
        binding.ivOption.setOnClickListener {
            callback.invoke()
        }
    }

    fun setOnFocusChange(param: () -> Unit?) {
        binding.etInput.editText?.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                param.invoke()
            }
        }
    }

    fun setInputType(inputType: Int) {
        binding.etAuto.inputType = inputType
    }

    fun setError(error: String?) {
        binding.etAuto.error = error
        binding.etAuto.requestFocus()
    }


    fun setMaxLength(length: Int) {
        binding.etAuto.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(length))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.etAuto.isFocusedByDefault = false
        } else
            binding.etAuto.isFocusable = false
    }

    fun addTextChangeListener(
        char: Int = 1,
        zip: Int,
        callback: KFunction2<String?, Int, Unit>,
        callBack2: (String?) -> Unit={},
        resetCallBack:()->Unit={}
    ) {
        binding.etAuto.addTextChangedListener {
            if (it?.length==0){
                resetCallBack.invoke()
            }
            if (it?.length!! > char && it.length<5)  {
                binding.autoProgress.show()
                callback.invoke(it.toString(),zip)
            }
            if (it.length==5)
                callBack2.invoke(it.toString())
        }
    }

    fun setOnItemSelectListener(callback: KFunction1<String?, Unit>){
        binding.etAuto.setOnItemClickListener { parent, view, position, id ->
            callback.invoke(adapter?.getItem(position))
        }
    }

    fun setPrefixText(text: String) {
        binding.etInput.prefixText = text

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.OviLabeledEditText,
            0,
            0
        ).apply {
            getInt(R.styleable.OviLabeledEditText_android_inputType, -1).let {
                if (it != -1) {
                    binding.etAuto.inputType = it
                }
            }
            getInt(R.styleable.OviLabeledEditText_android_maxLength, -1).let {
                if (it != -1) {
                    binding.etAuto.filters =
                        arrayOf<InputFilter>(InputFilter.LengthFilter(it))
                }
            }
            getString(R.styleable.OviLabeledEditText_prefixText).let {
                if (it != null) {
                    binding.etInput.prefixText = it
                }
            }
            getResourceId(R.styleable.OviLabeledEditText_endIconDrawable, -1).let {
                if (it != -1) {
                    binding.ivOption.show()
                    binding.ivOption.setImageDrawable(context.getOviDrawable(it))
                    binding.etAuto.isEnabled = false
                    binding.etAuto.isCursorVisible = false
                    binding.etAuto.isFocusableInTouchMode = false
                }
            }
            getBoolean(R.styleable.OviLabeledEditText_passwordToggleEnabled, false).let {
                binding.etInput.isPasswordVisibilityToggleEnabled = it

                if (it) {
//                    binding.etInput.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
//                    binding.etInput.setPasswordVisibilityToggleDrawable()
                    binding.ivOption.gone()
                }
            }
            getBoolean(R.styleable.OviLabeledEditText_readOnlyMode, false).let {
                binding.etAuto.isFocusable = !it
                binding.etAuto.isEnabled = !it
            }
            getColor(R.styleable.OviLabeledEditText_labelTextColor, -1).let {
                if (it != -1)
                    binding.tvLabel.setTextColor(it)
            }
            getColor(R.styleable.OviLabeledEditText_inputTextColor, -1).let {
                if (it != -1) {
                    binding.etAuto.setTextColor(it)
//                    binding.etInput.editText?.setHintTextColor(it)
                }
            }
            getString(R.styleable.OviLabeledEditText_labelText).let {
                binding.tvLabel.text = it
            }
            getString(R.styleable.OviLabeledEditText_android_hint).let {
                binding.etAuto.hint = it
            }
            getString(R.styleable.OviLabeledEditText_inputText).let {
                binding.etAuto.setText(it)
            }
            getInt(R.styleable.OviLabeledEditText_inputTextSize, 13).let {
                binding.etAuto.textSize = it.toFloat()
            }
            getInt(R.styleable.OviLabeledEditText_labelTextSize, 14).let {
                binding.tvLabel.textSize = it.toFloat()
            }
            getResourceId(
                R.styleable.OviLabeledEditText_android_background,
                R.drawable.bg_edit_text_white_brown_border
            ).let {
                binding.etAuto.background = context.getOviDrawable(it)
                background = null
            }
            getResourceId(
                R.styleable.OviLabeledEditText_android_fontFamily,
                R.font.opensans_semibold
            ).let {
                binding.tvLabel.typeface = context.getOviFont(it)
            }
        }
        binding.etAuto.setOnTouchListener { v, event ->
            setError(null)
            false
        }
    }
}