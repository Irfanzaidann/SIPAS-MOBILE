package com.example.sipas.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.example.sipas.api.OrangtuaApi
import com.example.sipas.database.AuthenticationDatabase
import com.example.sipas.databinding.ActivityRegisterOrangtuaBinding
import com.example.sipas.model.Orangtua
import com.example.sipas.model.Response
import com.example.sipas.retrofit.Api
import com.example.sipas.ui.home.HomeActivity
import com.example.sipas.ui.login.LoginActivity
import com.example.sipas.view_model.authentication.AuthenticationViewModel
import com.example.sipas.view_model.authentication.AuthenticationViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRegisterOrangtuaBinding

    private lateinit var inputTextAyah: TextInputEditText

    private lateinit var inputTextIbu: TextInputEditText

    private lateinit var email: TextInputEditText

    private lateinit var password: TextInputEditText

    private lateinit var registerButton: MaterialButton

    private lateinit var progressBar: ProgressBar

    private lateinit var loginText: TextView

    private lateinit var authViewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        val activity = this

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterOrangtuaBinding.inflate(this.layoutInflater)
        setContentView(binding.root)

        inputTextAyah = binding.inputAyah
        inputTextIbu = binding.inputIbu
        email = binding.inputEmail
        password = binding.inputPassword
        registerButton = binding.registerButton
        progressBar = binding.progressBar
        loginText = binding.textView

        authViewModel = AuthenticationViewModelFactory(
            AuthenticationDatabase
                .getInstance(this)
                .authenticationDao()
        ).create(AuthenticationViewModel::class.java)

        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {

            runOnUiThread {
                progressBar.visibility = View.VISIBLE
            }

            val ortu = Orangtua(
                namaAyah = inputTextAyah.text?.toString() ?: "",
                namaIbu = inputTextIbu.text?.toString() ?: "",
                email = email.text?.toString() ?: "",
                password = password.text?.toString() ?: "",
                isConnectedWithFaskes = false
            )

            val coroutineExceptionHandler = CoroutineExceptionHandler{
                _, throwable ->
                runOnUiThread {
                    if(throwable is HttpException){
                        val strResponse = throwable
                            .response()
                            ?.errorBody()
                            ?.string()

                        val responseObj = Gson().fromJson(strResponse, Response::class.java)
                        Toast.makeText(this, responseObj.message, Toast.LENGTH_SHORT).show()
                    }
                    progressBar.visibility = View.GONE
                }
            }

            lifecycle.coroutineScope.launch(
                coroutineExceptionHandler
            ) {
                val response = Api
                    .getInstance()
                    .create(OrangtuaApi::class.java)
                    .insertOrangtua(ortu)
                runOnUiThread {
                    if(response.success){
                        Toast.makeText(activity, "sukses membuat data orangtua", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                    }
                    progressBar.visibility = View.GONE
                }

                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycle.coroutineScope.launch {
            val authentication = authViewModel.getAuthentication()
            authentication?.let {
                val intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}