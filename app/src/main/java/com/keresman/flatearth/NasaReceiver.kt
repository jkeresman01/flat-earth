package com.keresman.flatearth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.keresman.flatearth.framework.setProperty
import com.keresman.flatearth.framework.startActivity

class NasaReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION = "com.keresman.flatearth.ACTION_DATA_FETCHED"
        const val ACTION_ERROR = "com.keresman.flatearth.ACTION_FETCH_ERROR"
        const val EXTRA_ERROR_CODE = "error_code"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        // FG
        //context.setProperty<Boolean>(DATA_IMPORTED, true)
        context.startActivity<HostActivity>()
    }
}