package com.example.pixaura.ui.adapter

import android.R.attr.text
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pixaura.data.model.WallpaperCategories
import com.example.pixaura.databinding.ItemCategoriesBinding

class CategoriesAdapter(
    private var categories: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private val binding: ItemCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: String) {
            binding.categoryText.text = category
            binding.root.setOnClickListener { onClick(category) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newCategories: List<String>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}
