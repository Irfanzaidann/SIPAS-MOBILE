package com.example.sipas.view_model.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sipas.dao.AuthenticationDao

class AuthenticationViewModelFactory(
    private val dao: AuthenticationDao
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthenticationViewModel::class.java)){
            return AuthenticationViewModel(dao) as T
        } else {
            throw IllegalArgumentException("invalid view model type")
        }
    }
}