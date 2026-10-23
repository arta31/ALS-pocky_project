package com.example.als_pocky.auth2

import android.content.Intent
import android.os.Bundle
//import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.appcompat.app.AppCompatActivity
import com.example.als_pocky.base3.BaseActivity
import com.example.als_pocky.databinding.ActivityAuthBinding
import com.example.als_pocky.register4.EmailGmailActivity

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Akses penyimpanan "user_pref"
        val sharedPref = getSharedPreferences(
            "user_pref", MODE_PRIVATE)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString()

            // Ambil akun hasil registrasi (Soal 2)
            val registerPref = getSharedPreferences("register_pref", MODE_PRIVATE)
            val usernameTerdaftar = registerPref.getString("username", null)
            val passwordTerdaftar = registerPref.getString("password", null)

            // Kondisi 1: username == password (aturan lama)
            val loginCara1 = username == password
            // Kondisi 2: username & password cocok dengan data di SharedPreferences
            val loginCara2 = username == usernameTerdaftar && password == passwordTerdaftar

            when {
                username.isEmpty() || password.isEmpty() ->
                    tampilkanError("Username dan password wajib diisi.")

                loginCara1 || loginCara2 -> {
                    // Login berhasil: simpan status login & username
                    val editor = sharedPref.edit()
                    editor.putBoolean("isLogin", true)
                    editor.putString("username", username)
                    editor.apply()

                    // Arahkan ke Home (BaseActivity dengan bottom nav)
                    startActivity(Intent(this, BaseActivity::class.java))
                    finish()
                }

                else ->
                    tampilkanError("Username atau password salah. Silakan coba lagi.")
            }
        }
        // Register With Gmail -> halaman input email
        binding.btnRegisterGmail.setOnClickListener {
            startActivity(Intent(this, EmailGmailActivity::class.java))
        }
    }
    private fun tampilkanError(pesan: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Login Gagal")
            .setMessage(pesan)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}