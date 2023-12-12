package com.example.sipas.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.example.sipas.api.OrangtuaApi
import com.example.sipas.database.AuthenticationDatabase
import com.example.sipas.databinding.ActivityProfileBinding
import com.example.sipas.model.Authentication
import com.example.sipas.model.UpdateOrangtua
import com.example.sipas.retrofit.Api
import com.example.sipas.util.translateExceptionIntoResponse
import com.example.sipas.view_model.authentication.AuthenticationViewModel
import com.example.sipas.view_model.authentication.AuthenticationViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class ProfileActivity: AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var authViewModel: AuthenticationViewModel

    private lateinit var namaAyah: TextInputEditText

    private lateinit var namaIbu: TextInputEditText

    private lateinit var email: TextInputEditText

    private lateinit var photoProfile: ImageView

    private lateinit var buttonUploadPhoto: MaterialButton

    private lateinit var buttonUpdate: MaterialButton

    private var image: File? = null

    private var auth: Authentication? = null

    private lateinit var back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(this.layoutInflater)

        setContentView(binding.root)

        namaAyah = binding.editNamaAyah
        namaIbu = binding.editNamaIbu
        email = binding.editEmail
        buttonUploadPhoto = binding.uploadImage
        photoProfile = binding.profilePicture
        buttonUpdate = binding.profileUpdate
        back = binding.imageView6
        authViewModel = AuthenticationViewModelFactory(
            AuthenticationDatabase
                .getInstance(this)
                .authenticationDao()
        ).create(AuthenticationViewModel::class.java)

        back.setOnClickListener {
            finish()
        }

        buttonUploadPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        }

        val coroutineExceptionHandler = CoroutineExceptionHandler{
                _, ex ->
            val resp = translateExceptionIntoResponse(ex)
            resp?.let {
                Toast.makeText(applicationContext, resp.message, Toast.LENGTH_SHORT)
            }
        }

        val retrofit = Api.getInstance()

        lifecycle.coroutineScope.launch(coroutineExceptionHandler){
            val currentAuth = authViewModel.getAuthentication()

            auth = currentAuth
            val bearerToken = "Bearer ${currentAuth?.jwtToken ?: ""}"

            val response = retrofit
                .create(OrangtuaApi::class.java)
                .getOrangtua(currentAuth?.data?.id ?: -1, bearerToken)
            val ortu = response.data
            ortu?.let {
                namaAyah.text = Editable
                    .Factory
                    .getInstance()
                    .newEditable(it.namaAyah)
                namaIbu.text = Editable
                    .Factory
                    .getInstance()
                    .newEditable(it.namaIbu)
                email.text = Editable
                    .Factory
                    .getInstance()
                    .newEditable(it.email)
                it.imageUrl?.let {
                        url ->
                    Picasso
                        .get()
                        .load(url)
                        .into(photoProfile)
                }
            }
        }

        buttonUpdate.setOnClickListener {
            val update = UpdateOrangtua(
                namaAyah = namaAyah.text.toString(),
                namaIbu = namaIbu.text.toString(),
                email = email.text.toString()
            )

            val bearerToken = "Bearer ${auth?.jwtToken ?: ""}"

            val strUpdate = Gson().toJson(update)

            val dtoRequestBody = strUpdate.toRequestBody("application/json".toMediaTypeOrNull())

            var imagePart: MultipartBody.Part? = null
            image?.let {
                val requestFile = RequestBody
                    .create("image/jpg".toMediaTypeOrNull(), it)

                requestFile?.let {
                    body ->
                    val part = MultipartBody.Part.createFormData("image", it.name, body)
                    imagePart = part
                }
            }

            lifecycle.coroutineScope.launch(coroutineExceptionHandler){
                try{
                    val ortu = retrofit
                        .create(OrangtuaApi::class.java)
                        .updateOrangtua(bearerToken, dtoRequestBody, imagePart)
                        .data

                    ortu?.let {
                        namaAyah.text = Editable
                            .Factory
                            .getInstance()
                            .newEditable(it.namaAyah)
                        namaIbu.text = Editable
                            .Factory
                            .getInstance()
                            .newEditable(it.namaIbu)
                        email.text = Editable
                            .Factory
                            .getInstance()
                            .newEditable(it.email)
                        Log.d("ProfileActivity", "url = ${it.imageUrl}")

                        it.imageUrl?.let {
                                url ->
                            Picasso
                                .get()
                                .load(url)
                                .into(photoProfile)
                        }
                    }

                    Toast.makeText(applicationContext, "sukses mengupdate data profile", Toast.LENGTH_SHORT).show()
                } catch (e: Exception){
                    Log.e("ProfileActivity", "error occurred with message ${e.message}")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == RESULT_OK){
            data?.let {
                val uri = data.data
                uri?.let {
                    val file = File(getRealPathFromURI(it))
                    image = file
                    Picasso
                        .get()
                        .load(uri)
                        .into(photoProfile)
                }
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri): String?{
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)

        cursor?.let {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            val filePath = it.getString(columnIndex)
            it.close()
            return filePath
        }
        return null
    }
}