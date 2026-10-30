package com.example.als_pocky.laporan5

import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.core.content.ContextCompat
import com.example.als_pocky.R
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Data yang dimasukkan ke laporan
data class DataLaporan(
    val namaPetani: String,
    val tanggal: String,
    val berat: Double,
    val harga: Double,
    val total: Double
)

// 💡 BARU: object = singleton, cukup panggil LaporanPdf.buat(...) tanpa membuat objek
object LaporanPdf {

    private val localeId = Locale("id", "ID")
    private fun rupiah(nilai: Double) = "Rp " + NumberFormat.getInstance(localeId).format(nilai.toLong())

    // Membuat file PDF di cache/laporan/ lalu mengembalikan File-nya
    fun buat(context: Context, data: DataLaporan): File {
        val navy = ContextCompat.getColor(context, R.color.primary_500)
        val emas = ContextCompat.getColor(context, R.color.accent_gold)
        val merah = ContextCompat.getColor(context, R.color.danger_500)
        val abu = ContextCompat.getColor(context, R.color.neutral_500)
        val hitam = ContextCompat.getColor(context, R.color.neutral_900)
        val garis = ContextCompat.getColor(context, R.color.neutral_200)

        // 💡 BARU: PdfDocument -> 1 halaman ukuran A4 (595 x 842 point)
        val pdf = PdfDocument()
        val halaman = pdf.startPage(PdfDocument.PageInfo.Builder(595, 842, 1).create())
        // 💡 BARU: Canvas = "kertas" tempat menggambar teks & bentuk, Paint = "kuas"-nya
        val canvas = halaman.canvas
        val kuas = Paint(Paint.ANTI_ALIAS_FLAG)
        val kiri = 40f
        val kanan = 555f

        // ---- Kop navy ----
        kuas.color = navy
        canvas.drawRect(0f, 0f, 595f, 120f, kuas)
        kuas.color = ContextCompat.getColor(context, R.color.neutral_0)
        kuas.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        kuas.textSize = 28f
        canvas.drawText("SEMAWIT", kiri, 62f, kuas)
        kuas.color = emas
        kuas.typeface = Typeface.DEFAULT
        kuas.textSize = 13f
        canvas.drawText("Laporan Bagi Hasil Panen Sawit", kiri, 88f, kuas)

        // ---- Identitas ----
        var y = 165f
        fun baris(label: String, nilai: String, tebal: Boolean = false) {
            kuas.typeface = Typeface.DEFAULT
            kuas.textSize = 12f
            kuas.color = abu
            kuas.textAlign = Paint.Align.LEFT
            canvas.drawText(label, kiri, y, kuas)
            kuas.color = hitam
            kuas.typeface = if (tebal) Typeface.create(Typeface.DEFAULT, Typeface.BOLD) else Typeface.DEFAULT
            kuas.textSize = if (tebal) 14f else 12f
            kuas.textAlign = Paint.Align.RIGHT
            canvas.drawText(nilai, kanan, y, kuas)
            kuas.textAlign = Paint.Align.LEFT
            y += 26f
        }
        fun judul(teks: String) {
            y += 14f
            kuas.color = navy
            kuas.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            kuas.textSize = 14f
            canvas.drawText(teks, kiri, y, kuas)
            y += 10f
            kuas.color = garis
            kuas.strokeWidth = 1f
            canvas.drawLine(kiri, y, kanan, y, kuas)
            y += 24f
        }

        baris("Petani", data.namaPetani)
        baris("Tanggal panen", data.tanggal)

        // ---- Rincian panen ----
        judul("Rincian Panen")
        baris("Berat panen", NumberFormat.getInstance(localeId).format(data.berat) + " Kg")
        baris("Harga buah per Kg", rupiah(data.harga))
        baris("Total pendapatan", rupiah(data.total), tebal = true)

        // ---- Pembagian dana ----
        judul("Pembagian Dana")
        val pos = listOf(
            Triple("Rumah Tangga (75%)", 0.75, navy),
            Triple("Pupuk (15%)", 0.15, emas),
            Triple("Racun (10%)", 0.10, merah)
        )
        // Bar bertumpuk yang menggambarkan proporsi 75 / 15 / 10
        var x = kiri
        val lebarBar = kanan - kiri
        for ((_, persen, warna) in pos) {
            kuas.color = warna
            canvas.drawRect(RectF(x, y - 6f, x + (lebarBar * persen).toFloat(), y + 10f), kuas)
            x += (lebarBar * persen).toFloat()
        }
        y += 40f
        for ((nama, persen, warna) in pos) {
            kuas.color = warna
            canvas.drawCircle(kiri + 5f, y - 4f, 5f, kuas)
            kuas.color = hitam
            kuas.typeface = Typeface.DEFAULT
            kuas.textSize = 12f
            canvas.drawText(nama, kiri + 18f, y, kuas)
            kuas.textAlign = Paint.Align.RIGHT
            kuas.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(rupiah(data.total * persen), kanan, y, kuas)
            kuas.textAlign = Paint.Align.LEFT
            y += 26f
        }

        // ---- Footer ----
        kuas.color = abu
        kuas.typeface = Typeface.DEFAULT
        kuas.textSize = 10f
        val dibuat = SimpleDateFormat("dd MMM yyyy, HH.mm", localeId).format(Date())
        canvas.drawText("Dibuat otomatis oleh aplikasi SEMAWIT • $dibuat", kiri, 810f, kuas)

        pdf.finishPage(halaman)

        // Simpan ke folder cache/laporan/ (folder ini didaftarkan di res/xml/file_paths.xml)
        val folder = File(context.cacheDir, "laporan")
        folder.mkdirs()
        val file = File(folder, "Laporan_Panen_${System.currentTimeMillis()}.pdf")
        FileOutputStream(file).use { pdf.writeTo(it) }
        pdf.close()
        return file
    }
}
