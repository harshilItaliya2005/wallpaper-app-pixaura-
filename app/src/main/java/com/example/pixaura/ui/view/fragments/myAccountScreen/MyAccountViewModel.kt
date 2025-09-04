package com.example.pixaura.ui.view.fragments.myAccountScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixaura.data.model.auth.User
import com.example.pixaura.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    val currentUserEmail = repository.getCurrentUserEmail()

    fun loadUser(email: String) {
        viewModelScope.launch {
            _user.value = repository.getUser(email)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.updateUser(user)
            _user.value = repository.getUser(user.email) // refresh after update
        }
    }
}