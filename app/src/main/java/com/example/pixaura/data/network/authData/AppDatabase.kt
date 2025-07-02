package com.example.pixaura.data.network.authData

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pixaura.data.model.auth.User

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}