package com.example.weatherx.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherx.R
import com.example.weatherx.databinding.FragmentRegisterBinding
import com.example.weatherx.utils.FragmentCommunicator
import com.example.weatherx.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel>()
    var isValid: Boolean = false
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        communicator = requireActivity() as OnboardingActivity
        setupView()
        setupObservers()
        return binding.root
    }

    private fun setupView() {
        binding.btBack.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btRegister.setOnClickListener {
            requestRegister()
        }
    }

    private fun setupObservers() {
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }
        viewModel.createdUser.observe(viewLifecycleOwner) { createdUser ->
            if (createdUser) {
                findNavController().navigate(R.id.action_registerFragment_to_personalInformationVariant)
            }
        }
    }

    private fun requestRegister() {
        if (binding.tietEmail.text.toString().isNotEmpty()
            && binding.tietPassword.text.toString().isNotEmpty()
        ) {
            isValid = true
        } else {
            isValid = false
        }

        if (isValid) {
            viewModel.requestRegister(
                binding.tietEmail.text.toString(),
                binding.tietPassword.text.toString()
            )
        } else {
            Toast.makeText(activity, "Please introduce your information", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
