package com.bangkit.storyapp.view.register

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
import com.bangkit.storyapp.databinding.ActivityRegisterBinding
import com.bangkit.storyapp.view.ViewModelFactory
import com.bangkit.storyapp.view.login.LoginActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel by viewModels<RegisterViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)
        btnRegister()
    }

    private fun btnRegister() {
        binding.btnSignup.setOnClickListener {
            val name = binding.edtName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val pass = binding.edtPassword.text.toString().trim()

            if (name.isEmpty()){
                binding.edlName.error = getString(R.string.data_kosong)
            } else if (email.isEmpty()) {
                binding.edlEmail.error = getString(R.string.data_kosong)
            } else if (pass.isEmpty()) {
                binding.edlPassword.error = getString(R.string.data_kosong)
            } else {
                binding.edlName.error = null
                binding.edlEmail.error = null
                binding.edlPassword.error = null
            }

            lifecycleScope.launch {
                try {
                    registerViewModel.register(name, email, pass)
                    Toast.makeText(this@RegisterActivity,
                        getString(R.string.create_account), Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()

                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                    Log.d(TAG, "btnRegister: ${errorResponse.message}")
                }
            }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val TAG = "RegisterActivity"
    }
}