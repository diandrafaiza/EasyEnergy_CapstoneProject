package com.kls.easyenergy.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kls.easyenergy.R
import com.kls.easyenergy.activity.EditProfileActivity
import com.kls.easyenergy.activity.RegisterActivity
import com.kls.easyenergy.activity.VerifActivity
import com.kls.easyenergy.databinding.FragmentHomeBinding
import com.kls.easyenergy.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val bind = FragmentProfileBinding.inflate(layoutInflater)

        bind.btnEditProfile.setOnClickListener{
            val intent = Intent (requireContext(), EditProfileActivity::class.java)
           startActivity(intent)
        }

        bind.btnVerif.setOnClickListener{
            val intent = Intent (requireContext(), VerifActivity::class.java)
            startActivity(intent)
        }

        bind.btnRequestHost.setOnClickListener {
            Toast.makeText(requireContext(), "Fitur ini sedang dalam pengembangan", Toast.LENGTH_SHORT).show()
        }

        return bind.root
    }
}
