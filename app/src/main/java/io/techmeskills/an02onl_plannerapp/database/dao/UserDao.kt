package io.techmeskills.an02onl_plannerapp.database.dao

import androidx.room.*
import io.techmeskills.an02onl_plannerapp.models.User
import io.techmeskills.an02onl_plannerapp.models.UserContent
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun newUser(user: User)

    @Delete
    abstract fun deleteUser(user: User)

    @Query("SELECT COUNT(*) FROM users WHERE name == :userName")
    abstract fun getUsersCount(userName: String): Int

    @Query("SELECT COUNT(*) FROM users WHERE name == :userName")
    abstract fun getUsersCountFlow(userName: String): Flow<Int>

    @Query("SELECT name FROM users")
    abstract fun getAllUserNames(): Flow<List<String>>

    @Transaction
    @Query("SELECT * from users WHERE name == :userName LIMIT 1")
    abstract fun getUserContent(userName: String): UserContent?

    @Transaction
    @Query("SELECT * from users WHERE name == :userName LIMIT 1")
    abstract fun getUserContentFlow(userName: String): Flow<UserContent?>
}