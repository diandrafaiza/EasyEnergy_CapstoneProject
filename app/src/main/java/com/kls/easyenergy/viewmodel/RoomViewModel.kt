package com.kls.easyenergy.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kls.easyenergy.model.RoomListModel
import com.kls.easyenergy.model.RoomResponse
import com.kls.jnecourierapps.utils.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val roomList = MutableLiveData<ArrayList<RoomListModel>>()

    fun getRoom(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getRoomTask()
        client.enqueue(object : Callback<RoomResponse> {
            override fun onResponse(
                call: Call<RoomResponse>,
                response: Response<RoomResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    roomList.value = response.body()?.data
                } else {
                    Log.e("APIERROR", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RoomResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}