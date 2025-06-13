package com.example.proyectoshopifyka.view.home
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.proyectoshopifyka.R
import com.example.proyectoshopifyka.databinding.FragmentUpdatePersonalInfoBinding
import com.example.proyectoshopifyka.model.User
import com.example.proyectoshopifyka.utils.FragmentComunicator
import com.example.proyectoshopifyka.view.home.viewModel.UpdatePersonalInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class UpdatePersonalInfoFragment : Fragment() {

    private var _binding: FragmentUpdatePersonalInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: FragmentComunicator
    private val viewModel by viewModels<UpdatePersonalInfoViewModel>()
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUpdatePersonalInfoBinding.inflate(inflater, container, false)
        communicator = requireActivity() as HomeActivity
        setupView()
        return binding.root
    }

    private fun setupView() {
        val userId = arguments?.let {
            UpdatePersonalInfoFragmentArgs.fromBundle(it).userId
        } ?: return

        viewModel.getUserInfo(userId)

        viewModel.userInfo.observe(viewLifecycleOwner) { user ->
            binding.firstNameTelt.setText(user.name)
            binding.secondNameTelt.setText(user.lastName)
            binding.userNameTelt.setText(user.userName)
            binding.dateUserTelt.setText(format.format(user.bornDate))
        }


        binding.dateUserTelt.apply {
            isFocusable = false
            isClickable = true
        }

        binding.btnUpdatepersonalInfo.setOnClickListener {
            Log.e("BOTON", "HA ENTRADO EN EL BOTON")
            if (userId != null) {
                viewModel.updateUserInfo(userId, User(
                    id = userId,
                    name = binding.firstNameTelt.text.toString(),
                    lastName = binding.secondNameTelt.text.toString(),
                    userName = binding.userNameTelt.text.toString(),
                    bornDate = format.parse(binding.dateUserTelt.text.toString()) ?: Date()
                ))
            }
        }

        binding.dateUserTelt.setOnClickListener {
            val calendario = Calendar.getInstance()
            val year = calendario.get(Calendar.YEAR)
            val month = calendario.get(Calendar.MONTH)
            val day = calendario.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                val fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                binding.dateUserTelt.setText(fechaSeleccionada)
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
                Log.d("Firestore", "Redirigiendo a ProfileInfoFragment")
                findNavController().navigate(R.id.action_updatePersonalInfoFragment_to_navigation_profile)
            }
        }
    }

}