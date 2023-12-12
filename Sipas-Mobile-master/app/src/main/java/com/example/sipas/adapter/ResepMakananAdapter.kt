package com.example.sipas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sipas.databinding.CardViewMakananBinding
import com.example.sipas.model.ResepMakanan
import com.squareup.picasso.Picasso

class ResepMakananAdapter(
    private val datas: List<ResepMakanan>
): RecyclerView.Adapter<ResepMakananAdapter.ResepMakananViewHolder>() {

    inner class ResepMakananViewHolder(val binding: CardViewMakananBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(rp: ResepMakanan){
            Picasso
                .get()
                .load(rp.urlGambar)
                .into(binding.gambarMakanan)
            binding.namaMakanan.text = rp.judulResep
            binding.trimester.text = rp.targetUsiaResep
            binding.waktuMakan.text = rp.jenis
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResepMakananViewHolder {
        val binding = CardViewMakananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResepMakananViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return this.datas.size
    }

    override fun onBindViewHolder(holder: ResepMakananViewHolder, position: Int) {
        val data = this.datas[position]

        holder.bind(data)
    }
}