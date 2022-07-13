package com.kls.easyenergy.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kls.easyenergy.R
import com.kls.easyenergy.databinding.ActivityDetailRoomBinding
import com.kls.easyenergy.databinding.ActivityLockedStackingBinding

class LockedStackingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLockedStackingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLockedStackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStake.setOnClickListener {
            Toast.makeText(this, "Fitur ini sedang dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
    }
}