package org.ovi.util.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast


@SuppressLint("NewApi")
fun Context.hasNetwork(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
    var isAvailable = false
    capabilities?.run {
        when {
            hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> isAvailable = true
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> isAvailable = true
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> isAvailable = true
            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> isAvailable = true
        }
    }
    return isAvailable





}

fun showToast(context: Context,text: String?) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}
