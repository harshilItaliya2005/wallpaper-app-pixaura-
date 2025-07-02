package com.example.pixaura.ui.activity.signupScreen

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pixaura.databinding.ActivitySignUpBinding
import com.example.pixaura.ui.activity.loginScreen.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.io.path.Path

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private val viewmodel: SignupViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.apply {
            button.setOnClickListener {
                val username = userNameEtText.text.toString().trim()
                val email = emailEtText.text.toString().trim()
                val password = passwordEtText.text.toString().trim()

                when {
                    username.isEmpty() || email.isEmpty() || password.isEmpty() -> {
                        Toast.makeText(this@SignUpActivity, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                    !isValidEmail(email) -> {
                        Toast.makeText(this@SignUpActivity, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                    }
                    !isStrongPassword(password) -> {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Password must contain 6+ chars, upper/lowercase, digit, special char",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {
                        viewmodel.register(username = username, email = email, password = password)
                    }
                }

            }

            logInText.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                finish()
            }

            observeSignupState()
        }
    }
    private fun observeSignupState() {
        lifecycleScope.launch {
            viewmodel.signupState.collectLatest { result ->
                // Ignore the initial idle state
                if (result == null) return@collectLatest

                result.onSuccess { success ->
                    if (success) {
                        Toast.makeText(this@SignUpActivity, "Signup Successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@SignUpActivity, "Email already exists", Toast.LENGTH_SHORT).show()
                    }
                }.onFailure {
                    Toast.makeText(this@SignUpActivity, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isStrongPassword(password: String): Boolean {
        val passwordPattern = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!]).{6,}\$")
        return passwordPattern.matches(password)
    }

}