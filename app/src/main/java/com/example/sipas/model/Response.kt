package com.example.sipas.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "response")
data class Response<T>(

    var message: String,

    var data: T?,

    var success: Boolean,

    var jwtToken: String
){
    constructor(): this( "", null, false, "")
}
