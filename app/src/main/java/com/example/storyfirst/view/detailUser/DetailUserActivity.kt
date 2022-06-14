package com.example.storyfirst.view.detailUser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.storyfirst.R
import com.example.storyfirst.databinding.ActivityDetailUserBinding

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
    }
}