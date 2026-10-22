package com.example.als_pocky.akun3

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.als_pocky.base3.BaseActivity
import com.example.als_pocky.databinding.FragmentAkunBinding
import com.example.als_pocky.profile2.ProfileActivity

class AkunFragment : Fragment() {

    private var _binding: FragmentAkunBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAkunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Akun"
            setDisplayShowTitleEnabled(true)
        }

        // Ambil username yang disimpan saat login (requireContext karena di Fragment)
        val sharedPref = requireContext().getSharedPreferences("user_pref", MODE_PRIVATE)
        val username = sharedPref.getString("username", "Petani") ?: "Petani"
        binding.tvUsername.text = username
        binding.tvInisialAkun.text = username.take(2).uppercase()

        binding.btnProfilDeveloper.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }

        binding.menuPengaturan.setOnClickListener {
            Toast.makeText(requireContext(), "Pengaturan", Toast.LENGTH_SHORT).show()
        }

        binding.menuTentang.setOnClickListener {
            Toast.makeText(requireContext(), "SEMAWIT v1.0", Toast.LENGTH_SHORT).show()
        }

        // Logout: dialog + hapus sharedPref + finish() ada di BaseActivity
        binding.btnLogout.setOnClickListener {
            (requireActivity() as BaseActivity).konfirmasiLogout()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
