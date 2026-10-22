package com.example.als_pocky.riwayat3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.als_pocky.databinding.FragmentRiwayatBinding
import com.example.als_pocky.databinding.ItemRiwayatBinding

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
            title = "Riwayat transaksi"
            setDisplayShowTitleEnabled(true)
        }

        if (teksRiwayat.isNotEmpty()) {
            binding.cardKosong.visibility = View.GONE
            binding.cardDaftar.visibility = View.VISIBLE

            // Setiap transaksi dipisah baris kosong
            val daftar = teksRiwayat.split("\n\n")
            daftar.forEachIndexed { index, transaksi ->
                // Format: "Panen 100 Kg  •  Rp 250.000  •  22 Okt 2026, 18.30"
                val bagian = transaksi.split("  •  ")

                // Buat 1 baris dari item_riwayat.xml, isi datanya, lalu tambahkan ke container
                val item = ItemRiwayatBinding.inflate(layoutInflater, binding.containerRiwayat, false)
                item.tvJudul.text = bagian[0]
                item.tvNominal.text = "+" + bagian.getOrElse(1) { "" }
                item.tvTanggal.text = bagian.getOrElse(2) { "Bagi hasil otomatis" }
                // Baris terakhir tidak perlu garis pemisah
                item.divider.visibility = if (index == daftar.lastIndex) View.GONE else View.VISIBLE
                binding.containerRiwayat.addView(item.root)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
