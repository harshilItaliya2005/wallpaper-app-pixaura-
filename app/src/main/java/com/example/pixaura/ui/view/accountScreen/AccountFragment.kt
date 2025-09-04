package com.example.pixaura.ui.view.accountScreen

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log.e
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.pixaura.R
import com.example.pixaura.databinding.FragmentAccountBinding
import com.example.pixaura.ui.activity.loginScreen.LoginActivity
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AccountViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var sharedPref: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()
        sharedPref = requireActivity().getSharedPreferences("user_prefs", MODE_PRIVATE)
        edit = sharedPref.edit()
        binding.profileTopBar.materialTopAppBar.post {
            val menu = binding.profileTopBar.materialTopAppBar.menu
            menu.findItem(R.id.action_search)?.isVisible = false
            menu.findItem(R.id.action_settings)?.isVisible = false
        }
        lifecycleScope.launch {
            val email = sharedPref.getString("logged_in_email", null)
            if (!email.isNullOrEmpty()) {
                val user = viewModel.getCurrentUser(email)
                user?.let {
                    binding.userName.text = it.username
                    binding.userEmail.text = it.email
                }
            }
        }

        binding.apply {
            download.setOnClickListener {
                navController.navigate(R.id.action_accountFragment_to_downloadFragment)
            }
            useOwnWallpaper.setOnClickListener {
                navController.navigate(R.id.action_accountFragment_to_galleryFragment)
            }
            setting.setOnClickListener {
                navController.navigate(R.id.action_accountFragment_to_settingFragment)
            }
            logOut.setOnClickListener {
                sharedPref.edit {
                    putBoolean("is_logged_in", false)
                    apply()
                }
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
        }

    }
}