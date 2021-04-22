package io.techmeskills.an02onl_plannerapp.screen.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import io.techmeskills.an02onl_plannerapp.repository.CloudRepository
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.repository.NotesRepository
import io.techmeskills.an02onl_plannerapp.repository.UsersRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(
    private val notesRepository: NotesRepository,
    private val usersRepository: UsersRepository,
    private val cloudRepository: CloudRepository
) : CoroutineViewModel() {

    val progressLiveData = MutableLiveData<Boolean>()

    val notesLiveData = notesRepository.currentUserNotesFlow.map {
        listOf(AddNewNote) + it
    }.flowOn(Dispatchers.IO).asLiveData()

    fun deleteNote(note: Note) {
        launch {
            notesRepository.deleteNote(note)
        }
    }

    fun logout() {
        launch {
            usersRepository.logout()
        }
    }

    fun exportNotes() = launch {
        val result = cloudRepository.exportNotes()
        progressLiveData.postValue(result)
    }

    fun importNotes() = launch {
        val result = cloudRepository.importNotes()
        progressLiveData.postValue(result)
    }
}

object AddNewNote : Note(-1, "", "")
