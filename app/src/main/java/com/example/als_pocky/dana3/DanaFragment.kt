package com.example.als_pocky.dana3

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.als_pocky.R
import com.example.als_pocky.databinding.FragmentDanaBinding
import com.example.als_pocky.riwayat3.RiwayatFragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

// Fitur baru: Grafik Dana (MPAndroidChart)
class DanaFragment : Fragment() {

    private var _binding: FragmentDanaBinding? = null
    private val binding get() = _binding!!

    private val localeId = Locale("id", "ID")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDanaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Dana"
            setDisplayShowTitleEnabled(true)
        }

        // Data diambil dari riwayat panen yang sudah tersimpan permanen
        val transaksi = RiwayatFragment.bacaTransaksi()
        if (transaksi.isEmpty()) {
            binding.cardKosong.visibility = View.VISIBLE
            binding.layoutGrafik.visibility = View.GONE
            return
        }
        binding.cardKosong.visibility = View.GONE
        binding.layoutGrafik.visibility = View.VISIBLE

        val total = transaksi.sumOf { it.nilai }
        binding.tvTotal.text = rupiah(total)
        binding.tvJumlahPanen.text = "dari ${transaksi.size} transaksi panen"
        binding.tvRumahTangga.text = rupiah((total * 0.75).toLong())
        binding.tvPupuk.text = rupiah((total * 0.15).toLong())
        binding.tvRacun.text = rupiah((total * 0.10).toLong())

        tampilkanPieChart(total)
        tampilkanBarChart(transaksi)
    }

    private fun warna(id: Int) = ContextCompat.getColor(requireContext(), id)

    // ===== PIE CHART: alokasi dana =====
    private fun tampilkanPieChart(total: Long) {
        // 💡 BARU: Entry -> DataSet -> Data -> Chart (alur data MPAndroidChart)
        val entries = listOf(
            PieEntry((total * 0.75).toFloat(), "Rumah Tangga"),
            PieEntry((total * 0.15).toFloat(), "Pupuk"),
            PieEntry((total * 0.10).toFloat(), "Racun")
        )
        val dataSet = PieDataSet(entries, "").apply {
            colors = listOf(warna(R.color.primary_500), warna(R.color.accent_gold), warna(R.color.danger_500))
            sliceSpace = 2f
            valueTextSize = 12f
            valueTextColor = Color.WHITE
        }

        binding.pieChart.apply {
            setUsePercentValues(true)                       // nilai ditampilkan sebagai persen
            data = PieData(dataSet).apply {
                setValueFormatter(PercentFormatter(binding.pieChart))
            }
            description.isEnabled = false
            legend.isEnabled = false                        // keterangan sudah ada di bawah grafik
            setDrawEntryLabels(false)
            isDrawHoleEnabled = true                        // bentuk donat
            holeRadius = 58f
            transparentCircleRadius = 62f
            setHoleColor(Color.TRANSPARENT)
            centerText = "Total\n${singkat(total)}"
            setCenterTextSize(15f)
            setCenterTextColor(warna(R.color.primary_800))
            animateY(900)                                   // animasi saat muncul
            invalidate()                                    // gambar ulang chart
        }
    }

    // ===== BAR CHART: pendapatan per panen =====
    private fun tampilkanBarChart(transaksi: List<RiwayatFragment.Transaksi>) {
        // teksRiwayat urut terbaru dulu -> ambil 7 terbaru lalu balik agar kiri = paling lama
        val terakhir = transaksi.take(7).reversed()
        val entries = terakhir.mapIndexed { i, t -> BarEntry(i.toFloat(), t.nilai.toFloat()) }
        val formatTanggal = DateTimeFormatter.ofPattern("d/M", localeId)
        val label = terakhir.map { it.tanggal?.format(formatTanggal) ?: "-" }

        // 💡 BARU: ValueFormatter = mengubah angka mentah menjadi teks (250000 -> "250rb")
        val formatSingkat = object : ValueFormatter() {
            override fun getFormattedValue(value: Float) = singkat(value.toLong())
        }

        val dataSet = BarDataSet(entries, "Pendapatan").apply {
            color = warna(R.color.primary_500)
            highLightColor = warna(R.color.accent_gold)     // bar yang disentuh jadi kuning
            valueTextSize = 10f
            valueTextColor = warna(R.color.neutral_700)
            valueFormatter = formatSingkat
        }

        binding.barChart.apply {
            data = BarData(dataSet).apply { barWidth = 0.55f }
            description.isEnabled = false
            legend.isEnabled = false
            setFitBars(true)
            setScaleEnabled(false)
            setPinchZoom(false)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                valueFormatter = IndexAxisValueFormatter(label)  // label sumbu X = tanggal panen
                textColor = warna(R.color.neutral_500)
            }
            axisLeft.apply {
                axisMinimum = 0f
                gridColor = warna(R.color.neutral_200)
                textColor = warna(R.color.neutral_500)
                valueFormatter = formatSingkat
            }
            axisRight.isEnabled = false
            animateY(900)
            invalidate()
        }
    }

    private fun rupiah(nilai: Long) = "Rp " + NumberFormat.getInstance(localeId).format(nilai)

    // 1.250.000 -> "1,3jt", 250.000 -> "250rb"
    private fun singkat(nilai: Long): String = when {
        nilai >= 1_000_000 -> String.format(localeId, "%.1fjt", nilai / 1_000_000.0).replace(",0jt", "jt")
        nilai >= 1_000 -> "${nilai / 1_000}rb"
        else -> nilai.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
