package org.ovi.util.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import org.ovi.util.CustomTypefaceSpan
import java.text.SimpleDateFormat
import java.util.*

/**
 * @param sizeInDp Integer size to apply for the span.
 * This considers the given size in DP
 * @param start Starting Index of the string.
 * @param end Ending Index of the span + 1.
 */
fun SpannableString.setAbsoluteSpan(
    sizeInDp: Int,
    start: Int,
    end: Int
) {
    this.setSpan(
        AbsoluteSizeSpan(sizeInDp, true),
        start,
        end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}

/**
 * This applies the given font and sets a BOLD style.
 *
 * @param fontId Resource ID of the font to apply for the span.
 * @param start Starting Index of the string.
 * @param end Ending Index of the span + 1.
 */
fun SpannableString.setCustomTypefaceSpanBold(
    fontId: Int,
    context: Context,
    start: Int,
    end: Int
) {
    val typeface = Typeface.create(
        ResourcesCompat.getFont(context, fontId),
        Typeface.BOLD
    )

    this.setSpan(
        CustomTypefaceSpan(typeface),
        start,
        end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}

/**
 * This applies the given font but does not affect any style.
 *
 * @param fontId Resource ID of the font to apply for the span.
 * @param start Starting Index of the string.
 * @param end Ending Index of the span + 1.
 */
fun SpannableString.setCustomTypefaceSpanNormal(
    fontId: Int,
    context: Context,
    start: Int,
    end: Int
) {
    val typeface = Typeface.create(
        ResourcesCompat.getFont(context, fontId),
        Typeface.NORMAL
    )

    this.setSpan(
        CustomTypefaceSpan(typeface),
        start,
        end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}

/**
 * @param colorId Resource ID of the color to apply for the span.
 * @param start Starting Index of the string.
 * @param end Ending Index of the span + 1.
 */
fun SpannableString.setForegroundColorSpan(
    colorId: Int,
    context: Context,
    start: Int,
    end: Int
) {
    val color = ContextCompat.getColor(context, colorId)
    this.setSpan(
        ForegroundColorSpan(color),
        start,
        end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}

/**
 * @param typefaceStyle Can be one of Typeface.BOLD, Typeface.NORMAL, Typeface.ITALIC
 * @param start Starting Index of the string.
 * @param end Ending Index of the span + 1.
 */
fun SpannableString.setStyleSpan(
    typefaceStyle: Int,
    start: Int,
    end: Int
) {
    this.setSpan(
        StyleSpan(typefaceStyle),
        start,
        end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}

fun String.capitalizeWords(): String = split(" ").joinToString(" ") {
    it.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }
}


@SuppressLint("SimpleDateFormat")
fun String.setIsoDateFormat(): String? {
    val date = SimpleDateFormat("yyyy-MM-dd").parse(this)
    val format = SimpleDateFormat("dd, MMM yyyy")
    return date?.let { format.format(it) }
}

@SuppressLint("SimpleDateFormat")
fun String.getIsoDateFormat(): String? {
    val date = SimpleDateFormat("dd, MMM yyyy").parse(this)
    val format = SimpleDateFormat("yyyy-MM-dd")
    return date?.let { format.format(it) }
}



@SuppressLint("SimpleDateFormat")
fun String.getFormatedDateToDisplay(): String? {
    val convertFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    convertFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = convertFormat.parse(this)
    val format = SimpleDateFormat("MMM dd,yyyy")
    return date?.let { format.format(it) }
}

@SuppressLint("SimpleDateFormat")
fun String.getFormatedValue(value:String): String? {
    val convertFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    convertFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = convertFormat.parse(this)
    val format = SimpleDateFormat(value)
    return date?.let { format.format(it) }
}


@SuppressLint("SimpleDateFormat")
fun String.getTimeIn12hrsFormat(): String? {
    val convertFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    convertFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = convertFormat.parse(this)
    val format = SimpleDateFormat("h:mm a")
    return date?.let { format.format(it) }
}

@SuppressLint("SimpleDateFormat")
fun String.getDateForBarChart(): String? {
    val convertFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    convertFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = convertFormat.parse(this)
    val format = SimpleDateFormat("MMM d")
    return date?.let { format.format(it) }
}

@SuppressLint("SimpleDateFormat")
fun String.getDate(): Date? {
    val convertFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    convertFormat.timeZone = TimeZone.getTimeZone("UTC")
    return (convertFormat.parse(this))
}


fun String.checkEmail():Boolean{
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this)
        .matches()
}