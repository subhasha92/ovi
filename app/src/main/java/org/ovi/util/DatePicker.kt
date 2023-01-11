package org.ovi.util

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import org.ovi.widget.OviLabeledEditText
import java.text.SimpleDateFormat
import java.util.*


object DatePicker {

    private const val CHILD_YRS = -13
    private const val ADULT_YRS = -18

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun dateSetListener(v: View, type: Int, role: String? = null) {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)

        val format = SimpleDateFormat("dd, MMM yyyy")
        v as OviLabeledEditText
        val datepickerdialog = DatePickerDialog(
            v.context,
            { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                // Display Selected date in textbox
                v.editText?.setText("")
                v.editText?.setText(format.format(cal.time))
                if (type == 2) {
                    timePicker(v, false)
                }
            },
            y,
            m,
            d
        )
        if (type == 1) {
//            Log.e(TAG, "dateSetListener: $role", )
            datepickerdialog.datePicker.calendarViewShown = false
            cal.add(Calendar.YEAR, if (role?.contains("youth",true) == true) CHILD_YRS else ADULT_YRS)
            datepickerdialog.datePicker.maxDate = cal.timeInMillis
        }
        datepickerdialog.show()
    }

    fun timePicker(v: View, editable: Boolean) {
        // Get Current Time
        // Get Current Time
        v as OviLabeledEditText
        val c = Calendar.getInstance()
        var mHour = c[Calendar.HOUR_OF_DAY]
        var mMinute = c[Calendar.MINUTE]

        // Launch Time Picker Dialog

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            v.context,
            { view, hourOfDay, minute ->
                mHour = hourOfDay
                mMinute = minute
                if (editable)
                    v.editText?.setText("")
                var dt: String? = null
                dt = if (editable)
                    String.format("%02d:%02d", hourOfDay, minute)
                else
                    v.editText?.text.toString()
                        .plus(String.format(" %02d:%02d", hourOfDay, minute))
                v.editText?.setText(dt)
            }, mHour, mMinute, true
        )
        timePickerDialog.show()

    }
}