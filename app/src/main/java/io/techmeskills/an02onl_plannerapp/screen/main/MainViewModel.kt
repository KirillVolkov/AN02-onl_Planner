package io.techmeskills.an02onl_plannerapp.screen.main

import androidx.lifecycle.MutableLiveData
import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class MainViewModel(private val notesDao: NotesDao) : CoroutineViewModel() {

    val notesLiveData = MutableLiveData<List<Note>>(listOf(AddNewNote))

    fun deleteNote(note: Note) {
        launch {
            notesDao.deleteNote(note)
        }
        invalidateList()
    }

    fun invalidateList() {
        launch {
            val notes = notesDao.getAllNotes()
            notesLiveData.postValue(listOf(AddNewNote) + notes)
        }
    }
}

object AddNewNote : Note(-1, "", "")





