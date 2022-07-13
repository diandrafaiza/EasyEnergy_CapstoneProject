package com.kls.easyenergy.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kls.easyenergy.activity.DetailRoom
import com.kls.easyenergy.databinding.RoomItemBinding
import com.kls.easyenergy.model.RoomListModel

class RoomAdapter(private val listRoom: ArrayList<RoomListModel>): RecyclerView.Adapter<RoomAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RoomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listRoom[position])
    }

    override fun getItemCount(): Int {
        return listRoom.size
    }

    class ViewHolder(private val binding: RoomItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(roomResponseModel: RoomListModel){
            binding.txtNamaRoom.text = roomResponseModel.roomName
            binding.txtAkses.text = roomResponseModel.category
            binding.txtChallenge.text = roomResponseModel.description
            binding.txtPartisipan.text = "${roomResponseModel.numberOfParticipants} Partisipan Terdaftar"
            Glide.with(binding.root.context)
                .load("https://i.ytimg.com/vi/rzsejNlvA8Y/maxresdefault.jpg")
                .into(binding.imgHeader)
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context,DetailRoom::class.java)
                intent.putExtra("idroom",roomResponseModel.id)
                binding.root.context.startActivity(intent)
            }
        }
    }

}