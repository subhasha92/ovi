package org.ovi.util.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.webkit.URLUtil
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import org.ovi.BuildConfig
import org.ovi.util.TransitionHelper
import java.io.File


fun Context.openWebLink(link: String) {
    if (URLUtil.isValidUrl(link)) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }
}

fun Context.dialNumber(mobNumber: String) {
    val intent = Intent(Intent.ACTION_CALL)
    intent.data = Uri.parse("tel:$mobNumber")
    startActivity(intent)
}

fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

fun Context.usePhoneViewer(url: String?): Boolean {
    val intent = Intent(Intent.ACTION_VIEW)
    val uri = Uri.parse(url)
    val mimeType = uri.getMimeTypeForIntent()
//    Log.d("IntentExt", "usePhoneViewer: mimeType $mimeType")
    intent.setDataAndType(uri, mimeType)

    kotlin.runCatching {
        startActivity(intent)
    }.onSuccess {
        return true
    }

    return false
}


fun Context.sendEmail(to: String, subject: String) {
    if (to.isValidEmail()) {
        val mailto = "mailto:" + to + "?subject=" + Uri.encode(subject)
        startActivity(Intent(Intent.ACTION_SENDTO).apply { data = Uri.parse(mailto) })
    } else {
        showToast("You do not have mail app installed!")
    }
}

fun Context.sendMessage(number: String) {
    startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.fromParts("sms", number, null)
        )
    )
}

fun Context.openMap(latitude: Double?, longitude: Double?, address: String?) {

    if (latitude == null || longitude == null)
        return

    val gmmIntentUri =
        Uri.parse("geo:$latitude,$longitude?q=" + Uri.encode(address))

    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    startActivity(mapIntent)
}

fun Context.openImage(filePath: String) {

    val file = File(filePath)
    if (file.exists()) {
        val imageIntent = Intent()
        imageIntent.apply {
            action = Intent.ACTION_VIEW
            setDataAndType(Uri.parse("file://$filePath"), "image/*")
        }
        startActivity(imageIntent)
    }
}

fun Bundle?.toString(logTag: String? = null) {

    if (this == null) {
//        Log.d(logTag.plus("bundleToString"), "args null")
        return
    }

    for (key in this.keySet()) {
//        Log.d(logTag.plus("bundleToString"), "Key -> $key value -> ${this.get(key)}")
    }
}

fun Intent?.toString(logTag: String? = null) {
    if (this == null) {
//        Log.d(logTag.plus("IntentToString"), "intent null")
        return
    }
    val stringBuilder = StringBuilder("action: ")
        .append(this.action)
        .append(" data: ")
        .append(this.dataString)
        .append(" extras: ")

    this.extras?.let {
        for (key in it.keySet()) stringBuilder.append(key)
            .append("=").append(it[key]).append(" ")
    }

//    Log.d("IntentToString", stringBuilder.toString())
}

@SuppressLint("QueryPermissionsNeeded")
fun Context.phoneHasSupportedApp(url: String?): Boolean {
    val intent = Intent(Intent.ACTION_VIEW)
    val uri = Uri.parse(url)
    val mimeType = uri.getMimeTypeForIntent()
//    Log.d("IntentExt", "usePhoneViewer: mimeType $mimeType")
    intent.setDataAndType(uri, mimeType)
    return intent.resolveActivity(this.packageManager) != null
}

fun Context.shareFile(filePath: String?) {

    filePath?.let { path ->
        val file = File(path)
//        Log.d("Intent", "shareFile: exists ${file.exists()} uri ${Uri.fromFile(file)}")
        if (!file.exists())
            return

        val uri = FileProvider.getUriForFile(this, "${org.ovi.BuildConfig.APPLICATION_ID}.provider", file)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = uri.getMimeTypeForIntent()
            putExtra(Intent.EXTRA_STREAM, uri)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivity(Intent.createChooser(shareIntent, "Share app using"))
    }
}


fun MutableMap<String, String>?.toString(logTag: String? = null) {
    if (this == null) {
//        Log.d(logTag.plus("mapToString"), "intent null")
        return
    }

    for ((key, value) in entries) {
//        Log.d(logTag.plus("mapToString"), "key, $key value $value")
    }
}

fun Intent.openActivity(context: Context?) {
    context?.startActivity(this)
}

fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun <T> FragmentActivity.openActivityForResult(
    it: Class<T>,
    requestCode: Int,
    extras: Bundle.() -> Unit = {}
) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivityForResult(intent, requestCode, getTransitionOptions(this))
}

fun <T> Context.openActivityWithTransition(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent, getTransitionOptions(this))
}


fun <T> Context.openActivityWithClearFlag(
    it: Class<T>,
    extras: Bundle.() -> Unit = {}
) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent, getTransitionOptions(this))
}


fun getTransitionOptions(context: Context): Bundle{
    val pairs = org.ovi.util.TransitionHelper.createSafeTransitionParticipants(context as Activity, true)
    return ActivityOptionsCompat.makeSceneTransitionAnimation(context, *pairs).toBundle()!!
}