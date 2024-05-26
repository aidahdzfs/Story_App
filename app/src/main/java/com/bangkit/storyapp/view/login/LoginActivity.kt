package com.bangkit.storyapp.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bangkit.storyapp.R
import com.bangkit.storyapp.data.api.response.RegisterResponse
import com.bangkit.storyapp.data.preference.UserModel
import com.bangkit.storyapp.databinding.ActivityLoginBinding
import com.bangkit.storyapp.view.main.MainActivity
import com.bangkit.storyapp.view.ViewModelFactory
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)
        btnLogin()
    }

    private fun btnLogin() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val pass = binding.edtPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.edlEmail.error = getString(R.string.data_kosong)
            } else if (pass.isEmpty()) {
                binding.edlPassword.error = getString(R.string.data_kosong)
            } else {
                binding.edlEmail.error = null
                binding.edlPassword.error = null
            }

            try {
                loginViewModel.login(email, pass)
                loginViewModel.isLoading.observe(this){
                    showLoading(it)
                }

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                Log.d(TAG, "btnLogin: ${errorResponse.message}")
            }
        }
        loginViewModel.loginResult.observe(this) { result ->
            if (result.error) {
                showToast(getString(R.string.invalid_login))
            } else {
                // Login successful, save session
                showToast(getString(R.string.welcome_string))
                saveSession(
                    UserModel(
                        result.loginResult?.token.toString(),
                        result.loginResult?.name.toString(),
                        result.loginResult?.userId.toString(),
                        true
                    )
                )
            }
        }
    }

    private fun saveSession(session: UserModel) {
        lifecycleScope.launch {
            loginViewModel.saveSession(session)
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            ViewModelFactory.clearInstance()
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
       private const val TAG = "LoginActivity"
    }
}