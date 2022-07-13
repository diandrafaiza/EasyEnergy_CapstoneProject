package com.kls.easyenergy.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kls.easyenergy.R
import com.kls.easyenergy.databinding.ActivityEditProfileBinding
import com.kls.easyenergy.databinding.ActivityLoginBinding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var editProfileBinding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editProfileBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(editProfileBinding.root)

        editProfileBinding.btnSimpan.setOnClickListener {
            finish()
        }
    }
}