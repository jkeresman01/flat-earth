package com.keresman.flatearth.framework

import android.content.SharedPreferences


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
