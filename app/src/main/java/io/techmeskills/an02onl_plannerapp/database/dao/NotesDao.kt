package io.techmeskills.an02onl_plannerapp.database.dao

import androidx.room.*
import io.techmeskills.an02onl_plannerapp.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
abstract class NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveNote(note: Note): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateNote(note: Note)

    @Delete
    abstract fun deleteNote(note: Note)

    @Query("SELECT * FROM notes WHERE userId == :userId ORDER BY id DESC")
    abstract fun getAllNotesFlowByUserId(userId: Long): Flow<List<Note>>
}