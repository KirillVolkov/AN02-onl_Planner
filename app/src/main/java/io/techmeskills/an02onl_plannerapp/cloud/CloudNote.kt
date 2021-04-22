package io.techmeskills.an02onl_plannerapp.cloud

import com.google.gson.annotations.SerializedName

class CloudNote(
    @SerializedName("id")
    val id: Long = -1,

    @SerializedName("title")
    val title: String,

    @SerializedName("date")
    val date: String
)