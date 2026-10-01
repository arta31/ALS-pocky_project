package com.example.als_pocky.pertemuan4_

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
// Perbaikan ada di baris bawah ini: Ganti ActivityMainBinding menjadi ActivityFourthBinding
import com.example.als_pocky.databinding.ActivityFourthBinding

class FourthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFourthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding untuk FourthActivity
        binding = ActivityFourthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Logika tombol kembali
        binding.btnKembali.setOnClickListener {
            finish()
        }
    }
}