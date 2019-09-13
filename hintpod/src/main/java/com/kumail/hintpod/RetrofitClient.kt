package com.kumail.hintpod

import com.google.gson.GsonBuilder
import com.kumail.hintpod.interfaces.ApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.xml.datatype.DatatypeConstants.SECONDS
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


class RetrofitClient {

    var gson = GsonBuilder()
            .setLenient()
            .create()

    fun getClient(): ApiService {
        val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build()
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .baseUrl("https://us-central1-hintpod.cloudfunctions.net/").build()
        return retrofit.create(ApiService::class.java)
    }

}