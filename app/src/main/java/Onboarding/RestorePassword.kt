package com.example.proyectoshopifyka.view.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.proyectoshopifyka.R
import com.example.proyectoshopifyka.databinding.FragmentLayoutRegisterBinding
import com.example.proyectoshopifyka.databinding.FragmentRestorePasswordBinding
import com.example.proyectoshopifyka.utils.FragmentComunicator

class restorePassword : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentRestorePasswordBinding? = null
    private val binding get() = _binding!!
    var isValid: Boolean = false
    private lateinit var communicator: FragmentComunicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestorePasswordBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {

        binding.imageButton.setOnClickListener {
            findNavController().navigate(R.id.action_restorePassword_to_layout_login)
        }

        binding.etCorreo.addTextChangedListener {
            if (binding.etCorreo.text.toString().isEmpty()) {
                binding.tilCorreo.error = "Por favor introduce un correo"
                isValid = false
            } else {
                isValid = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}