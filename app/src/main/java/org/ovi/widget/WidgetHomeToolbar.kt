package org.ovi.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import org.ovi.R
import org.ovi.databinding.WidgetMainToolbarBinding

class WidgetHomeToolbar:ConstraintLayout {

    private val binding by lazy { WidgetMainToolbarBinding.inflate(LayoutInflater.from(context),this,true) }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrSet: AttributeSet) {

        with(context.obtainStyledAttributes(attrSet, R.styleable.WidgetHomeToolbar)){
            binding.tvTitle.setText(getString(
                R.styleable.WidgetHomeToolbar_toolbarTitle
            ))
        }
    }
}