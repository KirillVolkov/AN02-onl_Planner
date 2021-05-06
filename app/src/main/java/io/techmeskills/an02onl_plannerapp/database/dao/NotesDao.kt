package io.techmeskills.an02onl_plannerapp.database.dao

import androidx.room.*
import io.techmeskills.an02onl_plannerapp.models.Note

@Dao
abstract class NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveNote(note: Note): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveNotes(notes: List<Note>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateNote(note: Note)

    @Query("SELECT * FROM notes WHERE id == :noteId LIMIT 1")
    abstract fun getNoteById(noteId: Long): Note?

    @Delete
    abstract fun deleteNote(note: Note)

    @Query("UPDATE notes SET fromCloud = 1")
    abstract fun setAllNotesSyncWithCloud()
}