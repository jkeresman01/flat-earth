package com.keresman.flatearth.api

import android.content.Context
import android.util.Log
import com.keresman.flatearth.NasaReceiver
import com.keresman.flatearth.framework.sendBroadcast
import com.keresman.flatearth.model.Item
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
        request.enqueue(object: Callback<List<NasaItem>> {
            override fun onResponse(
                call: Call<List<NasaItem>?>,
                response: Response<List<NasaItem>?>
            ) {
                response.body()?.let {
                    populateItems(it)
                }
            }

            override fun onFailure(
                call: Call<List<NasaItem>?>,
                t: Throwable
            ) {
                Log.e("ERROR", t.toString(), t)
            }

        })

        //BG -> FG
        context.sendBroadcast<NasaReceiver>()
    }

    private fun populateItems(nasaItems: List<NasaItem>) {
        //FG
        val items = mutableListOf<Item>()
        nasaItems.forEach {
            //download image from URL locally
            var picturePath = null

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
        context.sendBroadcast<NasaReceiver>()
    }
}