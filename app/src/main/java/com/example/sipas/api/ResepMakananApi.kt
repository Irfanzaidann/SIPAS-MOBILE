package com.example.sipas.api

import com.example.sipas.model.ResepMakanan
import com.example.sipas.model.Response
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ResepMakananApi {

    @POST("/resep/makanan/kehamilan/judul")
    suspend fun getResepMakanan(@Header("Authorization") token: String, @Body body: RequestBody, @Query("limit") limit: Int, @Query("page") page: Int): Response<List<ResepMakanan>>
}