package com.example.pixaura.ui.view.searchScreen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pixaura.R
import com.example.pixaura.databinding.FragmentSearchBinding
import com.example.pixaura.ui.adapter.HomeScreenFragmentAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: HomeScreenFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()
        binding.searchTopBar.materialTopAppBar.post {
            val menu = binding.searchTopBar.materialTopAppBar.menu
            menu.findItem(R.id.action_search)?.isVisible = false
        }

        binding.searchTopBar.materialTopAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_settings -> {
                    navController.navigate(R.id.settingFragment)
                    true
                }

                else -> false
            }
        }

        adapter = HomeScreenFragmentAdapter { photo ->
            val action = SearchFragmentDirections
                .actionSearchFragmentToWallpaperViewFragment(photo.id)
            findNavController().navigate(action)
        }

        binding.searchRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.searchRecyclerView.adapter = adapter

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val query = binding.searchEditText.text.toString().trim()
                if (query.isNotEmpty()) {
                    adapter.submitData(lifecycle, PagingData.empty())
                    viewModel.search(query)
                    hideKeyboard()
                }
                true
            } else false
        }

        observeResults()
    }


    private fun observeResults() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.wallpapers.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                val isLoading = loadState.refresh is LoadState.Loading
                val isEmpty =
                    loadState.refresh is LoadState.NotLoading && adapter.snapshot().isEmpty()

                val hasError = loadState.refresh is LoadState.Error
                Log.d(
                    "SearchState",
                    "isEmpty: $isEmpty, isLoading: $isLoading, itemCount: ${adapter.itemCount}"
                )

                binding.searchProgressBar.isVisible = isLoading
                binding.searchRecyclerView.isVisible = !isLoading && !isEmpty
                binding.emptySearchAnimation.isVisible = isEmpty
                binding.emptySearchText.isVisible = isEmpty
            }
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
