package com.example.als_pocky.transaksi1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.als_pocky.R
import com.example.als_pocky.base3.BaseActivity
import com.example.als_pocky.databinding.FragmentPanenBinding
import com.example.als_pocky.riwayat3.RiwayatFragment
import java.text.NumberFormat
import java.util.Locale

// Pindahan dari InputTransaksiActivity -> sekarang jadi menu "Panen" di bottom nav
class PanenFragment : Fragment() {

    private var _binding: FragmentPanenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPanenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Input Panen"
            setDisplayShowTitleEnabled(true)
        }

        binding.btnHitungBagi.setOnClickListener {
            val berat = binding.etBeratPanen.text.toString().toDoubleOrNull()
            val harga = binding.etHargaBuah.text.toString().toDoubleOrNull()

            if (berat != null && harga != null && berat > 0 && harga > 0) {
                val totalPendapatan = berat * harga

                // Format angka pakai titik ribuan
                val formatAngka = NumberFormat.getInstance(Locale("id", "ID"))
                val beratTeks = formatAngka.format(berat)
                val totalTeks = formatAngka.format(totalPendapatan.toLong())

                // Simpan ke riwayat (terbaru di atas)
                val transaksiBaru = "Panen $beratTeks Kg  •  Rp $totalTeks"
                val teksLama = RiwayatFragment.teksRiwayat
                RiwayatFragment.teksRiwayat = if (teksLama.isEmpty()) {
                    transaksiBaru
                } else {
                    transaksiBaru + "\n\n" + teksLama
                }

                // Kosongkan form, lalu pindah menu ke Beranda
                // supaya setelah "Kembali ke Beranda" user langsung lihat transaksi terakhir
                binding.etBeratPanen.text?.clear()
                binding.etHargaBuah.text?.clear()
                (requireActivity() as BaseActivity).bukaMenu(R.id.nav_beranda)

                // Buka halaman Hasil Bagi (masih Activity)
                val intent = Intent(requireContext(), HasilBagiActivity::class.java)
                intent.putExtra("TOTAL_PENDAPATAN", totalPendapatan)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Isi berat dan harga dengan benar!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
