package org.ovi.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import org.ovi.R
import org.ovi.databinding.WidgetImageAndTextBinding
import org.ovi.util.extensions.setImage

class WidgetImageAndText : ConstraintLayout {


    private val binding by lazy {
        WidgetImageAndTextBinding.inflate(LayoutInflater.from(context), this, true)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun setOnClickListener(callback: () -> Unit) {
        binding.clayParent.setOnClickListener {
            callback.invoke()
        }
    }

    var label: String = ""
        set(value) {
            field = value
            binding.tvTitle.text = value
        }
        get() = binding.tvTitle.text.toString()

    fun setImageUrl(url:String){
        binding.ivImage.setImage(url,R.drawable.ic_my_profile_img)
    }

    private fun init(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.WidgetImageAndText,
            0,
            0
        ).apply {

            getString(R.styleable.WidgetImageAndText_eventTitle).let {
                binding.tvTitle.text = it
            }

            getResourceId(R.styleable.WidgetImageAndText_imageSrc, -1).let {
                if (it != -1)
                    binding.ivImage.setImageResource(it)
            }
        }

    }

}