package com.example.pixaura.ui.view.fragments.myAccountScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.pixaura.data.model.auth.User
import com.example.pixaura.databinding.FragmentMyAccountBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAccountFragment : Fragment() {
    private var _binding: FragmentMyAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MyAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MyAccountViewModel::class.java]

        // Example: get current logged in user email (replace with DataStore/SharedPref)
        val currentUserEmail = viewModel.currentUserEmail

        // Load user details
        viewModel.loadUser(currentUserEmail)

        // Observe LiveData
        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.etUsername.setText(it.username)
                binding.etEmail.setText(it.email)
                binding.etPhone.setText(it.mobileNumber)
            }
        }

        // Save button clicked
        binding.btnSave.setOnClickListener {
            val updatedUser = User(
                id = viewModel.user.value?.id ?: 0,
                username = binding.etUsername.text.toString(),
                email = binding.etEmail.text.toString(),
                mobileNumber = binding.etPhone.text.toString(),
                password = viewModel.user.value?.password ?: ""
            )
            viewModel.updateUser(updatedUser)
            Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}