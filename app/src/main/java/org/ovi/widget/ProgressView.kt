package org.ovi.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import org.ovi.R
import org.ovi.databinding.WidgetProgressViewBinding
import org.ovi.util.extensions.gone
import org.ovi.util.extensions.show
import org.ovi.util.extensions.toDp

class ProgressView : FrameLayout {

    lateinit var binding: WidgetProgressViewBinding

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun show() {
        binding.progressLayout.post {
            binding.progressLayout.show()
        }
    }

    fun hide() {
        binding.progressLayout.post {
            binding.progressLayout.gone()
        }
    }

    private fun init(attrs: AttributeSet) {
        binding = WidgetProgressViewBinding.inflate(LayoutInflater.from(context), this, true)
        with(context.obtainStyledAttributes(attrs, R.styleable.ProgressView)) {
            getColor(R.styleable.ProgressView_progressColor, -1).let {
                if (it != -1)
                    binding.pBar.indeterminateTintList = ColorStateList.valueOf(it)
            }
        }
        ViewCompat.setTranslationZ(this.rootView, 8f.toDp(resources.displayMetrics))
    }


}