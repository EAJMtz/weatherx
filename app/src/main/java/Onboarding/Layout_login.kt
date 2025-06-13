package com.example.proyectoshopifyka.view.onboarding
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.proyectoshopifyka.databinding.FragmentLayoutLoginBinding
import com.example.proyectoshopifyka.utils.FragmentComunicator
import com.example.proyectoshopifyka.viewModel.SignInViewModel
import androidx.navigation.fragment.findNavController
import androidx.core.widget.addTextChangedListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.proyectoshopifyka.R
import com.example.proyectoshopifyka.core.LocationProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class layout_login : Fragment() {
    @Inject lateinit var locationProvider: LocationProvider

    private var _binding: FragmentLayoutLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SignInViewModel>()
    var isValid: Boolean = false
    private lateinit var communicator: FragmentComunicator

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (fineLocationGranted || coarseLocationGranted) {
            getUserLocation()
        } else {
            Toast.makeText(requireContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        communicator = requireActivity() as HomeActivity
        _binding = FragmentLayoutLoginBinding.inflate(inflater, container, false)
        setupView()
        setupObservers()
        return binding.root

    }

    private fun setupView() {

        getUserLocation()

        binding.textView2.setOnClickListener {
            findNavController().navigate(R.id.action_layout_login_to_layout_register)
        }
        binding.textView.setOnClickListener {
            findNavController().navigate(R.id.action_layout_login_to_restorePassword)
        }

        binding.filledButton.setOnClickListener {


            if (validateInputs()) {
               requestLogin()
            } else {
                Toast.makeText(activity, "Correo y contraseña son obligatorios", Toast.LENGTH_SHORT).show()
            }


        }

        binding.etCorreo.addTextChangedListener {
            if (binding.etCorreo.text.toString().isEmpty()) {
                binding.tilCorreo.error = "Por favor introduce un correo "
                isValid = false
            } else {
                isValid = true
            }
        }

        binding.etContrasenia.addTextChangedListener {
            if (binding.etContrasenia.text.toString().isEmpty()) {
                binding.tilContrasenia.error = "Por favor introduce una contraseña"
                isValid = false
            } else {
                isValid = true
            }
        }
    }

    private fun validateInputs(): Boolean {
        val emailNotEmpty = binding.etCorreo.text.toString().isNotEmpty()
        val passwordNotEmpty = binding.etContrasenia.text.toString().isNotEmpty()

        isValid = emailNotEmpty && passwordNotEmpty

        binding.etCorreo.error = if (!emailNotEmpty) "Introduce un correo" else null
        binding.etContrasenia.error = if (!passwordNotEmpty) "Introduce tu contraseña" else null

        return isValid
    }

    private fun setupObservers() {

        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }
        viewModel.sessionValid.observe(viewLifecycleOwner) { validSession ->
            Log.d("layout_login", "Estado de sesión válida: $validSession")
            if (validSession) {
                val bundle = Bundle().apply {
                    putString("email", binding.etCorreo.text?.toString() ?: "Correo no disponible")
                }
                findNavController().navigate(R.id.action_layout_login_to_weatherFragment, bundle)
            } else {
                Toast.makeText(activity, "Ingreso inválido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestLogin() {
        val email = binding.etCorreo.text?.toString()?.trim() ?: ""
        val password = binding.etContrasenia.text?.toString()?.trim() ?: ""

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Correo y contraseña son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.requestSignIn(email, password)
    }

    fun getUserLocation() {
        if (!hasLocationPermission()) {
            requestPermissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
            return
        }

        lifecycleScope.launch {
            try {
                val location = locationProvider.getCurrentLocation()
                location?.let {
                    Log.i("LOCATION", "Ubicación obtenida: ${it.latitude}, ${it.longitude}")
                } ?: run {
                    Log.e("LOCATION", "Error al obtener la ubicación")
                    Toast.makeText(requireContext(), "Error al obtener la ubicación", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("LOCATION_ERROR", "Error en solicitud de ubicación: ${e.message}")
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}