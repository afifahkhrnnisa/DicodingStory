package com.fifahkhirnnsa.dicodingstory.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fifahkhirnnsa.dicodingstory.databinding.ActivityRegisterBinding
import com.fifahkhirnnsa.dicodingstory.ui.login.LoginActivity
import android.widget.Toast
import com.fifahkhirnnsa.dicodingstory.R
import com.fifahkhirnnsa.dicodingstory.data.Result

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)
        setupAction()
        observeRegisterResult()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                registerViewModel.register(name, email, password)
            } else {
                Toast.makeText(this, getString(R.string.fill_all), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeRegisterResult() {
        registerViewModel.registerResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    navigateToLogin()
                }
                is Result.Error -> {
                    showLoading(false)
                    showError(result.error)
                }
                else -> {
                    showLoading(false)
                    Toast.makeText(this, "Unexpected state", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.signupButton.isEnabled = !isLoading
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
