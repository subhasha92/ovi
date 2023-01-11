package org.ovi.util.popup.internal

import android.content.Context
import android.widget.PopupWindow

internal fun createAppCompatPopupWindow(context: Context): PopupWindow = PopupWindow(context, null, 0)
