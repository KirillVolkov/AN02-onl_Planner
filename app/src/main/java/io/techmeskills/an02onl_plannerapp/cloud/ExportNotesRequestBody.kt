package io.techmeskills.an02onl_plannerapp.cloud

import com.google.gson.annotations.SerializedName

class ExportNotesRequestBody(

    @SerializedName("user")
    val user: CloudUser,

    @SerializedName("phoneId")
    val phoneId: String,

    @SerializedName("notes")
    val notes: List<CloudNote>
)

class CloudUser(

    @SerializedName("id")
    val userId: Long,

    @SerializedName("name")
    val userName: String
)