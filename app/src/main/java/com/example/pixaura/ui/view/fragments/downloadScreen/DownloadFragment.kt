package com.example.pixaura.ui.view.fragments.downloadScreen

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pixaura.R
import com.example.pixaura.databinding.FragmentDownloadBinding
import com.example.pixaura.ui.adapter.DownloadAdapter
import java.io.File


class DownloadFragment : Fragment() {
    private var _binding: FragmentDownloadBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDownloadBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerDownloads.layoutManager = GridLayoutManager(requireContext(), 3)

        val downloads = loadDownloadedImages()
        binding.recyclerDownloads.adapter = DownloadAdapter(downloads)
    }
    private fun loadDownloadedImages(): List<Uri> {
        val downloadsDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "PixAura"
        )

        if (!downloadsDir.exists()) return emptyList()

        return downloadsDir.listFiles()?.map { Uri.fromFile(it) } ?: emptyList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}