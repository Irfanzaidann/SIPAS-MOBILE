package com.example.sipas.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.sipas.model.Authentication
import com.example.sipas.model.Response

@Dao
interface AuthenticationDao {

    @Insert
    @Transaction
    fun insertAuthInfo(data: Authentication)

    @Query("SELECT * FROM authentication LIMIT 1")
    fun getAuthInfo(): Authentication?

    @Query("DELETE FROM authentication")
    fun deleteAuthInfo()
}