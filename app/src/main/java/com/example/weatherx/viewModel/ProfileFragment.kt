package com.example.proyectoshopifyka.view.home


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.proyectoshopifyka.databinding.FragmentProfileBinding
import com.example.proyectoshopifyka.model.User
import com.example.proyectoshopifyka.utils.FragmentComunicator
import com.example.proyectoshopifyka.view.home.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: FragmentComunicator
    private val viewModel by viewModels<ProfileViewModel>()
    val locale = Locale("es", "MX")
    val dateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", locale)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        communicator = requireActivity() as HomeActivity
        setupView()
        return binding.root
    }

    private fun setupView() {
        setupObservers()
        viewModel.getUserInfo()
        binding.deleteButton.setOnClickListener {
            viewModel.userInfo.value?.let { user ->
                viewModel.deleteUser(user.id)
            }
        }
    }

    private fun setupObservers() {
        viewModel.userInfo.observe(viewLifecycleOwner) { user ->
            updateUI(user)
        }
        viewModel.loaderState.observe(viewLifecycleOwner) {loaderState ->
            communicator.showLoader(loaderState)
        }
        viewModel.userDelete.observe(viewLifecycleOwner) { isDeleted ->
            if (isDeleted) {
                Log.d("Firestore", "Usuario eliminado exitosamente")
                val intent = Intent(requireContext(), HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }
        }

        binding.updateButton.setOnClickListener {
            val userId = viewModel.userInfo.value?.id

            if (userId != null) {
                val action = ProfileFragmentDirections.actionNavigationProfileToUpdatePersonalInfoFragment(userId)
                findNavController().navigate(action)
            } else {
                Log.e("Navigation", "No se pudo obtener el ID del usuario")
            }
        }

    }


    private fun updateUI(user: User){
        binding?.apply {
            fullNameLabel.text = "${user.name} ${user.lastName}"
            userNameLabel.text = user.userName
            dateLabel.text = dateFormat.format(user.bornDate)
        }
    }

}