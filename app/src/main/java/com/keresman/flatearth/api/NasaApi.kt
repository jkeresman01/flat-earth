package com.keresman.flatearth.api

import com.keresman.flatearth.api.model.NasaItemRecord
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//const val API_URL = "https://api.nasa.gov/planetary/"

const val API_URL = "https://api.jsonbin.io/v3/b/"

interface NasaApi {

    //@GET("apod?api_key=DEMO_KEY")
    @GET("695a9fc0d0ea881f40541f8a")
    fun fetchItems(@Query("count")count: Int = 10) : Call<NasaItemRecord>
}