package com.kls.easyenergy.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kls.easyenergy.R
import com.kls.easyenergy.adapter.ParticipantAdapter
import com.kls.easyenergy.adapter.RoomAdapter
import com.kls.easyenergy.databinding.ActivityDetailRoomBinding
import com.kls.easyenergy.databinding.ActivityHomeBinding
import com.kls.easyenergy.model.JoinRoomResponse
import com.kls.easyenergy.model.RoomDetailModel
import com.kls.easyenergy.utils.UserPreference
import com.kls.easyenergy.viewmodel.LoginViewModel
import com.kls.easyenergy.viewmodel.RoomDetailViewModel
import com.kls.easyenergy.viewmodel.RoomViewModel
import com.kls.easyenergy.viewmodel.ViewModelFactory
import com.kls.jnecourierapps.utils.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailRoom : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRoomBinding
    private lateinit var roomDetailViewModel: RoomDetailViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userToken: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("idroom").toString()
        roomDetailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[RoomDetailViewModel::class.java]
        setupViewModel(id)

    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun detailRoomTask(detailRoom: RoomDetailModel){
        binding.txtNameDetail.text = detailRoom.data.room.roomName
        binding.txtDeskripsiDetail.text = detailRoom.data.room.description
        binding.txtRewardDetail.text = detailRoom.data.room.reward.toString() + " mNRG"
        Glide.with(this)
            .load("https://i.ytimg.com/vi/rzsejNlvA8Y/maxresdefault.jpg")
            .into(binding.imgHeader)
        val adapter = ParticipantAdapter(detailRoom)
        binding.rvParticipant.adapter = adapter
        binding.rvParticipant.layoutManager = LinearLayoutManager(this)
        val startdate = detailRoom.data.room.startDate
        val enddate = detailRoom.data.room.endDate
        val rangedays = calculateDays(startdate.toLong(),enddate.toLong())
        binding.txtDateStart.text = "Tanggal Mulai : ${convertLongToTime(startdate.toLong())}"
        binding.txtRangeDay.text = "Lama Challenge : $rangedays Hari"
        binding.fabDaily.setOnClickListener {
            val intent = Intent(this,DailyReportActivity::class.java)
            intent.putExtra("start_mills",detailRoom.data.room.startDate)
            intent.putExtra("end_mills",detailRoom.data.room.endDate)
            intent.putExtra("dayrange",rangedays)
            intent.putExtra("id_room",detailRoom.data.room.id)
            startActivity(intent)
        }

        if (detailRoom.data.room.is_joined){
            binding.fabDaily.visibility = View.VISIBLE
        }else{
            binding.btnJoinRoom.visibility = View.VISIBLE

        }

    }

    private fun joinRoomTask(token: String, room_id: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().joinRoomTask("Bearer $token", room_id)
        client.enqueue(object : Callback<JoinRoomResponse> {
            override fun onResponse(call: Call<JoinRoomResponse>, response: Response<JoinRoomResponse>) {
                showLoading(false)
                if (response.isSuccessful){
                    binding.fabDaily.visibility = View.VISIBLE
                    binding.btnJoinRoom.visibility = View.INVISIBLE
                    Toast.makeText(this@DetailRoom, response.message(), Toast.LENGTH_SHORT).show()
                    roomDetailViewModel.getRoomDetail(room_id, userToken)
                }else{
                    Toast.makeText(this@DetailRoom, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<JoinRoomResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@DetailRoom, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupViewModel(id: String) {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            roomDetailViewModel.getRoomDetail(id,user.token)
            userToken = user.token
                roomDetailViewModel.detailRoom.observe(this){detail ->
                detailRoomTask(detail)
                binding.btnJoinRoom.setOnClickListener {
                    joinRoomTask(user.token,id)
                }
            }
            roomDetailViewModel.isLoading.observe(this) {
                showLoading(it)
            }
        }
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd MMMM yyyy")
        return format.format(date)
    }

    fun calculateDays(startdate: Long, enddate: Long): Long{
        val diff: Long = enddate - startdate
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

//        Toast.makeText(this, "jumlah hari : "+days, Toast.LENGTH_SHORT).show()
        return days
    }


}