package org.ovi.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import org.ovi.R
import org.ovi.util.extensions.getOviColor
import org.ovi.util.extensions.getOviDrawable
import org.ovi.util.extensions.px

class OviButton:AppCompatButton {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrSet: AttributeSet) {
        isLongClickable = false
        isAllCaps = false
        stateListAnimator = null

        val typedArray = context.obtainStyledAttributes(attrSet, R.styleable.OviButton)
        background = context.getOviDrawable(R.drawable.selecter_ovi_button)

        with(typedArray) {
            typeface = ResourcesCompat.getFont(context, getResourceId(R.styleable.OviButton_android_fontFamily,R.font.opensans_semibold))
            setTextColor(getColor(R.styleable.OviButton_android_textColor,context.getOviColor(R.color.white_1000)))
            getDrawable(R.styleable.OviButton_android_background)?.let{
                background = it
            } ?: kotlin.run {
                val shape = Shape.values()[getInt(R.styleable.OviButton_bgShape, 0)]
                background = context.getOviDrawable(if (shape == Shape.ROUND_RECT) R.drawable.selector_ovi_button_rounded
                else R.drawable.selecter_ovi_button)
            }

        }

        typedArray.recycle()
    }

    fun setEnable(state:Boolean){
        isEnabled = state
        if (!state)
            alpha=.2f
        else
            alpha=1f
    }



    enum class Shape {
        RECT,
        ROUND_RECT
    }

}