package com.example.pixaura.ui.view.fragments.subScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pixaura.databinding.FragmentCategoryWallpaperBinding
import com.example.pixaura.ui.adapter.HomeScreenFragmentAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryWallPaperFragment : Fragment() {

    private var _binding: FragmentCategoryWallpaperBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoryWallPaperViewModel by viewModels()
    private val args: CategoryWallPaperFragmentArgs by navArgs()

    private lateinit var wallpaperAdapter: HomeScreenFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryWallpaperBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.categoryToolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadWallpapers()
        observeLoadState()
    }

    private fun setupRecyclerView() {
        wallpaperAdapter = HomeScreenFragmentAdapter { id ->
            val action = CategoryWallPaperFragmentDirections
                .actionCategoryWallPaperFragmentToWallpaperViewFragment(id.id)
            findNavController().navigate(action)
        }
        binding.categoryWallpaperRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = wallpaperAdapter
        }
    }

    private fun loadWallpapers() {
        val query = args.query
        Log.d("CategoryWallPaperFragment", "Query received: $query")
        binding.categoryToolbar.title = query
        viewModel.search(query)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.wallpapers.collectLatest { pagingData ->
                wallpaperAdapter.submitData(pagingData)
            }
        }
    }

    private fun observeLoadState() {
        viewLifecycleOwner.lifecycleScope.launch {
            wallpaperAdapter.loadStateFlow.collectLatest { loadState ->
                val isLoading = loadState.refresh is LoadState.Loading
                val isEmpty = loadState.refresh is LoadState.NotLoading && wallpaperAdapter.itemCount == 0

                binding.categoryWallpaperProgressBar.isVisible = isLoading
                binding.categoryWallpaperRecyclerView.isVisible = !isLoading && !isEmpty
                binding.emptyCategoryAnimation.isVisible = isEmpty
                binding.emptyCategoryText.isVisible = isEmpty
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
