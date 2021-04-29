package io.techmeskills.an02onl_plannerapp.models

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "notes", indices = [Index(value = ["title"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("userName"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class Note(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val title: String,
    val date: String,

    @ColumnInfo(index = true, name = "userName")
    val userName: String,

    val fromCloud: Boolean = false
) : Parcelable