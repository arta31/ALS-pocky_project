package com.example.als_pocky.register4

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.als_pocky.auth2.AuthActivity
import com.example.als_pocky.databinding.ActivityRegisterSuccessBinding

class RegisterSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Baca data yang baru disimpan untuk membuktikan data sudah tersimpan
        val registerPref = getSharedPreferences("register_pref", MODE_PRIVATE)
        binding.tvNama.text = registerPref.getString("nama", "-")
        binding.tvEmail.text = registerPref.getString("email", "-")
        binding.tvUsername.text = "@" + registerPref.getString("username", "-")

        binding.btnKeLogin.setOnClickListener { kembaliKeLogin() }

        // Tombol Back HP juga diarahkan ke Login (bukan kembali ke form Registrasi)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                kembaliKeLogin()
            }
        })
    }

    // Kembali ke AuthActivity yang sudah ada di tumpukan,
    // sekaligus menutup halaman Email & Registrasi di atasnya
    private fun kembaliKeLogin() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }
}