package com.kls.easyenergy.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kls.easyenergy.activity.EditProfileActivity
import com.kls.easyenergy.activity.LockedStackingActivity
import com.kls.easyenergy.activity.VerifActivity
import com.kls.easyenergy.adapter.RoomAdapter
import com.kls.easyenergy.databinding.FragmentHomeBinding
import com.kls.easyenergy.databinding.FragmentProfileBinding
import com.kls.easyenergy.model.RoomListModel
import com.kls.easyenergy.viewmodel.RoomViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var roomViewModel: RoomViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        roomViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[RoomViewModel::class.java]
        roomViewModel.getRoom()
        roomViewModel.roomList.observe(viewLifecycleOwner){listRunsheet ->
            getRunsheet(listRunsheet)
        }
        roomViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.imgStake.setOnClickListener{
            val intent = Intent (requireContext(), LockedStackingActivity::class.java)
            startActivity(intent)
        }
        binding.cardTutorial.setOnClickListener {
            Toast.makeText(requireContext(), "Fitur ini sedang dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
        binding.imgWithdraw.setOnClickListener {
            Toast.makeText(requireContext(), "Fitur ini sedang dalam pengembangan", Toast.LENGTH_SHORT).show()
        }

        return root

    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbListroom.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getRunsheet(runsheet: ArrayList<RoomListModel>){
        val adapter = RoomAdapter(runsheet)
        binding.rvListroom.adapter = adapter
        binding.rvListroom.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL ,false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}