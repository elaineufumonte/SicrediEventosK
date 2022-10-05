package com.example.sicredieventos_.sicredieventosk.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
class People() : Parcelable {
    @SerializedName("eventId")
    private var eventId: String? = null

    @SerializedName("name")
    private var name: String? = null

    @SerializedName("email")
    private var email: String? = null

    constructor(parcel: Parcel) : this() {
        eventId = parcel.readString()
        name = parcel.readString()
        email = parcel.readString()
    }

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(eventId)
        parcel.writeString(name)
        parcel.writeString(email)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<People> {
        override fun createFromParcel(parcel: Parcel): People {
            return People(parcel)
        }

        override fun newArray(size: Int): Array<People?> {
            return arrayOfNulls(size)
        }
    }

}