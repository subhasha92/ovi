package org.ovi.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import org.ovi.R
import org.ovi.databinding.WidgetEventToolbarBinding
import org.ovi.util.extensions.getOviColor
import org.ovi.util.extensions.px

class OviEventToolbar : ConstraintLayout {

    private lateinit var binding: WidgetEventToolbarBinding

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    var toolbarTitle: String = ""
        set(value) {
            field = value
            binding.tvTitle.text = value
        }

    fun setBackListener(callback: () -> Unit) {
        binding.ivBack.setOnClickListener {
            callback.invoke()
        }
    }


    @SuppressLint("CustomViewStyleable")
    private fun init(attrSet: AttributeSet) {
        binding = WidgetEventToolbarBinding.inflate(LayoutInflater.from(context), this, true)
        with(context.obtainStyledAttributes(attrSet, R.styleable.OviTitledToolbar)) {
            getInt(R.styleable.OviTitledToolbar_toolHeight, 90).let {
                if (it != -1)
                    binding.cLay.layoutParams.height = it.px
            }

            binding.tvTitle.apply {
                getText(R.styleable.OviTitledToolbar_title)?.let {
                    text = it
                }

                if (!isInEditMode) {
                    typeface = ResourcesCompat.getFont(
                        context, getResourceId(
                            R.styleable.OviTitledToolbar_android_fontFamily,
                            R.font.opensans_semibold
                        )
                    )
                    setTextColor(
                        getColor(
                            R.styleable.OviTitledToolbar_android_textColor, context.getOviColor(
                                R.color.toolbar_title_color
                            )
                        )
                    )
                }
            }
            recycle()
        }


        binding.ivBack.setOnClickListener {
        }

    }
}