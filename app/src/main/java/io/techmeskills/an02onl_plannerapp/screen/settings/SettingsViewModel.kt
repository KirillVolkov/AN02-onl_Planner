package io.techmeskills.an02onl_plannerapp.screen.settings

import androidx.lifecycle.MutableLiveData
import io.techmeskills.an02onl_plannerapp.repository.CloudRepository
import io.techmeskills.an02onl_plannerapp.repository.UsersRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val cloudRepository: CloudRepository,
    private val usersRepository: UsersRepository
) : CoroutineViewModel() {


    val progressLiveData = MutableLiveData<Boolean>()

    fun exportNotes() = launch {
        val result = cloudRepository.exportNotes()
        progressLiveData.postValue(result)
    }

    fun importNotes() = launch {
        val result = cloudRepository.importNotes()
        progressLiveData.postValue(result)
    }

    fun logout() {
        launch {
            usersRepository.logout()
        }
    }

    fun deleteUser() {
        launch {
            usersRepository.deleteCurrent()
        }
    }

}