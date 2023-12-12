package com.example.sipas.converter

import androidx.room.TypeConverter
import com.example.sipas.model.AuthenticationData
import com.google.gson.Gson

class AuthenticationDataConverter {

    @TypeConverter
    fun convertFromDataToString(data: AuthenticationData) = Gson().toJson(data)

    @TypeConverter
    fun convertFromStringToData(str: String) = Gson().fromJson(str, AuthenticationData::class.java)
}