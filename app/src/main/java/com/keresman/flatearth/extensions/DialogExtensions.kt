
package com.keresman.flatearth.extensions

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.keresman.flatearth.R

// NOTE: Decided to put Domain-related extensions in this (extensions) package.
fun Activity.showExitDialog(onConfirm: () -> Unit) {
    AlertDialog.Builder(this).apply {
        setTitle(R.string.exit)
        setMessage(getString(R.string.really_exit_app))
        setIcon(R.drawable.exit)
        setNegativeButton(getString(R.string.cancel), null)
        setPositiveButton(getString(R.string.ok)) { _, _ -> onConfirm() }
        show()
    }
}

fun Activity.showRateLimitDialog(onConfirm: () -> Unit) {
    AlertDialog.Builder(this).apply {
        setTitle(R.string.error)
        setMessage(getString(R.string.rate_limit_exceeded))
        setPositiveButton(getString(R.string.ok)) { _, _ -> onConfirm() }
        setCancelable(false)
        show()
    }
}
