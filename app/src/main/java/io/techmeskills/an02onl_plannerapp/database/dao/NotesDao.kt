package io.techmeskills.an02onl_plannerapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.techmeskills.an02onl_plannerapp.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
abstract class NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveNote(note: Note): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveNotes(notes: List<Note>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateNote(note: Note)

    @Delete
    abstract fun deleteNote(note: Note)

    @Query("SELECT * FROM notes WHERE userId == :userId ORDER BY id DESC")
    abstract fun getAllNotesFlowByUserId(userId: Long): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE userId == :userId ORDER BY id DESC")
    abstract fun getAllNotesByUserId(userId: Long): List<Note>

    @Query("SELECT * FROM notes WHERE userId == :userId ORDER BY id DESC")
    abstract fun getAllNotesLiveDataByUserId(userId: Long): LiveData<List<Note>>

    @Query("UPDATE notes SET fromCloud = 1")
    abstract fun setAllNotesSyncWithCloud()
}