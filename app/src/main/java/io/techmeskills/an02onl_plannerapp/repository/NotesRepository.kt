package io.techmeskills.an02onl_plannerapp.repository

import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.datastore.AppSettings
import io.techmeskills.an02onl_plannerapp.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext

class NotesRepository(private val notesDao: NotesDao, private val appSettings: AppSettings) {

    val currentUserNotesFlow: Flow<List<Note>> =
        appSettings.userIdFlow()
            .flatMapLatest { userId -> //получаем из сеттингов текущий айди юзера
                notesDao.getAllNotesFlowByUserId(userId) //получаем заметки по айди юзера
            }

    suspend fun saveNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.saveNote(
                Note(
                    title = note.title,
                    date = note.date,
                    userId = appSettings.userId()
                )
            )
        }
    }

    suspend fun updateNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.updateNote(note)
        }
    }

    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.deleteNote(note)
        }
    }
}