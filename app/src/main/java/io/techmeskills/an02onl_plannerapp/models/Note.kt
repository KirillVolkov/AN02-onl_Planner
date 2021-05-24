package io.techmeskills.an02onl_plannerapp.models

import android.graphics.Color
import android.os.Parcelable
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.room.*
import com.github.unhappychoice.colorhash.ColorHash
import kotlinx.parcelize.IgnoredOnParcel
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

    val fromCloud: Boolean = false,

    val alarmEnabled: Boolean = false,
) : Parcelable {

    @Ignore
    @IgnoredOnParcel
    val backgroundColor: Int = ColorHash(title).toColor()

    @Ignore
    @IgnoredOnParcel
    val textColor: Int =
        ((299 * backgroundColor.red + 587 * backgroundColor.green + 114 * backgroundColor.blue) / 1000).let { y ->
            if (y >= 128) Color.BLACK else Color.WHITE
        }
}

