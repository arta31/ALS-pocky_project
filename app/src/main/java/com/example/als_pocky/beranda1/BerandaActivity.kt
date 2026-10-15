package com.example.als_pocky.beranda1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.als_pocky.R
import com.example.als_pocky.auth2.AuthActivity
import com.example.als_pocky.databinding.ActivityBerandaBinding
import com.example.als_pocky.profile2.ProfileActivity
import com.example.als_pocky.transaksi1.InputTransaksiActivity
import com.example.als_pocky.web1.HargaSawitWebActivity

class BerandaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBerandaBinding

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
            val intent = Intent(this, InputTransaksiActivity::class.java)
            startActivity(intent)
        }

        binding.btnInfoHarga.setOnClickListener {
            val intent = Intent(this, HargaSawitWebActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (teksRiwayat.isNotEmpty()) {
            binding.tvIsiRiwayat.gravity = android.view.Gravity.START
            binding.tvIsiRiwayat.text = teksRiwayat
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Pastikan ID ini sama dengan yang ada di res/menu/main_menu.xml
            R.id.action_developer -> {
                startActivity(Intent(this, ProfileActivity::class.java))
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
            R.id.action_logout -> {
                AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Logout")
                    .setMessage("Apakah Anda yakin ingin keluar?")
                    .setPositiveButton("Ya") { dialog, _ ->
                        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.clear()
                        editor.apply()

                        dialog.dismiss()

                        val intent = Intent(this, AuthActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton("Tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}