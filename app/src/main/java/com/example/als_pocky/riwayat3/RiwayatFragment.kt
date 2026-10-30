package com.example.als_pocky.riwayat3

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.als_pocky.R
import com.example.als_pocky.databinding.FragmentRiwayatBinding
import com.example.als_pocky.databinding.ItemCalendarDayBinding
import com.example.als_pocky.databinding.ItemRiwayatBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class RiwayatFragment : Fragment() {

    companion object {
        // Data riwayat bersama (dibaca Beranda & Riwayat, ditulis Panen)
        var teksRiwayat = ""

        // Simpan permanen ke SharedPreferences agar tidak hilang saat aplikasi ditutup
        fun simpan(context: Context) {
            context.getSharedPreferences("riwayat_pref", Context.MODE_PRIVATE)
                .edit().putString("teksRiwayat", teksRiwayat).apply()
        }

        // Dipanggil sekali saat BaseActivity dibuka
        fun muat(context: Context) {
            teksRiwayat = context.getSharedPreferences("riwayat_pref", Context.MODE_PRIVATE)
                .getString("teksRiwayat", "") ?: ""
        }

        // Pecah teksRiwayat menjadi daftar Transaksi (dipakai Riwayat & Dana)
        fun bacaTransaksi(): List<Transaksi> {
            if (teksRiwayat.isEmpty()) return emptyList()
            // Format yang sama dengan saat disimpan di PanenFragment
            val formatSimpan = SimpleDateFormat("dd MMM yyyy, HH.mm", Locale("id", "ID"))
            return teksRiwayat.split("\n\n").map { baris ->
                val bagian = baris.split("  •  ")
                val nominal = bagian.getOrElse(1) { "" }
                val tanggalTeks = bagian.getOrElse(2) { "" }
                val tanggal = try {
                    val date = formatSimpan.parse(tanggalTeks)
                    date?.let { Instant.ofEpochMilli(it.time).atZone(ZoneId.systemDefault()).toLocalDate() }
                } catch (e: Exception) {
                    null // transaksi lama tanpa tanggal
                }
                // "Rp 250.000" -> 250000 (ambil angkanya saja)
                val nilai = nominal.filter { it.isDigit() }.toLongOrNull() ?: 0L
                Transaksi(bagian[0], nominal, tanggalTeks, tanggal, nilai)
            }
        }
    }

    // 1 transaksi hasil pecahan teks "Panen 100 Kg  •  Rp 250.000  •  30 Okt 2026, 07.30"
    data class Transaksi(
        val judul: String,
        val nominal: String,
        val tanggalTeks: String,
        val tanggal: LocalDate?,
        val nilai: Long
    )


    private var _binding: FragmentRiwayatBinding? = null
    private val binding get() = _binding!!

    private val localeId = Locale("id", "ID")
    private var daftarTransaksi = listOf<Transaksi>()
    private var tanggalPanen = setOf<LocalDate>()   // tanggal yang diberi titik kuning
    private var tanggalDipilih: LocalDate? = null    // null = tampilkan semua

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Riwayat transaksi"
            setDisplayShowTitleEnabled(true)
        }

        daftarTransaksi = bacaTransaksi()
        tanggalPanen = daftarTransaksi.mapNotNull { it.tanggal }.toSet()

        siapkanKalender()
        tampilkanDaftar()

        binding.tvTampilkanSemua.setOnClickListener { pilihTanggal(null) }
    }

    // ================= KALENDER PANEN =================

    // 💡 BARU: ViewContainer = "pemegang view" untuk 1 sel tanggal.
    // View sel sudah di-inflate oleh library, jadi kita cukup bind(view), bukan inflate().
    inner class DayViewContainer(view: View) : ViewContainer(view) {
        val sel = ItemCalendarDayBinding.bind(view)
        lateinit var hari: CalendarDay

        init {
            view.setOnClickListener {
                // Hanya tanggal milik bulan yang sedang tampil yang bisa dipilih
                if (hari.position == DayPosition.MonthDate) {
                    pilihTanggal(if (tanggalDipilih == hari.date) null else hari.date)
                }
            }
        }
    }

    private fun siapkanKalender() {
        // Nama hari (Sen, Sel, Rab, ...) mengikuti urutan hari pertama dari Locale
        val urutanHari = daysOfWeek()
        binding.layoutNamaHari.removeAllViews()
        for (hari in urutanHari) {
            val tv = TextView(requireContext())
            tv.layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            tv.gravity = Gravity.CENTER
            tv.text = hari.getDisplayName(TextStyle.SHORT, localeId)
            tv.textSize = 12f
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.neutral_500))
            binding.layoutNamaHari.addView(tv)
        }

        // 💡 BARU: object : MonthDayBinder<...> { } = anonymous object yang mengisi
        // 2 fungsi wajib: create() (buat container) & bind() (isi data ke sel)
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.hari = data
                val tv = container.sel.tvTanggal
                tv.text = data.date.dayOfMonth.toString()

                if (data.position == DayPosition.MonthDate) {
                    tv.alpha = 1f
                    container.sel.dotPanen.visibility =
                        if (data.date in tanggalPanen) View.VISIBLE else View.INVISIBLE
                    when (data.date) {
                        tanggalDipilih -> {
                            tv.setBackgroundResource(R.drawable.bg_day_selected)
                            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.neutral_0))
                        }
                        LocalDate.now() -> {
                            tv.setBackgroundResource(R.drawable.bg_day_today)
                            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_500))
                        }
                        else -> {
                            tv.background = null
                            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.neutral_900))
                        }
                    }
                } else {
                    // Tanggal milik bulan sebelum/sesudah: dibuat pudar
                    tv.alpha = 0.3f
                    tv.background = null
                    tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.neutral_500))
                    container.sel.dotPanen.visibility = View.INVISIBLE
                }
            }
        }

        // 💡 BARU: java.time (YearMonth) untuk menentukan rentang bulan kalender
        val bulanIni = YearMonth.now()
        binding.calendarView.setup(bulanIni.minusMonths(12), bulanIni.plusMonths(12), urutanHari.first())
        binding.calendarView.scrollToMonth(bulanIni)

        // Judul bulan ikut berubah saat kalender digeser
        val formatBulan = DateTimeFormatter.ofPattern("MMMM yyyy", localeId)
        binding.tvBulan.text = bulanIni.format(formatBulan)
        binding.calendarView.monthScrollListener = { bulan ->
            binding.tvBulan.text = bulan.yearMonth.format(formatBulan)
        }

        binding.btnBulanSebelum.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.previousMonth)
            }
        }
        binding.btnBulanBerikut.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.nextMonth)
            }
        }
    }

    // Pilih / batal pilih tanggal, lalu gambar ulang sel yang berubah saja
    private fun pilihTanggal(tanggal: LocalDate?) {
        val lama = tanggalDipilih
        tanggalDipilih = tanggal
        lama?.let { binding.calendarView.notifyDateChanged(it) }
        tanggal?.let { binding.calendarView.notifyDateChanged(it) }
        tampilkanDaftar()
    }

    // ================= DAFTAR TRANSAKSI =================

    private fun tampilkanDaftar() {
        val tampil = if (tanggalDipilih == null) daftarTransaksi
                     else daftarTransaksi.filter { it.tanggal == tanggalDipilih }

        binding.tvFilter.text = tanggalDipilih?.let {
            "Panen " + it.format(DateTimeFormatter.ofPattern("d MMMM yyyy", localeId))
        } ?: "Semua transaksi"
        binding.tvTampilkanSemua.visibility = if (tanggalDipilih == null) View.GONE else View.VISIBLE

        binding.containerRiwayat.removeAllViews()
        if (tampil.isEmpty()) {
            binding.cardKosong.visibility = View.VISIBLE
            binding.cardDaftar.visibility = View.GONE
            if (tanggalDipilih == null) {
                binding.tvKosongJudul.text = "Belum ada transaksi"
                binding.tvKosongIsi.text = "Tekan tombol + di bawah untuk\nmencatat panen pertama."
            } else {
                binding.tvKosongJudul.text = "Tidak ada panen"
                binding.tvKosongIsi.text = "Tidak ada transaksi di tanggal ini."
            }
            return
        }

        binding.cardKosong.visibility = View.GONE
        binding.cardDaftar.visibility = View.VISIBLE
        tampil.forEachIndexed { index, t ->
            val item = ItemRiwayatBinding.inflate(layoutInflater, binding.containerRiwayat, false)
            item.tvJudul.text = t.judul
            item.tvNominal.text = "+" + t.nominal
            item.tvTanggal.text = t.tanggalTeks.ifEmpty { "Bagi hasil otomatis" }
            item.divider.visibility = if (index == tampil.lastIndex) View.GONE else View.VISIBLE
            binding.containerRiwayat.addView(item.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
