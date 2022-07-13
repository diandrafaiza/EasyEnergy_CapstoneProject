package com.kls.easyenergy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kls.easyenergy.databinding.ParticipantItemBinding
import com.kls.easyenergy.model.RoomDetailModel

class ParticipantAdapter(private val listParticipant: RoomDetailModel): RecyclerView.Adapter<ParticipantAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantAdapter.ViewHolder {
        val binding = ParticipantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParticipantAdapter.ViewHolder, position: Int) {
        holder.bind(listParticipant,position)
    }

    override fun getItemCount(): Int {

        return listParticipant.data.room.participants.size
    }

    class ViewHolder(private val binding: ParticipantItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(roomDetailModel: RoomDetailModel,position: Int){

            binding.txtNameParti.text = roomDetailModel.data.room.participants[position].fullname
//            Glide.with(binding.root.context)
//                .load(roomDetailModel.participant[position].avatar)
//                .into(binding.imgProfileParti)

        }
    }

}


