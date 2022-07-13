package com.kls.easyenergy.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kls.easyenergy.R
import com.kls.easyenergy.databinding.ActivityLoginBinding
import com.kls.easyenergy.databinding.ActivityVerifBinding

class VerifActivity : AppCompatActivity() {

    private lateinit var verifBinding: ActivityVerifBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifBinding = ActivityVerifBinding.inflate(layoutInflater)
        setContentView(verifBinding.root)

        verifBinding.btnSimpan.setOnClickListener {
            val intent = Intent(this, DetailVerifActivity::class.java)
            startActivity(intent)
        }
    }
}