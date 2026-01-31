package com.keresman.flatearth.api

import android.content.Context
import android.content.Intent
import com.keresman.flatearth.NasaReceiver

class NasaFetcher(private val context: Context) {

    fun fetchItems(count: Int = 10) {
        //BG
        Thread.sleep(6000)
        //BG -> FG
        context.sendBroadcast(Intent(context, NasaReceiver::class.java))
    }
}