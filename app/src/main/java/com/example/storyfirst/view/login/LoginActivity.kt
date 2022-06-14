package com.example.storyfirst.view.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyfirst.databinding.ActivityLoginBinding
import com.example.storyfirst.helper.Constant
import com.example.storyfirst.helper.PreferencesHelper
import com.example.storyfirst.model.LoginResponse
import com.example.storyfirst.model.LoginResult
import com.example.storyfirst.server.ApiConfig
import com.example.storyfirst.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var tokenId: LoginResult
    lateinit var sharedPref: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }
                else -> {
                    ApiConfig.instanceRetrofit.login(email, password)
                        .enqueue(object : Callback<LoginResponse> {
                            override fun onResponse(
                                call: Call<LoginResponse>,
                                response: Response<LoginResponse>
                            ) {
                                sharedPref.put(Constant.PREF_EMAIL, email)
                                sharedPref.put(Constant.PREF_PASSWORD, password)
                                sharedPref.put(Constant.PREF_IS_LOGIN, true)
                             //   sharedPref.put(Constant.PREF_TOKEN, token)
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                                Toast.makeText(this@LoginActivity, "Success", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Error: " + t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }
            }
        }
    }
}