package com.keresman.flatearth.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.keresman.flatearth.api.NasaFetcher

class NasaWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams){

    override fun doWork(): Result {
        //BG thread
        NasaFetcher(context).fetchItems()
        return Result.success()
    }
}