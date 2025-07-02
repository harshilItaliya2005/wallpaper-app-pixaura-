package com.example.pixaura.ui.activity.signupScreen

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
class SignupViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _signupState = MutableStateFlow<Result<Boolean>?>(null)
    val signupState: StateFlow<Result<Boolean>?> = _signupState

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = repository.register(User(username = username, email = email, password = password))
                _signupState.value = Result.success(result)
            } catch (e: Exception) {
                _signupState.value = Result.failure(e)
            }
        }
    }

}
