package com.example.pixaura.ui.view.categoryScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pixaura.R
import com.example.pixaura.databinding.FragmentCategoryBinding
import com.example.pixaura.ui.activity.MainScreenActivity
import com.example.pixaura.ui.adapter.CategoriesAdapter
import com.example.pixaura.utils.UIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoriesAdapter
    private val viewModel: CategoryViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        binding.categoryTopBar.materialTopAppBar.post {
            val menu = binding.categoryTopBar.materialTopAppBar.menu
            menu.findItem(R.id.action_settings)?.isVisible = false
        }

        binding.categoryTopBar.materialTopAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_search -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }
                else -> false
            }
        }

        setupRecyclerView()

        // Observe ViewModel state
        lifecycleScope.launch {
            viewModel.categoryState.collect { state ->
                when (state) {
                    is UIState.Loading -> {
                        binding.categoryProgressBar.visibility = View.VISIBLE
                    }

                    is UIState.Success -> {
                        binding.categoryProgressBar.visibility = View.GONE
                        categoryAdapter.submitList(state.data.categoryList)

                        binding.categoryTopBar.materialTopAppBar.setOnMenuItemClickListener { menuItem ->
                            when (menuItem.itemId) {
                                R.id.action_search -> {
                                    (activity as? MainScreenActivity)?.navigateToTab(R.id.searchFragment)
                                    true
                                }

                                R.id.action_settings -> {
                                    true
                                }

                                else -> false
                            }
                        }
                    }

                    is UIState.Error -> {
                        binding.categoryProgressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoriesAdapter(emptyList()) { categoryName ->
            val action = CategoryFragmentDirections
                .actionCategoryFragmentToCategoryWallPaperFragment(categoryName)
            navController.navigate(action)
        }


        binding.categoryRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = categoryAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
