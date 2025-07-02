package com.example.pixaura.ui.view.accountScreen

import androidx.lifecycle.ViewModel
import com.example.pixaura.data.model.auth.User
import com.example.pixaura.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    suspend fun getCurrentUser(email: String): User? {
        return repository.getUser(email)
    }

}