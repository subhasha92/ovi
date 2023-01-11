package org.ovi.widget

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.ovi.R
import org.ovi.util.extensions.dp
import org.ovi.util.extensions.findSuitableParent
import org.ovi.util.extensions.px
import org.ovi.util.extensions.show


class OviSnackBar(
    parent: ViewGroup,
    content: CustomSnackbar
) : BaseTransientBottomBar<OviSnackBar>(parent, content, content) {


    fun showSnack(): OviSnackBar {
        show()
        return this
    }

    init {
        getView().setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                android.R.color.transparent
            )
        )
        when(snackModel.type){
            SnackType.SUCCESS -> {
                val params =getView().layoutParams as FrameLayout.LayoutParams
                params.gravity=Gravity.TOP
                params.topMargin=120.px
                getView().layoutParams=params
            }
            SnackType.FAILURE -> {}
            SnackType.VALIDATION -> {}
            null -> {}
        }

        getView().setPadding(0, 0, 0, 0)
    }

    companion object {
        val snackModel = HaicaSnackDTO()
    }

    enum class SnackType {
        SUCCESS,
        FAILURE,
        VALIDATION
    }

    class Builder {

        private var iSnackListener: ISnackListener? = null

        interface ISnackListener {
            fun onClosed(view: View)
        }

        fun type(type: SnackType): Builder {
            snackModel.type = type
            return this
        }

        fun message(msg: String?): Builder {
            snackModel.message = msg
            return this
        }

        fun setCallBack(listener: ISnackListener): Builder {
            iSnackListener = listener
            return this
        }

        fun make(view: View): OviSnackBar {

            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            val customView = LayoutInflater.from(view.context).inflate(
                R.layout.widget_singer_snackbar,
                parent,
                false
            ) as CustomSnackbar


            customView.snackText.setOnClickListener {
                iSnackListener?.onClosed(it)
            }

            val snackBarLay = customView.findViewById<ConstraintLayout>(R.id.appSnackbarLay)

            var tColor: Int? = null
            var bgRes: Drawable? = null


            when (snackModel.type) {
                SnackType.SUCCESS -> {

                    tColor = R.color.light_green
                    snackBarLay.findViewById<AppCompatImageView>(R.id.ivIcon).show()
                    bgRes =
                        ContextCompat.getDrawable(customView.context, R.drawable.bg_green_bordered)
                    snackBarLay.findViewById<AppCompatTextView>(R.id.tvSnackText).textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                }
                SnackType.FAILURE -> {
                    tColor = R.color.snackColorRed
                    bgRes = ContextCompat.getDrawable(customView.context, R.drawable.snack_error)
                }
                SnackType.VALIDATION -> {
                    tColor = R.color.snackColorYellow
                    bgRes =
                        ContextCompat.getDrawable(customView.context, R.drawable.snack_validation)
                }
                null -> {

                }
            }

            snackBarLay?.let { sLay ->
                val text = sLay.findViewById<AppCompatTextView>(R.id.tvSnackText)
                tColor?.let {
                    text.setTextColor(getColor(view.context, it))
                }
                bgRes?.let { sLay.background = it }
                snackModel.message?.let {
                    text.text = it

                }
            }

            return OviSnackBar(
                parent,
                customView
            )
        }

        fun showSnack(): Any {
            return this
        }

        fun dismiss() {
        }
    }

}

@JsonClass(generateAdapter = true)
data class HaicaSnackDTO(
    @Json(name = "type")
    var type: OviSnackBar.SnackType? = null,
    @Json(name = "message")
    var message: String? = null
)