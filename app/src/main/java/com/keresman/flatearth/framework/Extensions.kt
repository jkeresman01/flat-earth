package com.keresman.flatearth.framework

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.animation.AnimationUtils

fun View.applyAnimation(animationId: Int)
    = startAnimation(AnimationUtils.loadAnimation(context, animationId))

// This is impossible code, unless information about the type is kept until the heap
// fun<T> Context.startActivity() = startActivity(Intent(this, T::class.java))

// reified -> keep type information, can only work as macro
inline fun<reified T : Activity> Context.startActivity()
    = startActivity(Intent(this, T::class.java))

