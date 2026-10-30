package com.example.als_pocky.transaksi1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.als_pocky.R
import com.example.als_pocky.base3.BaseActivity
import com.example.als_pocky.databinding.FragmentPanenBinding
import com.example.als_pocky.riwayat3.RiwayatFragment
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
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

        // Estimasi pendapatan berubah otomatis setiap kali user mengetik
        binding.etBeratPanen.doAfterTextChanged { hitungEstimasi() }
        binding.etHargaBuah.doAfterTextChanged { hitungEstimasi() }

        binding.btnHitungBagi.setOnClickListener {
            val berat = binding.etBeratPanen.text.toString().toDoubleOrNull()
            val harga = binding.etHargaBuah.text.toString().toDoubleOrNull()

            if (berat != null && harga != null && berat > 0 && harga > 0) {
                val totalPendapatan = berat * harga

                // Format angka pakai titik ribuan
                val formatAngka = NumberFormat.getInstance(Locale("id", "ID"))
                val beratTeks = formatAngka.format(berat)
                val totalTeks = formatAngka.format(totalPendapatan.toLong())

                // Simpan ke riwayat (terbaru di atas), lengkap dengan tanggal
                val tanggal = SimpleDateFormat("dd MMM yyyy, HH.mm", Locale("id", "ID")).format(Date())
                val transaksiBaru = "Panen $beratTeks Kg  •  Rp $totalTeks  •  $tanggal"
                val teksLama = RiwayatFragment.teksRiwayat
                RiwayatFragment.teksRiwayat = if (teksLama.isEmpty()) {
                    transaksiBaru
                } else {
                    transaksiBaru + "\n\n" + teksLama
                }

                // Simpan riwayat ke SharedPreferences agar tidak hilang saat aplikasi ditutup
                RiwayatFragment.simpan(requireContext())

                // Kosongkan form, lalu pindah menu ke Beranda
                // supaya setelah "Kembali ke Beranda" user langsung lihat transaksi terakhir
                binding.etBeratPanen.text?.clear()
                binding.etHargaBuah.text?.clear()
                (requireActivity() as BaseActivity).bukaMenu(R.id.nav_beranda)

                // Buka halaman Hasil Bagi (masih Activity)
                val intent = Intent(requireContext(), HasilBagiActivity::class.java)
                intent.putExtra("TOTAL_PENDAPATAN", totalPendapatan)
                // Data tambahan untuk Laporan PDF
                intent.putExtra("BERAT", berat)
                intent.putExtra("HARGA", harga)
                intent.putExtra("TANGGAL", tanggal)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Isi berat dan harga dengan benar!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Menghitung berat x harga lalu menampilkannya di tvEstimasi
    private fun hitungEstimasi() {
        val berat = binding.etBeratPanen.text.toString().toDoubleOrNull() ?: 0.0
        val harga = binding.etHargaBuah.text.toString().toDoubleOrNull() ?: 0.0
        val formatAngka = NumberFormat.getInstance(Locale("id", "ID"))
        binding.tvEstimasi.text = "Rp ${formatAngka.format((berat * harga).toLong())}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
