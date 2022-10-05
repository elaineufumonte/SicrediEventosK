package com.example.sicredieventos_.sicredieventosk.data

import android.os.Parcel
import android.os.Parcelable
import com.example.sicredieventos_.sicredieventosk.model.Parcelize

@Parcelize
data class PeopleK (var eventId: String, var name: String, var email: String) : Parcelable {

}
