package com.keresman.flatearth.utils

import android.os.Handler
import android.os.Looper


fun callWithDelay(delay: Long, work: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(
        work,
        delay
    )
}