package org.ovi.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import org.ovi.R
import org.ovi.databinding.WidgetOviLabelledEdittextBinding
import org.ovi.util.extensions.*


class OviLabeledEditText : ConstraintLayout {

    private val binding by lazy {
        WidgetOviLabelledEdittextBinding.inflate(LayoutInflater.from(context), this, true)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    var isEndIconVisible = false

    var label: String = ""
        set(value) {
            field = value
            binding.tvLabel.text = value
        }
        get() = binding.tvLabel.text.toString()

    val editText = binding.etInput.editText
    val inputLayout = binding.etInput
    val ivOption = binding.ivOption
    var hint: String = ""
        set(value) {
            field = value
            binding.etInput.editText?.hint = hint
        }
    var text: String = ""
        set(value) {
            field = value
            binding.etInput.editText?.setText(value)
        }
        get() = binding.etInput.editText?.text.toString()

    fun setEnable(state: Boolean) {
        binding.etInput.editText?.isFocusable = state
        binding.etInput.editText?.isEnabled = state
        binding.etInput.editText?.isCursorVisible = state
        binding.etInput.editText?.isFocusableInTouchMode = state
    }

    fun setOnIconClickListener(callback: () -> Unit) {
        binding.ivOption.setOnClickListener {
            callback.invoke()
        }
    }

    fun setInputType(inputType: Int) {
        binding.etInput.editText?.inputType = inputType
    }

    fun setError(error: String?) {
//        val customErrorDrawable = context.getOviDrawable(R.drawable.ic_baseline_error_24)
//        customErrorDrawable?.setBounds(
//            -40,
//            0,
//            customErrorDrawable.intrinsicWidth,
//            customErrorDrawable.intrinsicHeight
//        )
//        binding.etInput.editText?.setPadding(14.px, 14.px, if (isEndIconVisible) 60.px else 40.px, 14.px)
        binding.etInput.editText?.setPadding(
            binding.etInput.editText?.paddingLeft!!,
            binding.etInput.editText?.paddingTop!!,
            if (isEndIconVisible) 60.px else 40.px,
            binding.etInput.editText?.paddingBottom!!
        )
//        if (error == null)
//            binding.etInput.editText?.setPadding(0, 0, 0, 0)
        binding.etInput.helperText = error
        binding.etInput.setHelperTextTextAppearance(R.style.ErrorTextAppearance)
        binding.etInput.editText?.error = error //,customErrorDrawable)
        binding.etInput.editText?.requestFocus()
    }

    fun setMaxLength(length: Int) {
        binding.etInput.editText?.filters =
            arrayOf<InputFilter>(LengthFilter(length))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.etInput.editText?.isFocusedByDefault = false
        } else
            binding.etInput.editText?.isFocusable = false
    }

    fun setPrefixText(text: String) {
        binding.etInput.prefixText = text

    }

    fun setOnFocusChange(param: () -> Unit?) {
        binding.etInput.editText?.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                param.invoke()
            }
        }
    }

    fun setEndIconDrawable(icon: Int? = null) {
        binding.ivOption.show()
        isEndIconVisible = true
        setEnable(false)
        if (icon == null)
            binding.ivOption.setImageDrawable(context.getOviDrawable(R.drawable.ic_fi_calendar))
        else
            binding.ivOption.setImageDrawable(context.getOviDrawable(icon))

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
                    binding.etInput.editText?.inputType = it
                }
            }

            getInt(R.styleable.OviLabeledEditText_android_maxLength, -1).let {
                if (it != -1) {
                    binding.etInput.editText?.filters =
                        arrayOf<InputFilter>(LengthFilter(it))
                }
            }

            getString(R.styleable.OviLabeledEditText_prefixText).let {
                if (it != null) {
                    binding.etInput.prefixText = it
                }
            }

            getResourceId(R.styleable.OviLabeledEditText_endIconDrawable, -1).let {
                if (it != -1) {
                    setEndIconDrawable(it)
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
                binding.etInput.editText?.isFocusable = !it
                binding.etInput.editText?.isEnabled = !it
            }
            getBoolean(R.styleable.OviLabeledEditText_labelVisibility, true).let {
                if (it)
                    binding.tvLabel.show()
                else
                    binding.tvLabel.gone()
            }


            getColor(R.styleable.OviLabeledEditText_labelTextColor, -1).let {
                if (it != -1)
                    binding.tvLabel.setTextColor(it)
            }
            getColor(R.styleable.OviLabeledEditText_inputTextColor, -1).let {
                if (it != -1) {
                    binding.etInput.editText?.setTextColor(it)
//                    binding.etInput.editText?.setHintTextColor(it)
                }
            }
            getString(R.styleable.OviLabeledEditText_labelText).let {
                binding.tvLabel.text = it
            }
            getString(R.styleable.OviLabeledEditText_android_hint).let {
                binding.etInput.editText?.hint = it
            }
            getString(R.styleable.OviLabeledEditText_inputText).let {
                binding.etInput.editText?.setText(it)
            }
            getInt(R.styleable.OviLabeledEditText_inputTextSize, 13).let {
                binding.etInput.editText?.textSize = it.toFloat()
            }
            getInt(R.styleable.OviLabeledEditText_labelTextSize, 14).let {
                binding.tvLabel.textSize = it.toFloat()
            }

            getResourceId(
                R.styleable.OviLabeledEditText_labelFont,
                R.font.opensans_semibold
            ).let {
                binding.tvLabel.typeface = context.getOviFont(it)
            }
            getResourceId(
                R.styleable.OviLabeledEditText_android_background,
                R.drawable.bg_edit_text_white_brown_border
            ).let {
                binding.etInput.editText?.background = context.getOviDrawable(it)
                background = null
            }

            getResourceId(
                R.styleable.OviLabeledEditText_android_fontFamily,
                R.font.opensans_semibold
            ).let {
                binding.tvLabel.typeface = context.getOviFont(it)
            }
        }

        binding.etInput.editText?.setOnTouchListener { v, _ ->
            setError(null)
            v.focusAndShowKeyboard()
            v.requestFocusFromTouch()
            false
        }

    }
}