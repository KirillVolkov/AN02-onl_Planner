package io.techmeskills.an02onl_plannerapp.cloud

import com.google.gson.annotations.SerializedName

class CloudNote(

    @SerializedName("title")
    val title: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("alarmEnabled")
    val alarmEnabled: Boolean
)