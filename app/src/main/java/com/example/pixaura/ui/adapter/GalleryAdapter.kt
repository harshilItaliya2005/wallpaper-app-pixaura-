package com.example.pixaura.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pixaura.R
import com.example.pixaura.databinding.GalleryItemBinding

class GalleryAdapter(
    private val imageUris: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    inner class GalleryViewHolder(val binding: GalleryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = GalleryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(binding)
    }

    override fun getItemCount(): Int = imageUris.size

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val uri = imageUris[position].toUri()
        Glide.with(holder.itemView.context)
            .load(uri)
            .centerCrop()
            .into(holder.binding.imgGallery)

        holder.itemView.setOnClickListener {
            onItemClick(uri.toString())
        }
    }
}

