package io.techmeskills.an02onl_plannerapp.screen.note_details

import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.repository.NotesRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class NoteDetailsViewModel(private val notesRepository: NotesRepository) : CoroutineViewModel() {

    fun addNewNote(note: Note) {
        launch {
            notesRepository.saveNote(note)
        }
    }

    fun updateNote(note: Note) {
        launch {
            notesRepository.updateNote(note)
        }
    }
}