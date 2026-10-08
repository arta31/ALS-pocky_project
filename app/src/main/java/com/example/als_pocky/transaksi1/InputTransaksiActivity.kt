package com.example.als_pocky.transaksi1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.als_pocky.beranda1.BerandaActivity
import com.example.als_pocky.databinding.ActivityInputTransaksiBinding
import java.text.NumberFormat
import java.util.Locale

class InputTransaksiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputTransaksiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Input Panen"
            setDisplayHomeAsUpEnabled(true)
        }

        binding.btnHitungBagi.setOnClickListener {
            val beratStr = binding.etBeratPanen.text.toString()
            val hargaStr = binding.etHargaBuah.text.toString()

            // toDoubleOrNull: kalau isinya aneh (misal "."), hasilnya null, aplikasi tidak crash
            val berat = beratStr.toDoubleOrNull()
            val harga = hargaStr.toDoubleOrNull()

            if (berat != null && harga != null && berat > 0 && harga > 0) {
                val totalPendapatan = berat * harga

                // Format angka pakai titik ribuan: 232.323 dan Rp 5.397.327.936
                val formatAngka = NumberFormat.getInstance(Locale("id", "ID"))
                val beratTeks = formatAngka.format(berat)
                val totalTeks = formatAngka.format(totalPendapatan.toLong())

                // 1. Buat teks transaksi baru
                val transaksiBaru = "Panen $beratTeks Kg  •  Rp $totalTeks"

                // 2. Gabungkan dengan teks lama (jarak 1 baris kosong, hanya kalau riwayat sudah ada isinya)
                val teksLama = BerandaActivity.teksRiwayat
                BerandaActivity.teksRiwayat = if (teksLama.isEmpty()) {
                    transaksiBaru
                } else {
                    transaksiBaru + "\n\n" + teksLama
                }

                // 3. Pindah ke Halaman Hasil
                val intent = Intent(this, HasilBagiActivity::class.java)
                intent.putExtra("TOTAL_PENDAPATAN", totalPendapatan)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Isi berat dan harga dengan benar!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}