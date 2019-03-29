package com.kumail.hintpod.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.kumail.hintpod.interfaces.ApiService
import com.kumail.hintpod.R
import com.kumail.hintpod.RetrofitClient
import com.kumail.hintpod.adapters.CommentsAdapter
import com.kumail.hintpod.adapters.SuggestionsAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.activity_suggestion_detailed.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SuggestionDetailedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestion_detailed)


        val extras = intent.extras
        val suggestionId: String?
        if (extras == null) {
            return
        } else {
            suggestionId = extras.getString("suggestionId")
            println("DETAILS $suggestionId")
        }

        // Creates a vertical Layout Manager

        rv_comments.layoutManager = LinearLayoutManager(this)

        val suggestionsApi = RetrofitClient().getClient()

        val response = suggestionsApi.getComments(suggestionId)

        response.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            println("SIZE")
            println(it.size)
            rv_comments.adapter = CommentsAdapter(it, this)
        }

        println("here toast suggestion detailed act")
    }

}