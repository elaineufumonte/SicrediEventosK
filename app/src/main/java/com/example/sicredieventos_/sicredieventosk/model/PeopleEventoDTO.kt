package com.example.sicredieventos_.sicredieventosk.model

import com.google.gson.annotations.SerializedName

class PeopleEventoDTO (){
    @SerializedName("nome")
    private var nome: String? = null

    @SerializedName("email")
    private var email: String? = null

    @SerializedName("eventId")
    private var eventId: String? = null

    @SerializedName("body")
    private val text: String? = null
    fun PeopleEventoDTO() {}

    fun PeopleEventoDTO(nome: String?, email: String?, eventId: String?) {
        this.nome = nome
        this.email = email
        this.eventId = eventId
    }

    fun getNome(): String? {
        return nome
    }

    fun setNome(nome: String?) {
        this.nome = nome
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getEventId(): String? {
        return eventId
    }

    fun setEventId(eventId: String?) {
        this.eventId = eventId
    }
}