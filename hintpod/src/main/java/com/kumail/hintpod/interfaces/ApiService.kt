package com.kumail.hintpod.interfaces

import com.kumail.hintpod.data.Comment
import com.kumail.hintpod.data.Suggestion
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("loadSuggestions")
    fun getSuggestions(): Observable<List<Suggestion>>

    @GET("loadComments")
    fun getComments(@Query("suggestionId") suggestionId: String): Observable<List<Comment>>

//    @GET("league-tables?competition_id=49")
//    abstract fun getLeagueTables(@Query("api_key") api_key: String): Call<List<TableApiResponse>>
//
//
//    @GET("league-tables?competition_id=49")
//    Call<List<TableApiResponse>> getLeagueTables(@Query("api_key") String api_key);

//    companion object {
//        fun create(): ApiService {
//
//            val retrofit = Retrofit.Builder()
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .baseUrl("https://us-central1-hintpod.cloudfunctions.net/")
//                    .build()
//
//            return retrofit.create(ApiService::class.java)
//        }
//    }

//    companion object {
//        fun create(): WikiApiService {
//
//            val retrofit = Retrofit.Builder()
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .baseUrl("https://en.wikipedia.org/w/")
//                    .build()
//
//            return retrofit.create(WikiApiService::class.java)
//        }
//    }

//    private var retrofit: Retrofit? = null
//
//    fun getClient(baseUrl: String): Retrofit {
//        if (retrofit == null) {
//            retrofit = Retrofit.Builder()
//                    .baseUrl(baseUrl)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//        }
//
//        return retrofit
//    }


}
