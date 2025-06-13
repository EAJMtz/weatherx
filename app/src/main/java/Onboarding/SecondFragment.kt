package com.example.proyectoshopifyka.view.onboarding

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.proyectoshopifyka.R
import com.example.proyectoshopifyka.databinding.FragmentSecondBinding
import com.example.proyectoshopifyka.utils.FragmentComunicator
import com.example.proyectoshopifyka.viewModel.PersonalInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PersonalInfoViewModel>()
    var isValid: Boolean = false
    private lateinit var communicator: FragmentComunicator
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        communicator = requireActivity() as HomeActivity
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        setupView()
        return binding.root

    }

    private fun setupView() {
        //actions
        //Validations information user
        binding.btnBoton.setOnClickListener {
            Log.e("BOTON", "HA ENTRADO EN EL BOTON")

            val userId = arguments?.getString("userId") ?: run {
                Toast.makeText(activity, "Error: No se obtuvo el ID de usuario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.createUserInfo(
                userId,
                binding.etNombre.text.toString(),
                binding.etApellidos.text.toString(),
                binding.etUsuario.text.toString(),
                format.parse(binding.etFecha.text.toString()) ?: Date()
            )
        }

        binding.etFecha.setOnClickListener {
            val calendario = Calendar.getInstance()
            val year = calendario.get(Calendar.YEAR)
            val month = calendario.get(Calendar.MONTH)
            val day = calendario.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->

                val fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                binding.etFecha.setText(fechaSeleccionada)
            }, year, month, day)

            datePicker.show()
        }

        setupObservers()
    }

    private fun setupObservers(){
        viewModel.loaderState.observe(viewLifecycleOwner){
            communicator.showLoader(it)
        }
        viewModel.operationSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                findNavController().navigate(R.id.action_secondFragment_to_navigation_home)
            } else {
                Toast.makeText(activity, "Error al registrar datos en Firestore", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}