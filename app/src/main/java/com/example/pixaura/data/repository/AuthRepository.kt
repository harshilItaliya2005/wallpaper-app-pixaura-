package com.example.pixaura.data.repository

import com.example.pixaura.data.model.auth.User
import com.example.pixaura.data.network.authData.UserDao
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val userDao: UserDao,

) {

    private var currentUserEmail: String = ""
    suspend fun register(user: User): Boolean {
        return try {
            val existing = userDao.getUserByEmail(user.email)
            if (existing == null) {
                userDao.insertUser(user)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    // Login: fetch user by email and verify password manually
    suspend fun login(email: String, password: String): User? {
        val user = userDao.getUserByEmail(email)
        return if (user != null && user.password == password) {
            user
        } else {
            null
        }
    }

    suspend fun getUser(email: String): User? {
        return userDao.getUserByEmail(email)
    }
    suspend fun updateUser(user: User): Boolean {
        return try {
            userDao.updateUser(user)  // uses @Update
            true
        } catch (e: Exception) {
            false
        }
    }
    fun setCurrentUser(email: String) {
        currentUserEmail = email
    }
    fun getCurrentUserEmail(): String = currentUserEmail
}
