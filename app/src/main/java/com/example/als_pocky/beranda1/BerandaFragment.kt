package com.example.als_pocky.beranda1

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import java.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.example.als_pocky.R
import com.example.als_pocky.base3.BaseActivity
import com.example.als_pocky.databinding.FragmentBerandaBinding
import com.example.als_pocky.profile2.ProfileActivity
import com.example.als_pocky.riwayat3.RiwayatFragment
import com.example.als_pocky.web1.HargaSawitWebActivity

class BerandaFragment : Fragment() {

    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar: di Fragment harus lewat (requireActivity() as AppCompatActivity)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        // Menu titik tiga di toolbar (pengganti onCreateOptionsMenu di Activity)
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_developer -> {
                        startActivity(Intent(requireContext(), ProfileActivity::class.java))
                        true
                    }
                    R.id.action_settings -> {
                        Toast.makeText(requireContext(), "Pengaturan", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_tentang -> {
                        Toast.makeText(requireContext(), "SEMAWIT v1.0", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_logout -> {
                        (requireActivity() as BaseActivity).konfirmasiLogout()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Nama user dari SharedPreferences (disimpan saat login)
        val sharedPref = requireContext().getSharedPreferences("user_pref", MODE_PRIVATE)
        val username = sharedPref.getString("username", "Petani") ?: "Petani"
        binding.tvNamaUser.text = username
        binding.tvInisial.text = username.take(2).uppercase()

        // Sapaan sesuai jam
        val jam = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        binding.tvSapaan.text = when (jam) {
            in 4..10 -> "Selamat pagi,"
            in 11..14 -> "Selamat siang,"
            in 15..17 -> "Selamat sore,"
            else -> "Selamat malam,"
        }

        // Popup notifikasi
        binding.ivNotif.setOnClickListener { v ->
            val popup = PopupMenu(requireContext(), v)
            popup.menuInflater.inflate(R.menu.menu_notif, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.notif_tandai_dibaca -> {
                        Toast.makeText(requireContext(), "Semua notifikasi ditandai dibaca", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.notif_lihat_semua -> {
                        Toast.makeText(requireContext(), "Belum ada notifikasi", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        // "Input Panen" -> pindah ke menu Panen di bottom nav
        binding.btnInputTransaksi.setOnClickListener {
            (requireActivity() as BaseActivity).bukaMenu(R.id.nav_panen)
        }

        // "Harga Sawit" -> tetap membuka Activity WebView
        binding.btnInfoHarga.setOnClickListener {
            startActivity(Intent(requireContext(), HargaSawitWebActivity::class.java))
        }

        // "Lihat semua" -> pindah ke menu Riwayat
        binding.tvLihatSemua.setOnClickListener {
            (requireActivity() as BaseActivity).bukaMenu(R.id.nav_riwayat)
        }
    }

    override fun onResume() {
        super.onResume()
        val riwayat = RiwayatFragment.teksRiwayat
        if (riwayat.isNotEmpty()) {
            // Setiap transaksi dipisah baris kosong, terbaru di paling atas
            val daftar = riwayat.split("\n\n")
            binding.tvJumlahTransaksi.text = "${daftar.size} transaksi panen tercatat"

            // Format 1 transaksi: "Panen 100 Kg  •  Rp 250.000  •  22 Okt 2026, 18.30"
            val bagian = daftar.first().split("  •  ")
            binding.tvJudulTerakhir.text = bagian[0]
            binding.tvNominalTerakhir.text = bagian.getOrElse(1) { "" }
            binding.tvTanggalTerakhir.text = bagian.getOrElse(2) { "Hari ini" }

            binding.tvIsiRiwayat.visibility = View.GONE
            binding.layoutTerakhir.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
