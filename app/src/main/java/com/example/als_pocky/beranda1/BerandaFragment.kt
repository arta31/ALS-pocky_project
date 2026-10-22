package com.example.als_pocky.beranda1

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
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

        // "+ Input Panen" -> pindah ke menu Panen di bottom nav
        binding.btnInputTransaksi.setOnClickListener {
            (requireActivity() as BaseActivity).bukaMenu(R.id.nav_panen)
        }

        // "Web Harga" -> tetap membuka Activity WebView
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
        // Tampilkan 1 transaksi terbaru saja, sisanya ada di RiwayatFragment
        val riwayat = RiwayatFragment.teksRiwayat
        if (riwayat.isNotEmpty()) {
            binding.tvIsiRiwayat.gravity = Gravity.START
            binding.tvIsiRiwayat.text = riwayat.split("\n\n").first()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
