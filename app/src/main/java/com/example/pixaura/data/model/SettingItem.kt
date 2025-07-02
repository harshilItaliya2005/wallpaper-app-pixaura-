package com.example.pixaura.data.model

sealed class SettingItem {
    data class ToggleItem(val title: String, val isChecked: Boolean, val onToggle: (Boolean) -> Unit) : SettingItem()
    data class NavigationItem(val title: String, val onClick: () -> Unit) : SettingItem()
    data class SectionHeader(val title: String) : SettingItem()
}
