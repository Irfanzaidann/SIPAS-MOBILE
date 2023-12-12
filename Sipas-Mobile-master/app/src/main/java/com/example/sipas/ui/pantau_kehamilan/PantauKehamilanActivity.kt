package com.example.sipas.ui.pantau_kehamilan

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.example.sipas.api.DataKehamilanApi
import com.example.sipas.database.AuthenticationDatabase
import com.example.sipas.databinding.ActivityPantauKehamilanBinding
import com.example.sipas.model.DataKehamilan
import com.example.sipas.retrofit.Api
import com.example.sipas.util.translateExceptionIntoResponse
import com.example.sipas.view_model.authentication.AuthenticationViewModel
import com.example.sipas.view_model.authentication.AuthenticationViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

class PantauKehamilanActivity: AppCompatActivity() {

    private lateinit var binding: ActivityPantauKehamilanBinding

    private lateinit var back: ImageView

    private lateinit var nama: TextInputEditText

    private lateinit var tanggal: TextInputEditText

    private lateinit var buttonSubmit: MaterialButton

    private lateinit var authViewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPantauKehamilanBinding.inflate(this.layoutInflater)

        setContentView(binding.root)

        back = binding.back
        nama = binding.editNamaCalonBayi
        tanggal = binding.editTanggalHaid
        buttonSubmit = binding.buttonSimpan
        authViewModel = AuthenticationViewModelFactory(
            AuthenticationDatabase.getInstance(applicationContext)
                .authenticationDao()
        ).create(AuthenticationViewModel::class.java)

        back.setOnClickListener {
            finish()
        }

        val coroutineExceptionHandler = CoroutineExceptionHandler{
                _, ex ->
            val resp = translateExceptionIntoResponse(ex)
            resp?.let {
                Toast.makeText(applicationContext, resp.message, Toast.LENGTH_SHORT)
            }
        }

        tanggal.setOnClickListener{
            showDatePicker()
        }

        val retrofit = Api
            .getInstance()
            .create(DataKehamilanApi::class.java)

        buttonSubmit.setOnClickListener {
            val namaBayi = nama.text.toString()
            val tanggalText = tanggal.text.toString()

            val dataKehamilan = DataKehamilan(namaBayi, tanggalText)
            lifecycle.coroutineScope.launch(coroutineExceptionHandler){

                val auth = authViewModel.getAuthentication()

                try{
                    retrofit
                        .insert(dataKehamilan, "Bearer ${auth?.jwtToken}")

                    nama.text = Editable.Factory
                        .getInstance()
                        .newEditable("")

                    tanggal.text =  Editable.Factory
                        .getInstance()
                        .newEditable("")

                    Toast.makeText(applicationContext, "sukses membuat data kehamilan", Toast.LENGTH_SHORT).show()
                } catch(e: Exception){
                    Log.e("PantauKehamilanActivity", "exception occurred when insert Data Kehamilan with message ${e.message}")
                }
            }
        }

    }

    private fun showDatePicker(){
        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("Tanggal pertama haid")
            .build()

        datePicker.addOnPositiveButtonClickListener {
            val selectedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = selectedDate.format(Date(it))
            tanggal.text = Editable
                .Factory
                .getInstance()
                .newEditable(formattedDate)
        }

        datePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
    }
}