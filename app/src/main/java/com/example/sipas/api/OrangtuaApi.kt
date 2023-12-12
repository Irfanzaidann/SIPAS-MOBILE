package com.example.sipas.api

import com.example.sipas.model.Login
import com.example.sipas.model.Orangtua
import com.example.sipas.model.Response
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface OrangtuaApi {

    @POST("/orangtua")
    suspend fun insertOrangtua(@Body orangtua: Orangtua): Response<Orangtua>

    @POST("/orangtua/login")
    suspend fun loginOrangtua(@Body login: Login): Response<Orangtua>


    @GET("/orangtua/{id}")
    suspend fun getOrangtua(@Path("id") id: Int, @Header("Authorization") token: String): Response<Orangtua>

    @Multipart
    @PUT("/orangtua")
    suspend fun updateOrangtua(@Header("Authorization") token: String, @Part("dto") dto: RequestBody, @Part image: MultipartBody.Part? = null): Response<Orangtua>
}