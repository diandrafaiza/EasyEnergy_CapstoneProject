package com.kls.easyenergy.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.kls.easyenergy.R
import com.kls.easyenergy.databinding.ActivityDetailVerifBinding
import com.kls.easyenergy.databinding.ActivityVerifBinding

class DetailVerifActivity : AppCompatActivity() {

    private lateinit var activityBinding: ActivityDetailVerifBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityDetailVerifBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        activityBinding.btnSimpanDetailMeteran.setOnClickListener {
            Toast.makeText(this, "Fitur ini sedang dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
    }
}