package com.kumail.hintpod.data

import com.google.gson.annotations.SerializedName

data class Comment(
        @SerializedName("name") val name: String,
        @SerializedName("content") val content: String,
        @SerializedName("userId") val userId: String,
        @SerializedName("suggestionId") val suggestionId: String
)