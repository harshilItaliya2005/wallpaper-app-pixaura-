package com.example.pixaura.ui.view.fragments.wallpaperView

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.pixaura.DownloadOkReceiver
import com.example.pixaura.R
import com.example.pixaura.databinding.FragmentWallpaperViewBinding
import com.example.pixaura.utils.UIState
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@Suppress("DEPRECATION")
@AndroidEntryPoint
class WallpaperViewFragment : Fragment() {

    private var _binding: FragmentWallpaperViewBinding? = null
    private val binding get() = _binding!!
    private val args: WallpaperViewFragmentArgs by navArgs()
    private val viewModel: WallPaperViewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWallpaperViewBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val wallpaperId = args.id
        val localUri = args.localUri

        if (!localUri.isNullOrEmpty()) {
            // ---- Load LOCAL image ----
            binding.progressBar2.visibility = View.GONE
            val uri = Uri.parse(localUri)

            Glide.with(requireContext())
                .load(uri)
                .into(binding.wallpaperImage)

            binding.setWallpaper.setOnClickListener {
                showSetWallpaperBottomSheet(uri)
            }

            binding.share.setOnClickListener {
                Glide.with(requireContext())
                    .asBitmap()
                    .load(uri)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            shareImage(resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
            }

            // Hide download button for local image
            binding.download.visibility = View.GONE

        } else if (wallpaperId.isNotEmpty()) {
            // ---- Load REMOTE image from API ----
            viewModel.fetchWallpaperById(wallpaperId)

            lifecycleScope.launch {
                viewModel.wallpaperView.collect { state ->
                    when (state) {
                        is UIState.Loading -> {
                            binding.progressBar2.visibility = View.VISIBLE
                        }

                        is UIState.Error -> {
                            binding.progressBar2.visibility = View.GONE
                            Log.e("WallpaperView", "Error: ${state.message}")
                        }

                        is UIState.Success -> {
                            val wallpaper = state.data.data
                            Glide.with(requireContext())
                                .load(wallpaper.urls.regular)
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(
                                        e: GlideException?, model: Any?,
                                        target: Target<Drawable?>, isFirstResource: Boolean
                                    ): Boolean {
                                        binding.progressBar2.visibility = View.GONE
                                        return false
                                    }

                                    override fun onResourceReady(
                                        resource: Drawable, model: Any,
                                        target: Target<Drawable?>?, dataSource: DataSource,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        binding.progressBar2.visibility = View.GONE
                                        return false
                                    }
                                })
                                .into(binding.wallpaperImage)

                            val url = wallpaper.urls.regular

                            binding.download.setOnClickListener {
                                downloadImage(url)
                            }

                            binding.share.setOnClickListener {
                                Glide.with(requireContext())
                                    .asBitmap()
                                    .load(url)
                                    .into(object : CustomTarget<Bitmap>() {
                                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                            shareImage(resource)
                                        }

                                        override fun onLoadCleared(placeholder: Drawable?) {}
                                        override fun onLoadFailed(errorDrawable: Drawable?) {
                                            Toast.makeText(requireContext(), "Failed to load image for sharing.", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                            }

                            binding.setWallpaper.setOnClickListener {
                                showSetWallpaperBottomSheet(Uri.parse(url))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun shareImage(bitmap: Bitmap) {
        try {
            val file = File(requireContext().cacheDir, "shared_image.jpg")
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()

            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                file
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(Intent.createChooser(intent, "Share Wallpaper Via"))

        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Failed to share image: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
            Log.e("ShareImage", "Error: ", e)
        }
    }

    private fun downloadImage(url: String) {
        Glide.with(requireContext())
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    if (isAdded) saveImageToGallery(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    Toast.makeText(requireContext(), "Failed to load image.", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        val filename = "Wallpaper_${System.currentTimeMillis()}.jpg"
        val fos: OutputStream?
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + "/PixAura"
                    )
                }
                val imageUri = requireContext().contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
                )
                fos = imageUri?.let { requireContext().contentResolver.openOutputStream(it) }
            } else {
                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/PixAura")
                if (!imagesDir.exists()) imagesDir.mkdirs()
                val image = File(imagesDir, filename)
                fos = FileOutputStream(image)
                try {
                    MediaScannerConnection.scanFile(
                        requireContext(),
                        arrayOf(image.toString()),
                        null,
                        null
                    )
                } catch (e: SecurityException) {
                    Log.e("GalleryScan", "SecurityException: ${e.message}")
                }
            }

            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                Toast.makeText(requireContext(), "Image saved to Gallery", Toast.LENGTH_SHORT)
                    .show()
                showDownloadNotification(filename)
            }

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Save failed: ${e.message}", Toast.LENGTH_LONG).show()
            Log.e("DownloadImage", "Error saving image", e)
        }
    }

    private fun showDownloadNotification(fileName: String) {
        val channelId = "download_channel"
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Download Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifies when wallpaper is downloaded"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val okIntent = Intent(requireContext(), DownloadOkReceiver::class.java)
        val okPendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            okIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.icon_logo)
            .setContentTitle("Download Complete")
            .setContentText("$fileName saved to Gallery")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.ic_check, "OK", okPendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            notificationManager.notify(1, notification)
        } catch (e: SecurityException) {
            Log.e("Notification", "Missing POST_NOTIFICATIONS permission", e)
        }
    }
    private fun showSetWallpaperBottomSheet(imageUri: Uri) {
        val bottomSheet = WallpaperBottomSheetFragment()
        bottomSheet.setOnOptionSelectedListener { option ->
            Glide.with(requireContext())
                .asBitmap()
                .load(imageUri)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val wallpaperManager = WallpaperManager.getInstance(requireContext())
                        try {
                            when (option) {
                                WallpaperBottomSheetFragment.WallpaperOption.HOME -> {
                                    wallpaperManager.setBitmap(resource, null, true, WallpaperManager.FLAG_SYSTEM)
                                    Toast.makeText(requireContext(), "Home screen wallpaper set", Toast.LENGTH_SHORT).show()
                                }
                                WallpaperBottomSheetFragment.WallpaperOption.LOCK -> {
                                    wallpaperManager.setBitmap(resource, null, true, WallpaperManager.FLAG_LOCK)
                                    Toast.makeText(requireContext(), "Lock screen wallpaper set", Toast.LENGTH_SHORT).show()
                                }
                                WallpaperBottomSheetFragment.WallpaperOption.BOTH -> {
                                    wallpaperManager.setBitmap(resource, null, true, WallpaperManager.FLAG_SYSTEM)
                                    wallpaperManager.setBitmap(resource, null, true, WallpaperManager.FLAG_LOCK)
                                    Toast.makeText(requireContext(), "Wallpaper set for both screens", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), "Failed to set wallpaper", Toast.LENGTH_SHORT).show()
                            Log.e("SetWallpaper", "Error: ${e.message}")
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }

        bottomSheet.show(parentFragmentManager, "WallpaperOptionSheet")
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
