package com.example.als_pocky.transaksi1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.als_pocky.databinding.ActivityHasilBagiBinding
import com.example.als_pocky.laporan5.DataLaporan
import com.example.als_pocky.laporan5.LaporanPdf
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.NumberFormat
import java.util.Locale

class HasilBagiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHasilBagiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilBagiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tangkap data dari PanenFragment
        val total = intent.getDoubleExtra("TOTAL_PENDAPATAN", 0.0)
        val berat = intent.getDoubleExtra("BERAT", 0.0)
        val harga = intent.getDoubleExtra("HARGA", 0.0)
        val tanggal = intent.getStringExtra("TANGGAL") ?: "-"

        // Kalkulasi Persentase
        val danaRumahTangga = total * 0.75
        val danaPupuk = total * 0.15
        val danaRacun = total * 0.10

        // Tampilkan ke layar dengan format titik ribuan (Rp 250.000)
        binding.tvTotalPendapatan.text = formatRupiah(total)
        binding.tvRumahTangga.text = formatRupiah(danaRumahTangga)
        binding.tvPupuk.text = formatRupiah(danaPupuk)
        binding.tvRacun.text = formatRupiah(danaRacun)

        // Fitur baru: buat PDF lalu bagikan
        binding.btnBagikanPdf.setOnClickListener {
            val namaPetani = getSharedPreferences("user_pref", MODE_PRIVATE)
                .getString("username", "Petani") ?: "Petani"
            bagikanLaporan(DataLaporan(namaPetani, tanggal, berat, harga, total))
        }

        // Tombol Selesai -> tutup halaman ini, kembali ke BaseActivity (menu Beranda)
        binding.btnSelesai.setOnClickListener {
            finish()
        }
    }

    private fun bagikanLaporan(data: DataLaporan) {
        try {
            val file = LaporanPdf.buat(this, data)

            // 💡 BARU: FileProvider mengubah File menjadi content:// URI yang aman dibagikan
            val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)

            val kirim = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Laporan Bagi Hasil Panen - SEMAWIT")
                putExtra(Intent.EXTRA_TEXT, "Laporan bagi hasil panen ${data.tanggal}")
                // 💡 BARU: izinkan aplikasi tujuan (WhatsApp, Gmail) membaca file ini sementara
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            // 💡 BARU: createChooser = tampilkan pilihan aplikasi untuk berbagi
            startActivity(Intent.createChooser(kirim, "Bagikan laporan lewat"))
        } catch (e: Exception) {
            MaterialAlertDialogBuilder(this)
                .setTitle("Gagal Membuat Laporan")
                .setMessage(e.message ?: "Terjadi kesalahan.")
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    private fun formatRupiah(nilai: Double): String {
        val formatAngka = NumberFormat.getInstance(Locale("id", "ID"))
        return "Rp ${formatAngka.format(nilai.toLong())}"
    }
}
