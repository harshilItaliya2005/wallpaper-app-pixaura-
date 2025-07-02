package com.example.pixaura.data.repository

import android.R.attr.apiKey
import android.util.Log
import com.example.pixaura.data.model.apiData.Result
import com.example.pixaura.data.model.apiData.WallpaperResponse
import com.example.pixaura.data.network.ApiService
import com.example.pixaura.utils.Contacts.API_KEY
import retrofit2.http.Query
import javax.inject.Inject

class WallpaperRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getSearchWallpapers(query: String, page: Int): WallpaperResponse {
        return apiService.searchPhotos(query = query, page = page)
    }

    suspend fun getWallpaperById(id: String): Result {
        return apiService.getWallPaperById(id)
    }

}