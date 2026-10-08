package com.example.als_pocky.transaksi1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.als_pocky.databinding.ActivityHasilBagiBinding
import com.example.als_pocky.R

class HasilBagiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHasilBagiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilBagiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            title = "Hasil Bagi"
            setDisplayHomeAsUpEnabled(true)
        }
        binding.toolbar.setNavigationOnClickListener { finish() }
        // Tangkap data total pendapatan dari form sebelumnya
        val total = intent.getDoubleExtra("TOTAL_PENDAPATAN", 0.0)

        // Kalkulasi Persentase
        val danaRumahTangga = total * 0.75
        val danaPupuk = total * 0.15
        val danaRacun = total * 0.10

        // Tampilkan ke layar
        binding.tvTotalPendapatan.text = "Total: Rp ${total.toLong()}"
        binding.tvRumahTangga.text = "Rp ${danaRumahTangga.toLong()}"
        binding.tvPupuk.text = "Rp ${danaPupuk.toLong()}"
        binding.tvRacun.text = "Rp ${danaRacun.toLong()}"

        // Tombol Selesai
        binding.btnSelesai.setOnClickListener {
            finish() // Menutup halaman ini dan kembali ke Beranda (karena Beranda ada di tumpukan bawah)
        }
    }
}