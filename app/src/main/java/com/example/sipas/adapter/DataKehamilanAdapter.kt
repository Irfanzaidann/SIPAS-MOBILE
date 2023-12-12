package com.example.sipas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sipas.databinding.CardDataKehamilanBinding
import com.example.sipas.model.DataKehamilan

class DataKehamilanAdapter(
    private val data: List<DataKehamilan>
): RecyclerView.Adapter<DataKehamilanAdapter.DataKehamilanViewHolder>() {

    inner class DataKehamilanViewHolder(
        private val binding: CardDataKehamilanBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(dataKehamilan: DataKehamilan){
            binding.nama.text = dataKehamilan.namaCalonBayi
            binding.tanggalHaid.text = dataKehamilan.tanggalPertamaHaid
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataKehamilanViewHolder {
        val binding = CardDataKehamilanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataKehamilanViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onBindViewHolder(holder: DataKehamilanViewHolder, position: Int) {
        val data = this.data[position]
        holder.bind(data)
    }
}