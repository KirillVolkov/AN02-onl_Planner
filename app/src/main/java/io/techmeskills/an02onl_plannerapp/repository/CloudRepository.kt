package io.techmeskills.an02onl_plannerapp.repository

import io.techmeskills.an02onl_plannerapp.cloud.ApiInterface
import io.techmeskills.an02onl_plannerapp.cloud.CloudNote
import io.techmeskills.an02onl_plannerapp.cloud.CloudUser
import io.techmeskills.an02onl_plannerapp.cloud.ExportNotesRequestBody
import io.techmeskills.an02onl_plannerapp.models.Note
import kotlinx.coroutines.flow.first

class CloudRepository(
    private val apiInterface: ApiInterface,
    private val userRepository: UsersRepository,
    private val notesRepository: NotesRepository
) {

    suspend fun exportNotes(): Boolean {
        val user = userRepository.getCurrentUserFlow().first()
        val notes = notesRepository.getCurrentUserNotes()
        val cloudUser = CloudUser(userId = user.id, userName = user.name)
        val cloudNotes = notes.map { CloudNote(id = it.id, title = it.title, date = it.date) }
        val exportRequestBody =
            ExportNotesRequestBody(cloudUser, userRepository.phoneId, cloudNotes)
        val exportResult = apiInterface.exportNotes(exportRequestBody).isSuccessful
        if (exportResult) {
            notesRepository.setAllNotesSyncWithCloud()
        }
        return exportResult
    }

    suspend fun importNotes(): Boolean {
        val user = userRepository.getCurrentUserFlow().first()
        val response = apiInterface.importNotes(user.name, userRepository.phoneId)
        val cloudNotes = response.body() ?: emptyList()
        val notes = cloudNotes.map { cloudNote ->
            Note(
                title = cloudNote.title,
                date = cloudNote.date,
                userId = user.id,
                fromCloud = true
            )
        }
        notesRepository.saveNotes(notes)
        return response.isSuccessful
    }
}