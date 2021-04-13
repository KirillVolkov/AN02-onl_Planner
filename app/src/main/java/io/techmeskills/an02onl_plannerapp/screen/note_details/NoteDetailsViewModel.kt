package io.techmeskills.an02onl_plannerapp.screen.note_details

import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class NoteDetailsViewModel(private val notesDao: NotesDao) : CoroutineViewModel() {

    fun addNewNote(note: Note) {
        launch {
            notesDao.saveNote(note)
        }
    }

    fun updateNote(note: Note) {
        launch {
            notesDao.updateNote(note)
        }
    }
}