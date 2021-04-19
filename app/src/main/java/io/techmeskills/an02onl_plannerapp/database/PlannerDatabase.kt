package io.techmeskills.an02onl_plannerapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.techmeskills.an02onl_plannerapp.BuildConfig
import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.database.dao.UserDao
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.models.User

@Database(
    entities = [
        Note::class,
        User::class
    ],
    version = 1,
    exportSchema = false
)

abstract class PlannerDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    abstract fun usersDao(): UserDao
}

object DatabaseConstructor {
    fun create(context: Context): PlannerDatabase =
        Room.databaseBuilder(
            context,
            PlannerDatabase::class.java,
            "${BuildConfig.APPLICATION_ID}.db"
        ).build()
}