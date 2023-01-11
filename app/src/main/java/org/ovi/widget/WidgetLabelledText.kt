package org.ovi.widget

import android.R.attr.button
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import org.ovi.R
import org.ovi.databinding.WidgetLabelledTextBinding


class WidgetLabelledText : ConstraintLayout {

    private val binding by lazy {
        WidgetLabelledTextBinding.inflate(LayoutInflater.from(context), this, true)
    }

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
        }
        get() = binding.tvLabel.text.toString()


    fun setUnderlineText(){
        binding.tvOption.apply {
            paintFlags=paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }
    }
     fun removeUnderlineText(){
        binding.tvOption.apply {
            paintFlags= paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
        }
    }



    var text: String = ""
        set(value) {
            field = value
            binding.tvOption.text = value
        }
        get() = binding.tvOption.text.toString()


    private fun init(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.OviLabeledEditText,
            0,
            0
        ).apply {


            getString(R.styleable.OviLabeledEditText_labelText).let {
                binding.tvLabel.text = it
            }

            getString(R.styleable.OviLabeledEditText_inputText).let {
                binding.tvOption.text = it
            }

            getInt(R.styleable.OviLabeledEditText_labelTextSize, 14).let {
                binding.tvLabel.textSize = it.toFloat()
            }

        }


    }
}