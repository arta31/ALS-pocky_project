package com.example.als_pocky.transaksi1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.als_pocky.databinding.ActivityHasilBagiBinding
import java.text.NumberFormat
import java.util.Locale

class HasilBagiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHasilBagiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilBagiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tangkap data total pendapatan dari PanenFragment
        val total = intent.getDoubleExtra("TOTAL_PENDAPATAN", 0.0)

        // Kalkulasi Persentase
        val danaRumahTangga = total * 0.75
        val danaPupuk = total * 0.15
        val danaRacun = total * 0.10

        // Tampilkan ke layar dengan format titik ribuan (Rp 250.000)
        binding.tvTotalPendapatan.text = formatRupiah(total)
        binding.tvRumahTangga.text = formatRupiah(danaRumahTangga)
        binding.tvPupuk.text = formatRupiah(danaPupuk)
        binding.tvRacun.text = formatRupiah(danaRacun)

        // Tombol Selesai -> tutup halaman ini, kembali ke BaseActivity (menu Beranda)
        binding.btnSelesai.setOnClickListener {
            finish()
        }
    }

    private fun formatRupiah(nilai: Double): String {
        val formatAngka = NumberFormat.getInstance(Locale("id", "ID"))
        return "Rp ${formatAngka.format(nilai.toLong())}"
    }
}
