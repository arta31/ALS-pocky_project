package com.example.als_pocky.akun3

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.als_pocky.base3.BaseActivity
import com.example.als_pocky.databinding.FragmentAkunBinding
import com.example.als_pocky.profile2.ProfileActivity
import android.widget.SimpleAdapter
import com.example.als_pocky.R
import com.example.als_pocky.onboarding6.OnboardingActivity

class AkunFragment : Fragment() {

    private var _binding: FragmentAkunBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAkunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Akun"
            setDisplayShowTitleEnabled(true)
        }

        // Ambil username yang disimpan saat login (requireContext karena di Fragment)
        val sharedPref = requireContext().getSharedPreferences("user_pref", MODE_PRIVATE)
        val username = sharedPref.getString("username", "Petani") ?: "Petani"
        binding.tvUsername.text = username
        binding.tvInisialAkun.text = username.take(2).uppercase()

//        ====== SIMPLE VIEW HAPUS =====
//        binding.btnProfilDeveloper.setOnClickListener {
//            startActivity(Intent(requireContext(), ProfileActivity::class.java))
//        }
//
//        binding.menuPengaturan.setOnClickListener {
//            Toast.makeText(requireContext(), "Pengaturan", Toast.LENGTH_SHORT).show()
//        }
//
//        binding.menuTentang.setOnClickListener {
//            Toast.makeText(requireContext(), "SEMAWIT v1.0", Toast.LENGTH_SHORT).show()
//        }

//       ============ ganti ini================
        // Progres 6 (P11): menu Akun pakai ListView + SimpleAdapter
        // 1 Map = 1 baris menu (key -> isi)
        val menuAkun = listOf(
            mapOf("ikon" to R.drawable.ic_nav_akun, "judul" to "Profil Developer", "keterangan" to "Biodata pembuat aplikasi"),
            mapOf("ikon" to R.drawable.ic_info, "judul" to "Panduan Aplikasi", "keterangan" to "Lihat lagi pengenalan fitur"),
            mapOf("ikon" to R.drawable.ic_settings, "judul" to "Pengaturan", "keterangan" to "Atur preferensi aplikasi"),
            mapOf("ikon" to R.drawable.ic_shield, "judul" to "Tentang SEMAWIT", "keterangan" to "Versi 1.0")
        )

        // 💡 BARU: SimpleAdapter memasangkan key Map (from) ke id View (to) secara berurutan
        val menuAdapter = SimpleAdapter(
            requireContext(),
            menuAkun,                                                   // data
            R.layout.item_menu_akun,                                    // layout 1 baris
            arrayOf("ikon", "judul", "keterangan"),                     // from: key di Map
            intArrayOf(R.id.ivIkon, R.id.tvJudul, R.id.tvKeterangan)    // to: id view
        )
        binding.lvMenuAkun.adapter = menuAdapter

        // Klik baris menu berdasarkan posisinya (0 = paling atas)
        binding.lvMenuAkun.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> startActivity(Intent(requireContext(), ProfileActivity::class.java))
                1 -> startActivity(Intent(requireContext(), OnboardingActivity::class.java))
                2 -> Toast.makeText(requireContext(), "Pengaturan", Toast.LENGTH_SHORT).show()
                3 -> Toast.makeText(requireContext(), "SEMAWIT v1.0", Toast.LENGTH_SHORT).show()
            }
        }

        // Logout: dialog + hapus sharedPref + finish() ada di BaseActivity
        binding.btnLogout.setOnClickListener {
            (requireActivity() as BaseActivity).konfirmasiLogout()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
