package com.example.storyfirst.view.signup

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
//import androidx.datastore.preferences.preferencesDataStore
import com.example.storyfirst.databinding.ActivitySignupBinding
import com.example.storyfirst.helper.Constant
import com.example.storyfirst.helper.PreferencesHelper
import com.example.storyfirst.model.RegisterResponse
import com.example.storyfirst.server.ApiConfig
import com.example.storyfirst.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    lateinit var sharedPref: PreferencesHelper

    companion object {
        const val RESULT_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)
        setupView()
       // setupViewModel()
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
//        signupViewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(UserPreference.getInstance(dataStore))
//        )[SignupViewModel::class.java]
//    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = "Masukkan nama"
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }
                else -> {

                    ApiConfig.instanceRetrofit.register(name, email, password)
                        .enqueue(object : Callback<RegisterResponse> {
                            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                                Toast.makeText(
                                    this@SignupActivity,
                                    "Error: " + t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onResponse(
                                call: Call<RegisterResponse>,
                                response: Response<RegisterResponse>
                            ) {
                                sharedPref.put(Constant.PREF_USERNAME, name)
                                sharedPref.put(Constant.PREF_EMAIL, email)
                                sharedPref.put(Constant.PREF_PASSWORD, password)
                                sharedPref.put(Constant.PREF_IS_LOGIN, true)

                                setResult(RESULT_CODE)
                                val intent = Intent(this@SignupActivity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                                Toast.makeText(this@SignupActivity, "Success", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })

//                    signupViewModel.saveUser(UserModel(name, email, password, false))
//                    AlertDialog.Builder(this).apply {
//                        setTitle("Yeah!")
//                        setMessage("Akunnya sudah jadi nih. Yuk, login dan belajar coding.")
//                        setPositiveButton("Lanjut") { _, _ ->
//                            finish()
//                        }
//                        create()
//                        show()
//                    }
                }
            }
        }
    }

//    private fun saveUser(name: String, email: String, password: String) {
//        val userPreference = UserPreference(this)
//        userModel.name = name
//        userModel.email = email
//        userModel.password = password
//        userPreference.setUser(userModel)
//        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show()
//    }
}