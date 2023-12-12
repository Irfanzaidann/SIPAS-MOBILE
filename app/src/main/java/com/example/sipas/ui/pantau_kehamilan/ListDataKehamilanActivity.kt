package com.example.sipas.ui.pantau_kehamilan

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sipas.adapter.DataKehamilanAdapter
import com.example.sipas.api.DataKehamilanApi
import com.example.sipas.database.AuthenticationDatabase
import com.example.sipas.databinding.ActivityListDataKehamilanBinding
import com.example.sipas.model.DataKehamilan
import com.example.sipas.retrofit.Api
import com.example.sipas.util.translateExceptionIntoResponse
import com.example.sipas.view_model.authentication.AuthenticationViewModel
import com.example.sipas.view_model.authentication.AuthenticationViewModelFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class ListDataKehamilanActivity: AppCompatActivity() {

    private lateinit var binding: ActivityListDataKehamilanBinding

    private lateinit var rv: RecyclerView

    private lateinit var adapter: DataKehamilanAdapter

    private lateinit var authViewModel: AuthenticationViewModel

    private lateinit var datas: MutableList<DataKehamilan>

    private lateinit var back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListDataKehamilanBinding.inflate(this.layoutInflater)

        setContentView(binding.root)

        rv = binding.rv
        authViewModel = AuthenticationViewModelFactory(
            AuthenticationDatabase
                .getInstance(applicationContext)
                .authenticationDao()
        ).create(AuthenticationViewModel::class.java)


        datas = mutableListOf<DataKehamilan>()

        adapter = DataKehamilanAdapter(datas)

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

        back = binding.back

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

        val retrofit = Api
            .getInstance()
            .create(DataKehamilanApi::class.java)

        lifecycle.coroutineScope.launch(coroutineExceptionHandler) {
            try{
                val auth = authViewModel.getAuthentication()
                val bearerToken = "Bearer ${auth?.jwtToken}"

                val res = retrofit
                    .getList(bearerToken, 0, 100)
                    .data

                res?.let {
                    datas.clear()
                    datas.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception){
                Log.e("ListDataKehamilanActivity", "error occured when get list data kehamilan with message ${e.message}")
            }

        }

    }
}