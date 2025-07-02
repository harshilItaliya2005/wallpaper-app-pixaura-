package com.example.pixaura.ui.view.fragments.topBars

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.pixaura.R
import com.example.pixaura.databinding.FragmentHomeBinding
import com.example.pixaura.databinding.FragmentHomeTopBarBinding


class HomeTopBarFragment : Fragment() {
    private var _binding: FragmentHomeTopBarBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        val whiteColor = ContextCompat.getColor(requireContext(), R.color.white)
        binding.materialTopAppBar.menu.forEach { item ->
            item.icon?.setTint(whiteColor)
        }
    }



}