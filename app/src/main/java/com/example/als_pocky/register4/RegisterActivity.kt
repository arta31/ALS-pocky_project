package com.example.als_pocky.register4

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.als_pocky.R
import com.example.als_pocky.databinding.ActivityRegisterBinding
import android.content.Intent
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RegisterActivity : AppCompatActivity() {

    // Kunci (key) untuk mengirim email dari EmailGmailActivity
    companion object {
        const val EXTRA_EMAIL = "EXTRA_EMAIL"
    }

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back_box)
        }
        binding.toolbar.setNavigationOnClickListener { finish() }

        // Ambil email dari halaman sebelumnya lalu tampilkan (field-nya terkunci)
        val email = intent.getStringExtra(EXTRA_EMAIL) ?: ""
        binding.etEmail.setText(email)

        binding.btnRegistrasi.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString()

            // Validasi berurutan sesuai soal
            when {
                nama.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() ->
                    tampilkanError("Semua field wajib diisi.")

                username.any { it.isWhitespace() } ->
                    tampilkanError("Username tidak boleh mengandung spasi.")

                password.length < 6 ->
                    tampilkanError("Password minimal 6 karakter.")

                else -> {
                    // Simpan seluruh data registrasi ke SharedPreferences "register_pref"
                    // (terpisah dari "user_pref" agar tidak terhapus saat logout)
                    val registerPref = getSharedPreferences("register_pref", MODE_PRIVATE)
                    val editor = registerPref.edit()
                    editor.putString("nama", nama)
                    editor.putString("email", email)
                    editor.putString("username", username)
                    editor.putString("password", password)
                    editor.apply()

                    // Tampilkan halaman informasi "Registrasi Berhasil"
                    startActivity(Intent(this, RegisterSuccessActivity::class.java))
                    finish()
                }
            }
        }
    }
    private fun tampilkanError(pesan: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Registrasi Gagal")
            .setMessage(pesan)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}