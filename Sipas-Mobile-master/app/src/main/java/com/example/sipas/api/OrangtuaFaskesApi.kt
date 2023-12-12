package com.example.sipas.api

import com.example.sipas.model.OrangtuaFaskes
import com.example.sipas.model.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface OrangtuaFaskesApi {

    @GET("/ortu/faskes/description")
    suspend fun getDescription(@Header("Authorization") token: String): Response<OrangtuaFaskes>

}