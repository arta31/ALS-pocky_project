package com.example.als_pocky.web1

import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.als_pocky.databinding.ActivityHargaSawitWebBinding

class HargaSawitWebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHargaSawitWebBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHargaSawitWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Mengaktifkan Toolbar & Tombol Back[cite: 3]
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Info Harga Sawit"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // 2. Konfigurasi WebView[cite: 3]
        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.javaScriptEnabled = true
        // Memuat portal berita kelapa sawit sungguhan
        binding.webView.loadUrl("https://www.infosawit.com/")

        // 3. Animasi Toolbar sembunyi/tampil saat web di-scroll[cite: 3]
        binding.webView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) {
                binding.appBar.setExpanded(false, true) // Sembunyikan
            } else if (scrollY < oldScrollY) {
                binding.appBar.setExpanded(true, true) // Tampilkan
            }
        }
    }

    // Menangkap aksi klik pada tombol back di Toolbar bawaan
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Mengambil alih tombol back sistem agar menavigasi riwayat web terlebih dahulu[cite: 3]
    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}