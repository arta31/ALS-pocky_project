package com.example.als_pocky.onboarding6

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.als_pocky.databinding.ActivityOnboardingBinding

// Progres 6 (P11): On Board Screen pengenalan fitur SEMAWIT
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup ViewPager2 dengan adapter (sama seperti modul)
        val fragmentsList = listOf(Onboarding1Fragment(), Onboarding2Fragment(), Onboarding3Fragment())
        val adapter = OnboardingAdapter(this, fragmentsList)
        binding.onboardingViewPager.adapter = adapter

        // Hubungkan titik indikator dengan ViewPager2
        binding.dotIndicator.attachTo(binding.onboardingViewPager)

        // 💡 BARU: registerOnPageChangeCallback = dipanggil setiap halaman berganti
        binding.onboardingViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val halamanTerakhir = position == fragmentsList.lastIndex
                // Tombol "Mulai" hanya muncul di halaman terakhir, "Lewati" disembunyikan
                binding.btnMulai.visibility = if (halamanTerakhir) View.VISIBLE else View.INVISIBLE
                binding.tvLewati.visibility = if (halamanTerakhir) View.INVISIBLE else View.VISIBLE
            }
        })

        binding.tvLewati.setOnClickListener { selesai() }
        binding.btnMulai.setOnClickListener { selesai() }
    }

    // Sementara cukup tutup halaman. Step berikutnya: simpan status & lanjut ke Login
    private fun selesai() {
        finish()
    }
}