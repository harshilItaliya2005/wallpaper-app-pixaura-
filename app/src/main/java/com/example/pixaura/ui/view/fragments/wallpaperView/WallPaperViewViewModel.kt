package com.example.pixaura.ui.view.fragments.wallpaperView

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixaura.data.repository.WallpaperRepository
import com.example.pixaura.utils.UIState
import com.example.pixaura.utils.WallpaperScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallPaperViewViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : ViewModel() {


    private val _wallPaperView =
        MutableStateFlow<UIState<WallpaperUiStateData>>(UIState.Loading)
    val wallpaperView = _wallPaperView.asStateFlow()

    fun fetchWallpaperById(id: String) {
        viewModelScope.launch {
            try {
                Log.d("fetchWallpaperById", "Fetching started")
                _wallPaperView.value = UIState.Loading
                val result = repository.getWallpaperById(id)
                Log.d("fetchWallpaperById", "Success: ${result.id}")
                _wallPaperView.value = UIState.Success(WallpaperUiStateData(result))
            } catch (e: Exception) {
                Log.e("fetchWallpaperById", "Error: ${e.message}", e)
                _wallPaperView.value = UIState.Error(e.message ?: "Unknown error")
            }
        }
    }


}