package io.techmeskills.an02onl_plannerapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users", indices = [Index(value = ["name"], unique = true)])
data class User(

    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String
)