package org.ovi.widget


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import org.ovi.databinding.WidgetOviOtpBoxBinding


class OviOtpBoxEdittext : LinearLayout {

    private val binding by lazy {
        WidgetOviOtpBoxBinding.inflate(LayoutInflater.from(context), this, true)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }
    var edit =arrayOf<AppCompatEditText>()

    fun getText():String{
        val string= StringBuilder()
         edit.forEach {
            string.append(it.text.toString().trim())
        }
        return string.toString()
    }

    private fun init(attrSet: AttributeSet) {

        with(binding)
        {
         edit =
            arrayOf<AppCompatEditText>(
                etOtpBox1,
                etOtpBox2,
                etOtpBox3,
                etOtpBox4,
                etOtpBox5,
                etOtpBox6
            )

            edit.forEach {
                it.addTextChangedListener(GenericTextWatcher(it, edit))
            }
        }


    }


    inner class GenericTextWatcher(view: View, private val editText: Array<AppCompatEditText>) :
        TextWatcher {
        private val view: View
        override fun afterTextChanged(editable: Editable) {
            val text = editable.toString()
            when (view) {
                binding.etOtpBox1 -> if (text.length == 1) editText[1].requestFocus()
                binding.etOtpBox2 -> if (text.length == 1) editText[2].requestFocus() else if (text.isEmpty()) editText[0].requestFocus()
                binding.etOtpBox3 -> if (text.length == 1) editText[3].requestFocus() else if (text.isEmpty()) editText[1].requestFocus()
                binding.etOtpBox4 -> if (text.length == 1) editText[4].requestFocus()
                binding.etOtpBox5 -> if (text.length == 1) editText[5].requestFocus()
                binding.etOtpBox6 -> {}
            }
        }

        override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
        override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}

        init {
            this.view = view
        }
    }


}
