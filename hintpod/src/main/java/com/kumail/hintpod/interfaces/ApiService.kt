package com.kumail.hintpod.interfaces

import com.kumail.hintpod.data.Comment
import com.kumail.hintpod.data.Suggestion
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("verifyUser")
    fun verifyUser(@Query("uniqueId") uniqueId: String,
                   @Query("projectId") projectId: String,
                   @Query("name") name: String?): Observable<String>

    @GET("loadSuggestions")
    fun getSuggestions(): Observable<List<Suggestion>>

    @GET("loadComments")
    fun getComments(@Query("suggestionId") suggestionId: String): Observable<List<Comment>>

    @GET("addSuggestion")
    fun addSuggestion(@Query("title") title: String,
                      @Query("content") content: String,
                      @Query("userId") userId: String,
                      @Query("projectId") projectId: String): Observable<String>

    @GET("addComment")
    fun addComment(@Query("content") content: String,
                   @Query("userId") userId: String,
                   @Query("suggestionId") suggestionId: String): Observable<String>

    @GET("voteSuggestion")
    fun voteSuggestion(@Query("userId") userId: String,
                       @Query("suggestionId") suggestionId: String,
                       @Query("upvote") upvote: String,
                       @Query("voting") voting: String): Observable<String>


}
