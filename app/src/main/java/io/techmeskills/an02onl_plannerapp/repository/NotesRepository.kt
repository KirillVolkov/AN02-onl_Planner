package io.techmeskills.an02onl_plannerapp.repository

import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.database.dao.UserDao
import io.techmeskills.an02onl_plannerapp.datastore.AppSettings
import io.techmeskills.an02onl_plannerapp.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NotesRepository(
    private val userDao: UserDao,
    private val notesDao: NotesDao,
    private val appSettings: AppSettings,
    private val notificationRepository: NotificationRepository
) {

    val currentUserNotesFlow: Flow<List<Note>> =
        appSettings.userNameFlow()
            .flatMapLatest { userName -> //получаем из сеттингов текущее имя юзера
                userDao.getUserContentFlow(userName)
                    .map {
                        it?.notes?.sortedBy { it.date } ?: emptyList()
                    } //получаем заметки по имени юзера
            }

    suspend fun getCurrentUserNotes(): List<Note> {
        return userDao.getUserContent(appSettings.userName())?.notes ?: emptyList()
    }

    suspend fun setAllNotesSyncWithCloud() {
        withContext(Dispatchers.IO) {
            notesDao.setAllNotesSyncWithCloud()
        }
    }

    suspend fun saveNote(note: Note) {
        withContext(Dispatchers.IO) {
            val id = notesDao.saveNote(
                note.copy(userName = appSettings.userName())
            )
            if (note.alarmEnabled) {
                notificationRepository.setNotification(note.copy(id = id))
            }
        }
    }

    suspend fun saveNotes(notes: List<Note>) {
        withContext(Dispatchers.IO) {
            notesDao.saveNotes(notes)
            notes.forEach {
                if (it.alarmEnabled) {
                    notificationRepository.setNotification(it)
                }
            }
        }
    }

    suspend fun updateNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.getNoteById(note.id)?.let { oldNote ->
                notificationRepository.unsetNotification(oldNote)
            }
            notesDao.updateNote(note)
            if (note.alarmEnabled) {
                notificationRepository.setNotification(note)
            }
        }
    }

    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            notificationRepository.unsetNotification(note)
            notesDao.deleteNote(note)
        }
    }

    suspend fun deleteNoteById(noteId: Long) {
        withContext(Dispatchers.IO) {
            notesDao.getNoteById(noteId)?.let {
                notificationRepository.unsetNotification(it)
                notesDao.deleteNote(it)
            }
        }
    }

    suspend fun postponeNoteById(noteId: Long) {
        withContext(Dispatchers.IO) {
            notesDao.getNoteById(noteId)?.let { note ->
                notificationRepository.unsetNotification(note)
                val postponedNote = notificationRepository.postponeNoteTimeByFiveMins(note)
                notesDao.updateNote(postponedNote)
                notificationRepository.setNotification(postponedNote)
            }
        }
    }
}