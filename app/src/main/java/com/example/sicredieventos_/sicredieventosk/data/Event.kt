package com.example.sicredieventos_.sicredieventosk.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

@Parcelize
data class Event (val people: ArrayList<PeopleK>, val date: Long, val description: String, val image: String, val longitude: Double, val latitude: Double, val price: Double, val title: String, val id: String) :
    Parcelable