package com.keresman.flatearth.api

import android.content.Context
import com.keresman.flatearth.NasaReceiver
import com.keresman.flatearth.framework.sendBroadcast

class NasaFetcher(private val context: Context) {

    fun fetchItems(count: Int = 10) {
        //BG
        Thread.sleep(6000)
        //BG -> FG
        context.sendBroadcast<NasaReceiver>()
    }
}