package com.example.als_pocky.beranda1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.als_pocky.databinding.ItemMenuBerandaBinding

// Progres 6 (P11): Adapter = penghubung data (menuList) dengan tampilan item (item_menu_beranda.xml)
class MenuBerandaAdapter(
    private val menuList: List<MenuBeranda>,
    private val onItemClick: (MenuBeranda) -> Unit
) : RecyclerView.Adapter<MenuBerandaAdapter.MenuViewHolder>() {

    // ViewHolder = pemegang view 1 item, dipakai ulang (recycle) saat scroll
    inner class MenuViewHolder(val binding: ItemMenuBerandaBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Dipanggil saat butuh kotak baru -> inflate layout item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuBerandaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MenuViewHolder(binding)
    }

    // Dipanggil untuk mengisi data ke kotak di posisi tertentu
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = menuList[position]
        with(holder.binding) {
            ivIkon.setImageResource(item.ikon)
            ivIkon.setBackgroundResource(item.bgIkon)
            // 💡 BARU: imageTintList = versi kode dari app:tint di XML
            ivIkon.imageTintList = ContextCompat.getColorStateList(root.context, item.warnaIkon)
            tvJudul.text = item.judul
            tvKeterangan.text = item.keterangan

            root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun getItemCount(): Int = menuList.size
}