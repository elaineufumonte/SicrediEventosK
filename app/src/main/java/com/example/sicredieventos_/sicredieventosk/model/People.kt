package com.example.sicredieventos_.sicredieventosk.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
class People : Parcelable {
    @SerializedName("eventId")
    private var eventId: String? = null

    @SerializedName("name")
    private var name: String? = null

    @SerializedName("email")
    private var email: String? = null

    fun People() {}
    fun People(eventId: String?, name: String?, email: String?) {
        this.eventId = eventId
        this.name = name
        this.email = email
    }

    fun getEventId(): String? {
        return eventId
    }

    fun setEventId(eventId: String?) {
        this.eventId = eventId
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

}