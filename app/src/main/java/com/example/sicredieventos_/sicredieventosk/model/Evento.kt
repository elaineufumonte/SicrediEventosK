package com.example.sicredieventos_.sicredieventosk.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

@Parcelize
//data class Event(var people: ArrayList<People>, val ) :Parcelable
@Seriazable
class Evento : Parcelable {
    @SerializedName("people")
    private var people: ArrayList<People> = ArrayList<People>()

    @SerializedName("date")
    private var date: Long = 0

    @SerializedName("description")
    private var description: String? = null

    @SerializedName("image")
    private var image: String? = null

    @SerializedName("longitude")
    private var longitude: Double? = null

    @SerializedName("latitude")
    private var latitude: Double? = null

    @SerializedName("price")
    private var price: Double? = null

    @SerializedName("title")
    private var title: String? = null

    @SerializedName("id")
    private var id: String? = null

    @SerializedName("body")
    private val text: String? = null

    fun Evento() {}

    fun Evento(
        people: ArrayList<People>,
        date: Long,
        description: String?,
        image: String?,
        longitude: Double?,
        latitude: Double?,
        price: Double?,
        title: String?,
        id: String?
    ) {
        this.people = people
        this.date = date
        this.description = description
        this.image = image
        this.longitude = longitude
        this.latitude = latitude
        this.price = price
        this.title = title
        this.id = id
    }


    fun getPeople(): ArrayList<People>? {
        return people
    }

    fun setPeople(people: ArrayList<People>) {
        this.people = people
    }

    fun getDate(): Long {
        return date
    }

    fun setDate(date: Long) {
        this.date = date
    }

    fun getDescription(): String? {
        return description
    }

    fun setDescription(description: String?) {
        this.description = description
    }

    fun getImage(): String? {
        return image
    }

    fun setImage(image: String?) {
        this.image = image
    }

    fun getLongitude(): Double? {
        return longitude
    }

    fun setLongitude(longitude: Double?) {
        this.longitude = longitude
    }

    fun getLatitude(): Double? {
        return latitude
    }

    fun setLatitude(latitude: Double?) {
        this.latitude = latitude
    }

    fun getPrice(): Double? {
        return price
    }

    fun setPrice(price: Double?) {
        this.price = price
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?) {
        this.title = title
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

}

annotation class Parcelize

annotation class Seriazable
