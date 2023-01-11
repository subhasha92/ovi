package org.ovi.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.text.InputFilter
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import org.ovi.R
import org.ovi.databinding.WidgetSwitchEditBinding
import org.ovi.util.extensions.*


class OviSwitchEdit : ConstraintLayout {

    private lateinit var binding: WidgetSwitchEditBinding

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    var label: String = ""
        set(value) {
            field = value
            binding.tvLabel.text = value
            binding.tvLabelInput.text = value
            binding.acText.label = value
        }
        get() = binding.tvLabel.text.toString()

    fun enableAutoComplete(enable: Boolean) {
        if (enable) {
            binding.acText.show()
            binding.lLayTop.gone()
            binding.cLayBottom.gone()
        }
    }

    var text: String = ""
        set(value) {
            field = value
            binding.tvOption.text = value
            binding.acText.text = value
//            binding.etInput.editText?.setText(value)
        }
        get() = binding.tvOption.text.toString()

    fun setError(msg: String?) {
        binding.etInput.editText?.error = msg
        binding.etInput.editText?.requestFocus()
    }

    fun setMaxLength(length: Int) {
        binding.etInput.editText?.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(length))
    }

    val editText by lazy { binding.etInput.editText }
    val inputLayout by lazy { binding.etInput }

    val oviDropDown by lazy { binding.ddDrop }

    val autoComplete by lazy { binding.acText }

    fun setLabelTextColor(res1:Int, res2:Int){
        if (res1!=-1){

            binding.tvLabel.setTextColor(context.getOviColor(res1))
        }
        if (res2!=-1){

            binding.tvOption.setTextColor(context.getOviColor(res2))
        }
    }

    private fun init(attrSet: AttributeSet) {
        binding = WidgetSwitchEditBinding.inflate(LayoutInflater.from(context), this, true)
        context.theme.obtainStyledAttributes(
            attrSet,
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
                        arrayOf<InputFilter>(InputFilter.LengthFilter(it))
                }
            }
            getResourceId(R.styleable.OviLabeledEditText_endIconDrawable, -1).let {
                if (it != -1) {
                    binding.ivOption.show()
                    binding.ivOption.setImageDrawable(context.getOviDrawable(it))
                }
            }
            getResourceId(R.styleable.OviLabeledEditText_android_fontFamily, -1).let {
                if (it != -1) {
                    val typeface = context.getOviFont(it)
                    binding.tvLabel.typeface = typeface
//                    binding.tvOption.typeface = typeface
                }
            }

            getBoolean(R.styleable.OviLabeledEditText_readOnlyMode, true).let {
                setEditModeVisiblity(it)
            }
            getString(R.styleable.OviLabeledEditText_labelText).let {
                binding.tvLabel.text = it
                binding.tvLabelInput.text = it.toString()
            }
            getColor(R.styleable.OviLabeledEditText_labelTextColor, -1).let {
                if (it != -1) {
                    binding.tvLabel.setTextColor(it)
                    binding.tvOption.setTextColor(it)
                }
            }

            getString(R.styleable.OviLabeledEditText_inputText).let {
                binding.etInput.editText?.setText(it)
                binding.tvOption.text = it
            }
        }

        binding.etInput.editText?.addTextChangedListener {
            text = it.toString()
            setError(null)
        }


    }

    private fun setTopMargin(i: Int) {
        val r: Resources = context.resources
        when (i) {
            0 -> {

//                val pxMargin = TypedValue.applyDimension(
//                    TypedValue.COMPLEX_UNIT_DIP,
//                    20f,
//                    r.displayMetrics
//                ).toInt()
                setMargins(binding.vsET, 0, 0, 0, 0)

            }
            1 -> {
                val px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    16f,
                    r.displayMetrics
                ).toInt()
                setMargins(binding.vsET, 0, px, 0, 0)
            }
        }

    }

    private fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        v.layoutParams as ConstraintLayout.LayoutParams
        if (v.layoutParams is MarginLayoutParams) {
            val p = v.layoutParams as MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
        }
    }

    fun setDropMode(mode: Boolean) {
        if (mode) {
            binding.lLayTop.gone()
            binding.cLayBottom.gone()
            binding.ddDrop.show()
        }
    }

    fun setEditModeVisiblity(state: Boolean) {
        if (state) {
            binding.lLayTop.show()
            binding.cLayBottom.gone()
//            setTopMargin(0)
        } else {
            binding.lLayTop.gone()
            binding.cLayBottom.show()
//            setTopMargin(1)
        }

    }


}