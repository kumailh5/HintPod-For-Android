package com.kumail.hintpod

import com.google.gson.GsonBuilder
import com.kumail.hintpod.interfaces.ApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    fun getClient(): ApiService {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://us-central1-hintpod.cloudfunctions.net/").build()
        return retrofit.create(ApiService::class.java)
    }

}