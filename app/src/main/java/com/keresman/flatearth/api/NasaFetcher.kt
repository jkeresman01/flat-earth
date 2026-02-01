package com.keresman.flatearth.api

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import com.keresman.flatearth.ACTION_DATA_FETCHED
import com.keresman.flatearth.NASA_PROVIDER_CONTENT_URI
import com.keresman.flatearth.ACTION_DATA_FETCH_ERROR
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

private const val EXTRA_ERROR_CODE = "error_code"
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
        val request = nasaApi.fetchItems(count)
        request.enqueue(object: Callback<NasaItemRecord> {
            override fun onResponse(
                call: Call<NasaItemRecord>,
                response: Response<NasaItemRecord>
            ) {
                handleResponse(response)
            }

            override fun onFailure(
                call: Call<NasaItemRecord>,
                t: Throwable
            ) {
                handleFailure(t)
            }
        })
    }

    private fun handleResponse(response: Response<NasaItemRecord>) {
        val itemsRecord = response.body()
        if (itemsRecord != null) {
            context.sendBroadcast(Intent(ACTION_DATA_FETCHED))
            populateItems(itemsRecord.record)
        } else {
            broadcastError(response.code())
        }
    }

    private fun broadcastError(code: Int) {
        val errorIntent = Intent(ACTION_DATA_FETCH_ERROR)
        errorIntent.putExtra(EXTRA_ERROR_CODE, code)
        context.sendBroadcast(errorIntent)
    }

    private fun handleFailure(t: Throwable) {
        Log.e("ERROR", t.toString(), t)
    }

    private fun populateItems(nasaItems: List<NasaItem>) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            nasaItems.forEach {
                val picturePath = downloadImage(context, it.url)

                val values = ContentValues().apply {
                    put(Item::title.name, it.title)
                    put(Item::explanation.name, it.explanation)
                    put(Item::picturePath.name, picturePath ?: "")
                    put(Item::date.name, it.date)
                    put(Item::read.name, false)
                }

                context.contentResolver.insert(
                    NASA_PROVIDER_CONTENT_URI,
                    values
                )
            }

            val cursor = context.contentResolver.query(NASA_PROVIDER_CONTENT_URI, null, null, null, null)
            cursor?.use {
                Log.d("DB_CHECK", "Items in DB: ${it.count}")
                while (it.moveToNext()) {
                    val title = it.getString(it.getColumnIndexOrThrow(Item::title.name))
                    val date = it.getString(it.getColumnIndexOrThrow(Item::date.name))
                    val picturePath = it.getString(it.getColumnIndexOrThrow(Item::picturePath.name))
                    Log.d("DB_CHECK", "Item: title=$title, date=$date, picturePath=$picturePath")
                }
            }

            context.sendBroadcast(Intent(ACTION_DATA_FETCHED))
        }
    }



}