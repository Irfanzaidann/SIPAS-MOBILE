package com.example.sipas.ui.resep_makanan

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sipas.adapter.ResepMakananAdapter
import com.example.sipas.api.ResepMakananApi
import com.example.sipas.database.AuthenticationDatabase
import com.example.sipas.databinding.ActivityResepMakananBinding
import com.example.sipas.model.ResepMakanan
import com.example.sipas.retrofit.Api
import com.example.sipas.util.translateExceptionIntoResponse
import com.example.sipas.view_model.authentication.AuthenticationViewModel
import com.example.sipas.view_model.authentication.AuthenticationViewModelFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class ResepMakananActivity: AppCompatActivity() {

    private lateinit var binding: ActivityResepMakananBinding

    private lateinit var rv: RecyclerView

    private lateinit var adapter: ResepMakananAdapter

    private lateinit var data: MutableList<ResepMakanan>

    private lateinit var authViewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResepMakananBinding.inflate(this.layoutInflater)

        setContentView(binding.root)

        data = mutableListOf()

        rv = binding.rv

        adapter = ResepMakananAdapter(data)

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

        val retrofit = Api
            .getInstance()
            .create(ResepMakananApi::class.java)

        authViewModel = AuthenticationViewModelFactory(
            AuthenticationDatabase
                .getInstance(applicationContext)
                .authenticationDao()
        ).create(AuthenticationViewModel::class.java)

        val coroutineExceptionHandler = CoroutineExceptionHandler{
                _, ex ->
            val resp = translateExceptionIntoResponse(ex)
            resp?.let {
                Toast.makeText(applicationContext, resp.message, Toast.LENGTH_SHORT)
            }
        }

        lifecycle.coroutineScope.launch(coroutineExceptionHandler) {
            val auth = authViewModel.getAuthentication()
            auth?.let {
                val token = "Bearer ${auth.jwtToken}"
                retrofit
                    .getResepMakanan(token, RequestBody.create("application/json".toMediaTypeOrNull(), "{}"), 100, 0)
                    .data
                    ?.let {
                        data.clear()
                        data.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
            }
        }
    }
}