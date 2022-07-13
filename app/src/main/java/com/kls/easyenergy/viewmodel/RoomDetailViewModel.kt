package com.kls.easyenergy.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kls.easyenergy.model.RoomDetailModel
import com.kls.jnecourierapps.utils.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomDetailViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val detailRoom = MutableLiveData<RoomDetailModel>()

    fun getRoomDetail(id: String, token: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailRoom("Bearer $token", id)
        client.enqueue(object : Callback<RoomDetailModel> {
            override fun onResponse(
                call: Call<RoomDetailModel>,
                response: Response<RoomDetailModel>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    detailRoom.value = response.body()
                } else {
                    Log.e("APIERROR", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RoomDetailModel>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}