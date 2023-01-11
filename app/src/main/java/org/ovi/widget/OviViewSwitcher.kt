package org.ovi.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ViewSwitcher
import org.ovi.R

class OviViewSwitcher: ViewSwitcher {

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(
        context: Context?, attrs: AttributeSet?
    ) : super(context, attrs) {
        init()
    }

    private fun init() {
        setInAnimation(context, R.anim.fade_in)
        setOutAnimation(context, R.anim.fade_out)
    }

    fun setChildVisible() {
        if (displayedChild != 1) {
            displayedChild = 1
        }
    }

    fun setParentVisible() {
        if (displayedChild != 0) {
            displayedChild = 0
        }
    }

}