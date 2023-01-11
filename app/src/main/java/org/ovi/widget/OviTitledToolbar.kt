package org.ovi.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import org.ovi.R
import org.ovi.databinding.WidgetTitledToolbarBinding
import org.ovi.util.extensions.getOviColor
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.px
import org.ovi.util.extensions.show

class OviTitledToolbar: ConstraintLayout {

    private lateinit var binding: WidgetTitledToolbarBinding

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

    fun setEndIconListener(callback: () -> Unit) {
        binding.ivOption.setOnClickListener {
            callback.invoke()
        }
    }

    private fun init(attrSet: AttributeSet) {
        binding = WidgetTitledToolbarBinding.inflate(LayoutInflater.from(context), this, true)
        with(context.obtainStyledAttributes(attrSet, R.styleable.OviTitledToolbar)) {
            getInt(R.styleable.OviTitledToolbar_toolHeight, 90).let {
                if (it != -1)
                    binding.cLay.layoutParams.height = it.px
            }

            binding.ivBack.apply {
                if (getBoolean(R.styleable.OviTitledToolbar_backIconEnabled, true))
                    show()
                else gone()
            }
            binding.tvTitle.apply {  if (getBoolean(R.styleable.OviTitledToolbar_hideTitle, false))
                gone()
            else show() }
            binding.tvTitle.apply {
                getText(R.styleable.OviTitledToolbar_title)?.let {
                    text = it
                }
                getResourceId(
                    R.styleable.OviTitledToolbar_endOptionIcon,
                    -1
                ).let {
                    if (it == -1) {
                        binding.ivOption.gone()
                    } else {
                        binding.ivOption.apply {
                            setImageResource(it)
                            show()
                        }
                    }
                    getBoolean(R.styleable.OviTitledToolbar_hideIcon,false).let {
                        if (it)
                            binding.tvTitle.setCompoundDrawables(null,null,null,null)
                    }
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
        binding.ivOption.setOnClickListener { }


        binding.ivBack.setOnClickListener {
        }

    }
}