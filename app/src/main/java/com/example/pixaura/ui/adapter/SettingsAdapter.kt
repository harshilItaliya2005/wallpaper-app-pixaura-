package com.example.pixaura.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pixaura.data.model.SettingItem
import com.example.pixaura.databinding.ItemSettingHeaderBinding
import com.example.pixaura.databinding.ItemSettingNavBinding
import com.example.pixaura.databinding.ItemSettingToggleBinding

class SettingsAdapter(private val items: List<SettingItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_TOGGLE = 0
        private const val TYPE_NAV = 1
        private const val TYPE_HEADER = 2
    }

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is SettingItem.ToggleItem -> TYPE_TOGGLE
        is SettingItem.NavigationItem -> TYPE_NAV
        is SettingItem.SectionHeader -> TYPE_HEADER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_TOGGLE -> ToggleViewHolder(ItemSettingToggleBinding.inflate(inflater, parent, false))
            TYPE_NAV -> NavViewHolder(ItemSettingNavBinding.inflate(inflater, parent, false))
            TYPE_HEADER -> HeaderViewHolder(ItemSettingHeaderBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is SettingItem.ToggleItem -> (holder as ToggleViewHolder).bind(item)
            is SettingItem.NavigationItem -> (holder as NavViewHolder).bind(item)
            is SettingItem.SectionHeader -> (holder as HeaderViewHolder).bind(item)
        }
    }

    inner class ToggleViewHolder(private val binding: ItemSettingToggleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingItem.ToggleItem) {
            binding.title.text = item.title
            binding.toggleSwitch.isChecked = item.isChecked
            binding.toggleSwitch.setOnCheckedChangeListener { _, isChecked -> item.onToggle(isChecked) }
        }
    }

    inner class NavViewHolder(private val binding: ItemSettingNavBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingItem.NavigationItem) {
            binding.title.text = item.title
            binding.root.setOnClickListener { item.onClick() }
        }
    }

    inner class HeaderViewHolder(private val binding: ItemSettingHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingItem.SectionHeader) {
            binding.headerTitle.text = item.title
        }
    }
}
