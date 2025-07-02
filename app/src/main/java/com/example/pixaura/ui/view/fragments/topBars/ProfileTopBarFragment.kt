package com.example.pixaura.ui.view.fragments.topBars

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.pixaura.R
import com.example.pixaura.databinding.FragmentProfileTopBarBinding
import com.example.pixaura.databinding.FragmentSearchTopBarBinding


class ProfileTopBarFragment : Fragment() {

    private var _binding: FragmentProfileTopBarBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileTopBarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        navController = findNavController()

        binding.materialTopAppBar.post {
            val menu = binding.materialTopAppBar.menu
            menu.findItem(R.id.action_search)?.isVisible = false
            menu.findItem(R.id.action_settings)?.isVisible = false
        }


    }


}