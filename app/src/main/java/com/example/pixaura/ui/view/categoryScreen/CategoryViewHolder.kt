package com.example.pixaura.ui.view.categoryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixaura.data.model.WallpaperCategories
import com.example.pixaura.utils.CategoryScreenState
import com.example.pixaura.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor() : ViewModel() {

    private val _categoryState = MutableStateFlow<CategoryScreenState>(UIState.Loading)
    val categoryState: StateFlow<CategoryScreenState> = _categoryState.asStateFlow()

    init {
        fetchCategory()
    }

    private fun fetchCategory() {
        viewModelScope.launch {
            try {
                val categories = WallpaperCategories.list
                _categoryState.value = UIState.Success(
                    CategoryUiStateData(categories)
                )
            } catch (e: Exception) {
                _categoryState.value = UIState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}
