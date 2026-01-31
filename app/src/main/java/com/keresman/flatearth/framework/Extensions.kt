package com.keresman.flatearth.framework

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager

fun View.applyAnimation(animationId: Int)
    = startAnimation(AnimationUtils.loadAnimation(context, animationId))

// This is impossible code, unless information about the type is kept until the heap
// fun<T> Context.startActivity() = startActivity(Intent(this, T::class.java))

// reified -> keep type information, can only work as macro
// Activities should be started from activites, if started from other compoenent FLAG_ACTIVITY_NEW_TASK
// should be set
inline fun<reified T : Activity> Context.startActivity()
    = startActivity(Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })

inline fun<reified T : BroadcastReceiver> Context.sendBroadcast()
        = sendBroadcast(Intent(this, T::class.java))

//
//fun getBooleanProperty(key: String, context: Context) {
//    PreferenceManager.getDefaultSharedPreferencesName(context)
//}

// Could be generic function using same tricks from before
//fun Context.getBooleanProperty(key: String) {
//    PreferenceManager.getDefaultSharedPreferences(this)
//        .getBoolean(key, false)
//}

// Generic version using reified -> T type is known at compile time,
// so we can branch on T::class to call the correct SharedPreferences getter/putter
@Suppress("UNCHECKED_CAST")
inline fun <reified T> SharedPreferences.get(key: String, default: T): T =
    when (T::class) {
        Boolean::class -> getBoolean(key, default as Boolean) as T
        String::class -> getString(key, default as String) as T
        Int::class -> getInt(key, default as Int) as T
        Long::class -> getLong(key, default as Long) as T
        Float::class -> getFloat(key, default as Float) as T
        else -> throw UnsupportedOperationException("Unsupported type: ${T::class}")
    }

inline fun <reified T> SharedPreferences.Editor.put(key: String, value: T) {
    when (T::class) {
        Boolean::class -> putBoolean(key, value as Boolean)
        String::class -> putString(key, value as String)
        Int::class -> putInt(key, value as Int)
        Long::class -> putLong(key, value as Long)
        Float::class -> putFloat(key, value as Float)
        else -> throw UnsupportedOperationException("Unsupported type: ${T::class}")
    }
}

inline fun <reified T> Context.getProperty(key: String, default: T): T =
    PreferenceManager.getDefaultSharedPreferences(this).get(key, default)

//fun Context.setBooleanProperty(key: String, value: Boolean) : Unit {
//    PreferenceManager.getDefaultSharedPreferences(this)
//        .edit {
//            putBoolean(key, value)
//        }
//}


inline fun <reified T> Context.setProperty(key: String, value: T) {
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit { put(key, value) }
}

// hasAnyTransport() - vararg extension so we can check multiple transports in one call
// uses any() to return true if at least one transport matches
fun NetworkCapabilities.hasAnyTransport(vararg transports: Int): Boolean =
    transports.any { hasTransport(it) }

fun Context.isOnline() : Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>()

    connectivityManager?.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)?.let { capabilities ->
            return capabilities.hasAnyTransport(
                NetworkCapabilities.TRANSPORT_WIFI,
                NetworkCapabilities.TRANSPORT_CELLULAR
            )
        }
    }

    return false
}

fun callWithDelay(delay: Long, work: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(
        work,
        delay
    )
}