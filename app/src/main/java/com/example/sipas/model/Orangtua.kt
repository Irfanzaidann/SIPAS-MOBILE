package com.example.sipas.model

data class Orangtua(
    val namaAyah: String,
    val namaIbu: String,
    val email: String,
    val password: String,
    val isConnectedWithFaskes: Boolean,
    val imageUrl: String? = null,
    var id: Int? = null
)
