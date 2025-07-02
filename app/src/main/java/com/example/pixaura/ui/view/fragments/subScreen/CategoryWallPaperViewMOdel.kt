package com.example.pixaura.ui.view.fragments.subScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.pixaura.data.repository.WallpaperRepository
import com.example.pixaura.utils.pagingSource.WallpaperPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CategoryWallPaperViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : ViewModel() {

    private val currentQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val wallpapers = currentQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(pageSize = 30, enablePlaceholders = false),
            pagingSourceFactory = { WallpaperPagingSource(repository, query) }
        ).flow
    }.cachedIn(viewModelScope)

    fun search(query: String) {
        currentQuery.value = query
    }
}
