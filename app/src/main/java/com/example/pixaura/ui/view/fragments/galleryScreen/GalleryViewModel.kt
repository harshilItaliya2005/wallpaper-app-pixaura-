package com.example.pixaura.ui.view.fragments.galleryScreen

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    private val _images = MutableStateFlow<List<Uri>>(emptyList())
    val images: StateFlow<List<Uri>> = _images

    init {
        loadImagesFromGallery()
    }

    private fun loadImagesFromGallery() {
        viewModelScope.launch(Dispatchers.IO) {
            val imageList = mutableListOf<Uri>()
            val projection = arrayOf(MediaStore.Images.Media._ID)
            val uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val cursor = application.contentResolver.query(uriExternal, projection, null, null, "${MediaStore.Images.Media.DATE_ADDED} DESC")

            cursor?.use {
                val idIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (it.moveToNext()) {
                    val id = it.getLong(idIndex)
                    val contentUri = ContentUris.withAppendedId(uriExternal, id)
                    imageList.add(contentUri)
                }
            }
            _images.value = imageList
        }
    }
}
