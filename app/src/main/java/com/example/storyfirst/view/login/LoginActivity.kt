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
import androidx.lifecycle.ViewModelProvider
import com.example.storyfirst.databinding.ActivityLoginBinding
import com.example.storyfirst.helper.Constant
import com.example.storyfirst.helper.PreferencesHelper
import com.example.storyfirst.model.LoginResponse
import com.example.storyfirst.model.LoginResult
import com.example.storyfirst.model.UserModel
import com.example.storyfirst.model.UserPreference
import com.example.storyfirst.server.ApiConfig
import com.example.storyfirst.view.ViewModelFactory
import com.example.storyfirst.view.main.MainActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: UserModel
    private lateinit var tokenId: LoginResult

    lateinit var sharedPref: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)

        //     setupView()
        //   setupViewModel()
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

//    private fun setupViewModel() {
//        loginViewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(UserPreference.getInstance(dataStore))
//        )[LoginViewModel::class.java]
//
//        loginViewModel.getUser().observe(this, { user ->
//            this.user = user
//        })
//    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val token = tokenId.token
            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }
//                email != user.email -> {
//                    binding.emailEditTextLayout.error = "Email tidak sesuai"
//                }
//                password != user.password -> {
//                    binding.passwordEditTextLayout.error = "Password tidak sesuai"
//                }
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
                                sharedPref.put(Constant.PREF_TOKEN, token)
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
//                    loginViewModel.login()
//                    AlertDialog.Builder(this).apply {
//                        setTitle("Yeah!")
//                        setMessage("Anda berhasil login. Sudah tidak sabar untuk belajar ya?")
//                        setPositiveButton("Lanjut") { _, _ ->
//                            val intent = Intent(context, MainActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                            startActivity(intent)
//                            finish()
//                        }
//                        create()
//                        show()
//                    }
                }
            }
        }
    }

    companion object {
        var token = ""
    }
}