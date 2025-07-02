package com.example.pixaura.ui.view.fragments.topBars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.pixaura.R
import com.example.pixaura.databinding.FragmentCategoryTopBarBinding

class CategoryTopBarFragment : Fragment() {

    private var _binding: FragmentCategoryTopBarBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryTopBarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()

        binding.materialTopAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_search -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }
                R.id.action_settings -> {
                    navController.navigate(R.id.settingFragment)
                    true
                }
                else -> false
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
