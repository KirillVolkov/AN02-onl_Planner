package io.techmeskills.an02onl_plannerapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.models.Note

@Database(
    entities = [
        Note::class
    ],
    version = 1,
    exportSchema = false
)

abstract class PlannerDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}

object DatabaseConstructor {
    fun create(context: Context): PlannerDatabase =
        Room.databaseBuilder(
            context,
            PlannerDatabase::class.java,
            "io.techmeskills.an02onl_plannerapp.db"
        ).build()
}