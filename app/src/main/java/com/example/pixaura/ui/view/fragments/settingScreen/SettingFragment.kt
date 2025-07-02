package com.example.pixaura.ui.view.fragments.settingScreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.pixaura.R
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pixaura.databinding.FragmentSettingBinding
import com.example.pixaura.ui.adapter.SettingsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()
        viewModel.settings.observe(viewLifecycleOwner) { items ->
            binding.settingsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.settingsRecyclerView.adapter = SettingsAdapter(items)
        }
        viewModel.navigateTo.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { destination ->
                when (destination) {
                    "profile" -> navController.navigate(R.id.action_settingFragment_to_myAccountFragment)
//                    "privacy" -> navController.navigate(R.id.action_settingsFragment_to_privacyPolicyFragment)
//                    "terms" -> navController.navigate(R.id.action_settingsFragment_to_termsFragment)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
