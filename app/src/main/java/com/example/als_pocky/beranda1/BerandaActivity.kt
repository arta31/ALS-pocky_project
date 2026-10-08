package com.example.als_pocky.beranda1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.als_pocky.databinding.ActivityBerandaBinding
import android.widget.PopupMenu
import android.widget.Toast
import com.example.als_pocky.R

class BerandaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBerandaBinding

    // Variabel biasa untuk menyimpan teks gabungan
    companion object {
        var teksRiwayat = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.ivNotif.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.menu_notif, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.notif_tandai_dibaca -> {
                        Toast.makeText(this, "Semua notifikasi ditandai dibaca", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.notif_lihat_semua -> {
                        Toast.makeText(this, "Belum ada notifikasi", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        binding.btnInputTransaksi.setOnClickListener {
            val intent = Intent(this, com.example.als_pocky.transaksi1.InputTransaksiActivity::class.java)
            startActivity(intent)
        }

        binding.btnInfoHarga.setOnClickListener {
            val intent = Intent(this, com.example.als_pocky.web1.HargaSawitWebActivity::class.java)
            startActivity(intent)
        }
    }

    // Dipanggil setiap kali halaman beranda muncul di layar
    override fun onResume() {
        super.onResume()
        if (teksRiwayat.isNotEmpty()) {
            binding.tvIsiRiwayat.gravity = android.view.Gravity.START
            binding.tvIsiRiwayat.text = teksRiwayat
        }
    }
    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profil -> {
                Toast.makeText(this, "Profil Petani", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_settings -> {
                Toast.makeText(this, "Pengaturan", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_tentang -> {
                Toast.makeText(this, "SEMAWIT v1.0", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}