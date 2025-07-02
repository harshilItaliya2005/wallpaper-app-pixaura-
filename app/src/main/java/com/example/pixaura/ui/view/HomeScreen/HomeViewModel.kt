package com.example.pixaura.ui.view.HomeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pixaura.data.model.apiData.Result
import com.example.pixaura.data.repository.WallpaperRepository
import com.example.pixaura.utils.HomeScreenState
import com.example.pixaura.utils.UIState
import com.example.pixaura.utils.pagingSource.WallpaperPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : ViewModel() {

    private val currentQuery = MutableStateFlow("wallpaper")

    @OptIn(ExperimentalCoroutinesApi::class)
    val wallpapers: Flow<PagingData<Result>> = currentQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(pageSize = 30, enablePlaceholders = false),
            pagingSourceFactory = { WallpaperPagingSource(repository, query) }
        ).flow
    }.cachedIn(viewModelScope)

    fun refresh() {
        currentQuery.value = currentQuery.value
    }
}

