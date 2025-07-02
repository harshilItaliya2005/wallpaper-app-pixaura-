package com.example.pixaura.utils

import com.example.pixaura.ui.view.HomeScreen.HomeUiStateData
import com.example.pixaura.ui.view.categoryScreen.CategoryUiStateData
import com.example.pixaura.ui.view.fragments.wallpaperView.WallpaperUiStateData

sealed class UIState<out T> {
    object Loading : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
    data class Error(val message: String) : UIState<Nothing>()
}

typealias HomeScreenState = UIState<HomeUiStateData>
typealias CategoryScreenState = UIState<CategoryUiStateData>
typealias WallpaperScreenState = UIState<WallpaperUiStateData>
