package com.example.sipas.ui.home

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.coroutineScope
import com.example.sipas.R
import com.example.sipas.api.OrangtuaApi
import com.example.sipas.api.OrangtuaFaskesApi
import com.example.sipas.database.AuthenticationDatabase
import com.example.sipas.databinding.ActivityHomeBinding
import com.example.sipas.retrofit.Api
import com.example.sipas.ui.login.LoginActivity
import com.example.sipas.ui.pantau_kehamilan.ListDataKehamilanActivity
import com.example.sipas.ui.pantau_kehamilan.PantauKehamilanActivity
import com.example.sipas.ui.profile.ProfileActivity
import com.example.sipas.ui.resep_makanan.ResepMakananActivity
import com.example.sipas.util.translateExceptionIntoResponse
import com.example.sipas.view_model.authentication.AuthenticationViewModel
import com.example.sipas.view_model.authentication.AuthenticationViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class HomeActivity: AppCompatActivity(){

    private lateinit var authViewModel: AuthenticationViewModel

    private lateinit var binding: ActivityHomeBinding

    private lateinit var welcomeText: TextView

    private lateinit var linearLayout: LinearLayout

    private lateinit var statusAccountDescription: TextView

    private lateinit var buttonStatus: MaterialButton

    private lateinit var imageLogout: ImageView

    private lateinit var button1: ImageButton

    private lateinit var button2: ImageButton

    private lateinit var button3: ImageButton

    private lateinit var button4: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(this.layoutInflater)

        setContentView(binding.root)

        Log.d("HomeActivity", "starting binding")

        welcomeText = binding.homeWelcome

        linearLayout = binding.linearLayout

        statusAccountDescription = binding.statusDescription

        buttonStatus = binding.buttonStatus

        imageLogout = binding.imageView5

        button1 = binding.button1

        button2 = binding.button2

        button3 = binding.button3

        button4 = binding.button4

        authViewModel = AuthenticationViewModelFactory(
            AuthenticationDatabase
                .getInstance(this)
                .authenticationDao()
        ).create(AuthenticationViewModel::class.java)

        val coroutineExceptionHandler = CoroutineExceptionHandler{
            _, ex ->
            val resp = translateExceptionIntoResponse(ex)
            resp?.let {
                Toast.makeText(applicationContext, resp.message, Toast.LENGTH_SHORT)
            }
        }

        button1.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener {
            val intent = Intent(applicationContext, PantauKehamilanActivity::class.java)
            startActivity(intent)
        }

        // TODO: Add Button 3 disini yak Mikail
        button3.setOnClickListener {
            val intent = Intent(applicationContext, ResepMakananActivity::class.java)
            startActivity(intent)
        }

        button4.setOnClickListener {
            val intent = Intent(applicationContext, ListDataKehamilanActivity::class.java)
            startActivity(intent)
        }

        val retrofit = Api.getInstance()

        imageLogout.setOnClickListener {
            lifecycle.coroutineScope.launch(coroutineExceptionHandler) {
                authViewModel.deleteAuthentication()
            }

            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        lifecycle.coroutineScope.launch(coroutineExceptionHandler) {
            val currentAuth = authViewModel.getAuthentication()
            val bearerToken = "Bearer ${currentAuth?.jwtToken ?: ""}"
            Log.d("AuthenticationActivity", "getting ortu with currentAuth $currentAuth")
            val response = retrofit
                .create(OrangtuaApi::class.java)
                .getOrangtua(currentAuth?.data?.id ?: -1, bearerToken)
            val ortu = response.data
            Log.d("AuthenticationActivity", "fetch ortu done, ortu = $ortu")

            ortu?.let {
                welcomeText.text = "Selamat Datang, ${ortu.namaIbu}"
                if(it.isConnectedWithFaskes){
                    val ortuFaskes = retrofit
                        .create(OrangtuaFaskesApi::class.java)
                        .getDescription(bearerToken)
                        .data

                    Log.d("AuthenticationActivity", "fetch ortufaskes decsription done, ortufaskes = $ortuFaskes")

                    linearLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.young_green))
                    statusAccountDescription.text = "Anda sudah terhubung dengan ${ortuFaskes?.faskes?.username}"
                    statusAccountDescription.setTextColor(resources.getColor(R.color.deep_green))
                    buttonStatus.setTextColor(resources.getColor(R.color.white))
                    buttonStatus.text = "Lihat Disini"
                    buttonStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.orange))
                } else {
                    linearLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.young_orange))
                    statusAccountDescription.text = "Anda belum terhubung ke fasilitas kesehatan"
                    statusAccountDescription.setTextColor(resources.getColor(R.color.deep_yellow))
                    buttonStatus.setTextColor(resources.getColor(R.color.white))
                    buttonStatus.text = "Hubungkan"
                    buttonStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.orange))
                }
            }
        }
    }
}