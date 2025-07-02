package com.example.pixaura.utils.pagingSource

import android.R.attr.data
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pixaura.data.model.apiData.Result
import com.example.pixaura.data.repository.WallpaperRepository
import retrofit2.HttpException
import retrofit2.http.Query
import java.net.HttpRetryException

class WallpaperPagingSource(
    private val repository: WallpaperRepository,
    private val query: String
) : PagingSource<Int, Result>() {

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val currentPage = params.key ?: 1
            Log.d("Paging", "Loading page: $currentPage")
            val photos = repository.getSearchWallpapers(query = query, page = currentPage)
            val data = photos.results.shuffled()
            Log.d("Paging", "Loaded ${data.size} items")

            LoadResult.Page(
                data = data,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (data.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            Log.e("Paging", "Error: ${e.message}")
            LoadResult.Error(e)
        }
    }

}
