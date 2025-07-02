package com.example.pixaura.ui.view.fragments.settingScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pixaura.data.model.SettingItem
import com.example.pixaura.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _navigateTo = MutableLiveData<Event<String>>()
    val navigateTo: LiveData<Event<String>> = _navigateTo

    private val _settings = MutableLiveData<List<SettingItem>>()
    val settings: LiveData<List<SettingItem>> = _settings

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _settings.value = listOf(
            SettingItem.SectionHeader("Notifications"),
            SettingItem.ToggleItem("Enable Notifications", true) { enabled ->
                // Toggle logic (e.g., save to DataStore or Preferences)
            },
            SettingItem.SectionHeader("More Options"),
            SettingItem.NavigationItem("My Profile") {
                _navigateTo.value = Event("profile")
            },
            SettingItem.NavigationItem("Privacy Policy") {
                _navigateTo.value = Event("privacy")
            },
            SettingItem.NavigationItem("Terms & Conditions") {
                _navigateTo.value = Event("terms")
            }
        )
    }
}
