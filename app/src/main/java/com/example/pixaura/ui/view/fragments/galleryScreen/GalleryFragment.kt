package com.example.pixaura.ui.view.fragments.galleryScreen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pixaura.databinding.FragmentUseOwnWallpaperBinding
import com.example.pixaura.ui.adapter.GalleryAdapter

class GalleryFragment : Fragment() {

    private var _binding: FragmentUseOwnWallpaperBinding? = null
    private val binding get() = _binding!!

    private val mediaList = mutableListOf<String>()
    private lateinit var galleryAdapter: GalleryAdapter

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            loadMedia()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUseOwnWallpaperBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up adapter with click listener
        galleryAdapter = GalleryAdapter(mediaList) { uriString ->
            val action = GalleryFragmentDirections
                .actionGalleryFragmentToWallpaperViewFragment(uriString)
            findNavController().navigate(action)
        }

        binding.galleryRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = galleryAdapter
        }

        checkAndRequestPermission()
    }

    private fun checkAndRequestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            loadMedia()
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }

    private fun loadMedia() {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val cursor = requireContext().contentResolver.query(
            uri,
            projection,
            null,
            null,
            sortOrder
        )

        mediaList.clear()

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val contentUri = Uri.withAppendedPath(uri, id.toString())
                mediaList.add(contentUri.toString())
            }
            galleryAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
