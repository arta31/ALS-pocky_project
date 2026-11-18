package com.example.als_pocky.beranda1

// Progres 6 (P11): data 1 kotak menu di grid Beranda
data class MenuBeranda(
    val ikon: Int,        // R.drawable.xxx
    val judul: String,
    val keterangan: String,
    val bgIkon: Int,      // latar kotak ikon (R.drawable.bg_icon_tile / _gold)
    val warnaIkon: Int    // R.color.xxx
)