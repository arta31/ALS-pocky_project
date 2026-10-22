package com.example.als_pocky.riwayat3

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.als_pocky.databinding.FragmentRiwayatBinding

class RiwayatFragment : Fragment() {

    // Data riwayat (pindahan dari BerandaActivity.teksRiwayat).
    // companion object = bisa diakses tanpa membuat objek: RiwayatFragment.teksRiwayat
    companion object {
        var teksRiwayat = ""
    }

    private var _binding: FragmentRiwayatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Riwayat"
            setDisplayShowTitleEnabled(true)
        }

        if (teksRiwayat.isNotEmpty()) {
            binding.tvDaftarRiwayat.gravity = Gravity.START
            binding.tvDaftarRiwayat.text = teksRiwayat
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
