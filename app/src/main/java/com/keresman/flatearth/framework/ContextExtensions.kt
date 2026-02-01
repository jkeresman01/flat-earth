package com.keresman.flatearth.framework

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager

inline fun<reified T : Activity> Context.startActivity()
    = startActivity(Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })

inline fun<reified T : BroadcastReceiver> Context.sendBroadcast()
        = sendBroadcast(Intent(this, T::class.java))

inline fun <reified T> Context.getProperty(key: String, default: T): T =
    PreferenceManager.getDefaultSharedPreferences(this).get(key, default)

inline fun <reified T> Context.setProperty(key: String, value: T) {
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit { put(key, value) }
}

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>() ?: return false
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    return capabilities.hasAnyTransport(
        NetworkCapabilities.TRANSPORT_WIFI,
        NetworkCapabilities.TRANSPORT_CELLULAR
    )
}

//NOTES:
//
// This is impossible code, unless information about the type is kept until the heap
// fun<T> Context.startActivity() = startActivity(Intent(this, T::class.java))

// reified -> keep type information, can only work as macro
// Activities should be started from activites, if started from other compoenent FLAG_ACTIVITY_NEW_TASK
// should be set
//
//
//fun getBooleanProperty(key: String, context: Context) {
//    PreferenceManager.getDefaultSharedPreferencesName(context)
//}
//
// Could be generic function using same tricks from before
//fun Context.getBooleanProperty(key: String) {
//    PreferenceManager.getDefaultSharedPreferences(this)
//        .getBoolean(key, false)
//}
//
// Generic version using reified -> T type is known at compile time,
// so we can branch on T::class to call the correct SharedPreferences getter/putter
//
//fun Context.setBooleanProperty(key: String, value: Boolean) : Unit {
//    PreferenceManager.getDefaultSharedPreferences(this)
//        .edit {
//            putBoolean(key, value)
//        }
//}
