package com.example.pixaura.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pixaura.R
import com.example.pixaura.data.model.apiData.Result
import com.example.pixaura.databinding.ItemWallpaperBinding

class HomeScreenFragmentAdapter(  private val onItemClick: (Result) -> Unit) :
    PagingDataAdapter<Result, HomeScreenFragmentAdapter.PhotoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemWallpaperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position)
        if (photo != null) {
            holder.bind(photo, onItemClick)
        }
    }

    class PhotoViewHolder(private val binding: ItemWallpaperBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Result, onItemClick: (Result) -> Unit) {
            Glide.with(binding.root.context)
                .load(photo.urls.regular)
                .into(binding.imageView)
            Log.d("============================", "Binding photo: ${photo.id}")

            val anim = AnimationUtils.loadAnimation(binding.root.context, R.anim.item_fade_scale)
            binding.root.startAnimation(anim)
            binding.imgCard.setOnClickListener {
                onItemClick(photo)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }
}
