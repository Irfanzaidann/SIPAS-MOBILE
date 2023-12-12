package com.example.sipas.util

import com.example.sipas.model.Response
import com.google.gson.Gson
import retrofit2.HttpException


fun translateExceptionIntoResponse(throwable: Throwable): Response<*>?{
    if(throwable is HttpException) {
        val strResponse = throwable
            .response()
            ?.errorBody()
            ?.string()

        return Gson().fromJson(strResponse, Response::class.java)
    }
    return null
}