package com.example.sicredieventos_.sicredieventosk.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PeopleK (var eventId: String, var name: String, var email: String) : Parcelable {

}
