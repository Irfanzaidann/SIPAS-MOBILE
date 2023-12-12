package com.example.sipas.view_model.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sipas.dao.AuthenticationDao
import com.example.sipas.model.Authentication
import com.example.sipas.model.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticationViewModel(
    private val authDao: AuthenticationDao
): ViewModel() {

    suspend fun insertAuthenticationInfo(response: Authentication){
        withContext(Dispatchers.IO){
            Log.d("AuthenticationViewModel", "inserting auth info into room")
            authDao.insertAuthInfo(response)
            Log.d("AuthenticationViewModel", "finish inserting auth info into room")
        }
    }

    suspend fun getAuthentication(): Authentication? = withContext(Dispatchers.IO){
        authDao.getAuthInfo()
    }

    suspend fun deleteAuthentication() = withContext(Dispatchers.IO){
        authDao.deleteAuthInfo()
    }
}