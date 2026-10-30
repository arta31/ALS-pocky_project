package com.example.als_pocky.base3

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.als_pocky.R
import com.example.als_pocky.akun3.AkunFragment
import com.example.als_pocky.auth2.AuthActivity
import com.example.als_pocky.beranda1.BerandaFragment
import com.example.als_pocky.dana3.DanaFragment
import com.example.als_pocky.databinding.ActivityBaseBinding
import com.example.als_pocky.riwayat3.RiwayatFragment
import com.example.als_pocky.transaksi1.PanenFragment

class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Muat riwayat panen yang tersimpan (SharedPreferences "riwayat_pref")
        RiwayatFragment.muat(this)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // bottom = 0, karena BottomNavigationView sudah mengatur jarak bawahnya sendiri
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // Fragment pertama yang tampil: Beranda
        // (cek null agar tidak dobel saat layar diputar)
        if (savedInstanceState == null) {
            replaceFragment(BerandaFragment())
        }

        // Saat menu bottom nav diklik -> ganti fragment sesuai id menu
        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_beranda -> {
                    replaceFragment(BerandaFragment())
                    true
                }
                R.id.nav_dana -> {
                    replaceFragment(DanaFragment())
                    true
                }
                R.id.nav_panen -> {
                    replaceFragment(PanenFragment())
                    true
                }
                R.id.nav_riwayat -> {
                    replaceFragment(RiwayatFragment())
                    true
                }
                R.id.nav_akun -> {
                    replaceFragment(AkunFragment())
                    true
                }
                else -> false // return false jika item tidak ada yang di klik
            }
        }

        // Tombol + di tengah -> pilih menu Panen (memicu listener di atas)
        binding.fabPanen.setOnClickListener {
            bukaMenu(R.id.nav_panen)
        }
    }

    // Menampilkan fragment di fragmentContainer (mengganti fragment sebelumnya)
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            // tanpa addToBackStack -> tombol back langsung keluar aplikasi
            .commit()
    }

    // Dipanggil dari fragment untuk pindah menu (misal tombol "+ Input Panen" di Beranda).
    // Mengubah selectedItemId akan memicu setOnItemSelectedListener di atas.
    fun bukaMenu(menuId: Int) {
        binding.bottomNavView.selectedItemId = menuId
    }

    // Dipakai bersama oleh BerandaFragment (menu titik tiga) dan AkunFragment (tombol Logout)
    fun konfirmasiLogout() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setPositiveButton("Ya") { dialog, _ ->
                val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.clear()
                editor.apply()
                dialog.dismiss()
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
