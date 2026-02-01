package com.keresman.flatearth.api

import android.content.Context
import android.util.Log
import android.content.Intent
import com.keresman.flatearth.NasaReceiver
import com.keresman.flatearth.api.model.NasaItem
import com.keresman.flatearth.api.model.NasaItemRecord
import com.keresman.flatearth.handler.downloadImage
import com.keresman.flatearth.model.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class NasaFetcher(private val context: Context) {

     private var nasaApi: NasaApi

     init {
         val retrofit = Retrofit.Builder()
             .baseUrl(API_URL)
             .addConverterFactory(GsonConverterFactory.create())
             .build()

         nasaApi = retrofit.create<NasaApi>()
     }

    fun fetchItems(count: Int = 10) {
        //BG
        val request = nasaApi.fetchItems(count)
        request.enqueue(object: Callback<NasaItemRecord> {
            override fun onResponse(
                call: Call<NasaItemRecord>,
                response: Response<NasaItemRecord>
            ) {
                val itemsRecord = response.body()
                if (itemsRecord != null) {
                    val nasaItems = itemsRecord.record
                    context.sendBroadcast(Intent(NasaReceiver.ACTION))
                    populateItems(nasaItems)
                } else {
                    val errorIntent = Intent(NasaReceiver.ACTION_ERROR)
                    errorIntent.putExtra(NasaReceiver.EXTRA_ERROR_CODE, response.code())
                    context.sendBroadcast(errorIntent)
                }
            }

            override fun onFailure(
                call: Call<NasaItemRecord>,
                t: Throwable
            ) {
                Log.e("ERROR", t.toString(), t)
            }

        })
    }

    private fun populateItems(nasaItems: List<NasaItem>) {
        //FG
        val items = mutableListOf<Item>()

        //BG
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            nasaItems.forEach {
                var picturePath = downloadImage(context, it.url)

                items.add(Item(
                    null,
                    it.title,
                    it.explanation,
                    picturePath ?:"",
                    it.date,
                    false
                ))
            }

            //BG -> FG
            context.sendBroadcast(Intent(NasaReceiver.ACTION))
        }
    }



}