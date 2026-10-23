package com.example.als_pocky.register4

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.als_pocky.R
import com.example.als_pocky.databinding.ActivityEmailGmailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.content.Intent

class EmailGmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailGmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailGmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar dengan tombol kembali kotak
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back_box)
        }
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnLanjut.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()

            // Validasi berurutan, berhenti di kesalahan pertama
            when {
                email.isEmpty() ->
                    tampilkanError("Email tidak boleh kosong.")

                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    tampilkanError("Format email tidak valid.")

                !email.lowercase().endsWith("@gmail.com") ->
                    tampilkanError("Email harus menggunakan domain @gmail.com.")

                else -> {
                    // Email valid -> buka halaman Registrasi sambil membawa email
                    val intent = Intent(this, RegisterActivity::class.java)
                    intent.putExtra(RegisterActivity.EXTRA_EMAIL, email)
                    startActivity(intent)
                }
            }
        }
    }

    // Menampilkan pesan error dengan MaterialAlertDialog (sesuai soal)
    private fun tampilkanError(pesan: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Email Tidak Valid")
            .setMessage(pesan)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}