package com.example.pixaura.ui.activity.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixaura.data.model.auth.User
import com.example.pixaura.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Result<User>>(Result.failure(Exception("Idle")))
    val loginState: StateFlow<Result<User>> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {

            val user = repository.login(email, password)
            repository.setCurrentUser(email)
            _loginState.value = when {
                user == null -> Result.failure(Exception("Email or password is incorrect"))
                else -> Result.success(user)
            }
        }
    }
}
