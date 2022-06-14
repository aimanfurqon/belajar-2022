package com.example.storyfirst.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val name: String,
    val photoUrl: String,
    val description: String
) : Parcelable