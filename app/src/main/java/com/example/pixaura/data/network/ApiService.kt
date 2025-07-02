package com.example.pixaura.data.network

import com.example.pixaura.data.model.apiData.Result
import com.example.pixaura.data.model.apiData.WallpaperResponse
import com.example.pixaura.utils.Contacts.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10,
        @Query("client_id") clientId: String = API_KEY
    ): WallpaperResponse

    @GET("photos/{id}")
    suspend fun getWallPaperById(
        @retrofit2.http.Path("id") id: String,
        @Query("client_id") clientId: String = API_KEY
    ): Result

}

