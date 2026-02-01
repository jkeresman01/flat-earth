package com.keresman.flatearth.framework

import android.net.NetworkCapabilities


// hasAnyTransport() - vararg extension so we can check multiple transports in one call
// uses any() to return true if at least one transport matches
fun NetworkCapabilities.hasAnyTransport(vararg transports: Int): Boolean =
    transports.any { hasTransport(it) }
