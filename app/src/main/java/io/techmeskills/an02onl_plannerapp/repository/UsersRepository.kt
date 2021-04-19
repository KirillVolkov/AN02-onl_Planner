package io.techmeskills.an02onl_plannerapp.repository

import io.techmeskills.an02onl_plannerapp.database.dao.UserDao
import io.techmeskills.an02onl_plannerapp.datastore.AppSettings
import io.techmeskills.an02onl_plannerapp.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UsersRepository(private val usersDao: UserDao, private val appSettings: AppSettings) {

    val allUserNames = usersDao.getAllUserNames()

    suspend fun login(userName: String) {
        withContext(Dispatchers.IO) { //указываем, что метод должен выполниться в IO
            if (checkUserExists(userName).not()) { //проверяем существует ли в базе юзер с таким именем
                val userId =
                    usersDao.newUser(User(name = userName)) //добавляем в базу нового юзера, берем его сгенерированный базой id
                appSettings.setUserId(userId) //запоминаем залогиненного юзера в сеттингах
            } else {
                val userId = usersDao.getUserId(userName)
                appSettings.setUserId(userId)
            }
        }
    }

    private suspend fun checkUserExists(userName: String): Boolean {
        return withContext(Dispatchers.IO) {
            usersDao.getUsersCount(userName) > 0
        }
    }

    fun checkUserLoggedIn(): Flow<Boolean> =
        appSettings.userIdFlow().map { it >= 0 }.flowOn(Dispatchers.IO)

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            appSettings.setUserId(-1)
        }
    }
}