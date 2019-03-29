package com.kumail.hintpod.data

import com.google.gson.annotations.SerializedName

data class Comment(
        @SerializedName("comment") val comment: String,
        @SerializedName("userId") val userId: String,
        @SerializedName("suggestionId") val suggestionId: String
)