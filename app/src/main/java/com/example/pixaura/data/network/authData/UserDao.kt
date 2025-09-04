package com.example.pixaura.data.network.authData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pixaura.data.model.auth.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE username = :username AND email = :email")
    suspend fun getUserByUsernameAndEmail(username: String, email: String): User?

    @Update
    suspend fun updateUser(user: User)
}
