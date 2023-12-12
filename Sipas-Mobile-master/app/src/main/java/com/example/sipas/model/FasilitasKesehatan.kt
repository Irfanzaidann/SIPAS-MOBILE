package com.example.sipas.model

data class FasilitasKesehatan(
    val email: String,
    val password: String,
    val username: String,
    val kodeUnik: String,
    val nomorTelepon: String?,
    val alamatFaskes: String?,
    val id: Int? = null
)
