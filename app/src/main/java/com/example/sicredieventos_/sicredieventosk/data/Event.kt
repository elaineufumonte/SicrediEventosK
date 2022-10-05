package com.example.sicredieventos_.sicredieventosk.data

import android.os.Parcelable
import java.util.ArrayList


data class Event (val people: ArrayList<PeopleK>, val date: Long, val description: String, val image: String, val longitude: Double, val latitude: Double, val price: Double, val title: String, val id: String) :
    Parcelable {

}