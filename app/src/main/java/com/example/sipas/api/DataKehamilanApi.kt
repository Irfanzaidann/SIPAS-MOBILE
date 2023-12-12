package com.example.sipas.api

import com.example.sipas.model.DataKehamilan
import com.example.sipas.model.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface DataKehamilanApi {

    @POST("/kehamilan")
    suspend fun insert(@Body kehamilan: DataKehamilan, @Header("Authorization") token: String)

    @GET("/kehamilan/list")
    suspend fun getList(@Header("Authorization") token: String, @Query("page") page: Int, @Query("limit") limit: Int): Response<List<DataKehamilan>>
}