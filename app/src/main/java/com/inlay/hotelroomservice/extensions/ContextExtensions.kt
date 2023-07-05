package com.inlay.hotelroomservice.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
            it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || it.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            )
        } ?: false
    } else connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true
}