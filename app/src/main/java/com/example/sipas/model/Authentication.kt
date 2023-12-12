package com.example.sipas.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authentication")
data class Authentication(

    @ColumnInfo(name = "authentication_data")
    val data: AuthenticationData,

    @ColumnInfo(name = "jwt_token")
    val jwtToken: String,

    @PrimaryKey
    val id: Int? = null,
){}