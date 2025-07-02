package com.example.pixaura.ui.view.HomeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pixaura.R
import com.example.pixaura.databinding.FragmentHomeBinding
import com.example.pixaura.ui.activity.MainScreenActivity
import com.example.pixaura.ui.adapter.HomeScreenFragmentAdapter
import com.example.pixaura.ui.adapter.LoadingStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var homeScreenAdapter: HomeScreenFragmentAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        homeScreenAdapter = HomeScreenFragmentAdapter { photo ->
            val action = HomeFragmentDirections
                .actionHomeFragmentToWallpaperViewFragment(photo.id)
            navController.navigate(action)
        }
        navController = findNavController()

        binding.homeTopBar.materialTopAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_search -> {
                    (activity as? MainScreenActivity)?.navigateToTab(R.id.searchFragment)
                    true
                }
                R.id.action_settings -> {
                    navController.navigate(R.id.action_homeFragment_to_settingFragment)
                    Toast.makeText(requireContext(), "Settings clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        setupRecyclerView()
        observeWallpapers()
        observeLoadState()
    }

    private fun setupRecyclerView() {
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = homeScreenAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter { homeScreenAdapter.retry() }
            )
        }
    }

    private fun observeWallpapers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.wallpapers.collectLatest { pagingData ->
                homeScreenAdapter.submitData(pagingData)
            }
        }
    }

    private fun observeLoadState() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeScreenAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.progressBar.isVisible =
                    loadStates.refresh is LoadState.Loading && homeScreenAdapter.itemCount == 0
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
