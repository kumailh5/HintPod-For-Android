package com.kumail.hintpod.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Suggestion(
        @SerializedName("key") val key: String,
        @SerializedName("userId") val userId: String,
        @SerializedName("title") val title: String,
        @SerializedName("content") val content: String,
        @SerializedName("projectId") val projectId: String,
        @SerializedName("status") val status: String,
        @SerializedName("voteCount") val voteCount: Int,
        @SerializedName("votes") val votes: Map<String, String>,
        var upEnabled: Boolean = false,
        var downEnabled: Boolean = false,
        var local: Boolean = false
) : Serializable