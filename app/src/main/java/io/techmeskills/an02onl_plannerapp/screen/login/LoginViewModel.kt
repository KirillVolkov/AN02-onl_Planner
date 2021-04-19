package io.techmeskills.an02onl_plannerapp.screen.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import io.techmeskills.an02onl_plannerapp.repository.UsersRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class LoginViewModel(private val usersRepository: UsersRepository) : CoroutineViewModel() {

    val autoCompleteUserNamesLiveData = usersRepository.allUserNames.asLiveData()

    val loggedIn: LiveData<Boolean> = usersRepository.checkUserLoggedIn().asLiveData()

    val errorLiveData = MutableLiveData<String>()

    fun login(user: String) {
        launch {
            try {
                if (user.isNotBlank()) {
                    usersRepository.login(user)
                } else {
                    errorLiveData.postValue("Enter user name")
                }
            } catch (e: Exception) {
                errorLiveData.postValue(e.message)
            }
        }
    }
}