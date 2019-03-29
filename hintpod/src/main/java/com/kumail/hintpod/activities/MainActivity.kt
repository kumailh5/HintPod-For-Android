package com.kumail.hintpod.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.kumail.hintpod.interfaces.ApiService
import com.kumail.hintpod.R
import com.kumail.hintpod.RetrofitClient
import com.kumail.hintpod.adapters.SuggestionsAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Creates a vertical Layout Manager
        rv_suggestions.layoutManager = LinearLayoutManager(this)

        val suggestionsApi = RetrofitClient().getClient()
        val response = suggestionsApi.getSuggestions()
        response.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            println("SIZE")
            println(it.size)
            rv_suggestions.adapter = SuggestionsAdapter(it, this)
        }

        println("here toast main act")
    }

}
