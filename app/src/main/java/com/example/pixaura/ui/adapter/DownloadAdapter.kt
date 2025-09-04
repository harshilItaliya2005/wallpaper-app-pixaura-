package com.example.pixaura.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pixaura.R
import com.example.pixaura.databinding.ItemDownloadBinding

class DownloadAdapter(private val images: List<Uri>) :
    RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder>() {

    class DownloadViewHolder(val binding: ItemDownloadBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        val binding = ItemDownloadBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DownloadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        Glide.with(holder.binding.imageDownload.context)
            .load(images[position])
            .into(holder.binding.imageDownload)
    }

    override fun getItemCount() = images.size
}