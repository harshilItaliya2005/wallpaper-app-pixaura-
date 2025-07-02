package com.example.pixaura.ui.activity.loginScreen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.example.pixaura.R
import com.example.pixaura.databinding.ActivityLoginBinding
import com.example.pixaura.ui.activity.MainScreenActivity
import com.example.pixaura.ui.activity.signupScreen.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private var isPasswordVisible = false
    private var enteredEmail: String = ""
    private lateinit var sharedPref: SharedPreferences
    private val viewmodel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)

        // Skip if already logged in
        if (sharedPref.getBoolean("is_logged_in", false)) {
            startActivity(Intent(this, MainScreenActivity::class.java))
            finish()
            return
        }

        // ✅ Observe login state once
        observeLoginState()
        binding.passwordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.passwordEtText.inputType =
                    android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.passwordToggle.setImageResource(R.drawable.ic_pass_on)
            } else {
                binding.passwordEtText.inputType =
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.passwordToggle.setImageResource(R.drawable.ic_pass_off)
            }
            binding.passwordEtText.setSelection(binding.passwordEtText.text.length)
        }
        binding.button.setOnClickListener {
            val email = binding.emailEtText.text.toString().trim()
            val password = binding.passwordEtText.text.toString().trim()
            enteredEmail = email
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewmodel.login(email, password)
            } else {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signUpText.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            viewmodel.loginState.collectLatest { result ->
                // Ignore initial idle state
                if (result.exceptionOrNull()?.message == "Idle") return@collectLatest

                result.onSuccess {
                    sharedPref.edit {
                        putBoolean("is_logged_in", true)
                        putString("logged_in_email", enteredEmail)
                    }
                    Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainScreenActivity::class.java))
                    finish()
                }.onFailure {
                    Toast.makeText(this@LoginActivity, it.message ?: "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}