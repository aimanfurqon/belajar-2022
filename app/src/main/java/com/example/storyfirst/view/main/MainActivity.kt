package com.example.storyfirst.view.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyfirst.R
import com.example.storyfirst.adapter.StoriesAdapter
import com.example.storyfirst.databinding.ActivityMainBinding
import com.example.storyfirst.model.GetStoriesResponse
import com.example.storyfirst.model.UserPreference
import com.example.storyfirst.server.ApiConfig
import com.example.storyfirst.view.ViewModelFactory
import com.example.storyfirst.view.welcome.WelcomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
//        setupViewModel()
        setupAction()

        adapter = StoriesAdapter(arrayListOf(), this@MainActivity)
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.setHasFixedSize(true)
        binding.rvStories.adapter = adapter
        getStudents()
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
//        mainViewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(UserPreference.getInstance(dataStore))
//        )[MainViewModel::class.java]
//
//        mainViewModel.getUser().observe(this, { user ->
//            if (user.isLogin){
//                binding.nameTextView.text = getString(R.string.greeting, user.name)
//            } else {
//                startActivity(Intent(this, WelcomeActivity::class.java))
//                finish()
//            }
//        })
//    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            mainViewModel.logout()
            startActivity(Intent(this,WelcomeActivity::class.java))
        }
    }

    fun getStudents() {
        ApiConfig.instanceRetrofit.getStories().enqueue(object : Callback<GetStoriesResponse> {
            override fun onResponse(
                call: Call<GetStoriesResponse>,
                response: Response<GetStoriesResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        setData(data.listStory)
                    }
                }
            }

            override fun onFailure(call: Call<GetStoriesResponse>, t: Throwable) {
                Log.d("Error Get", "" + t.stackTraceToString())
            }
        })
    }

    fun setData(data: ArrayList<GetStoriesResponse.ListStoryItem>) {
        adapter.setData(data)
    }
}