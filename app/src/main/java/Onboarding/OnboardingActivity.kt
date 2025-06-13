package com.example.proyectoshopifyka.view.onboarding

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.proyectoshopifyka.R
import com.example.proyectoshopifyka.databinding.ActivityOnboardingBinding
import com.example.proyectoshopifyka.utils.FragmentComunicator
import com.google.android.material.bottomnavigation.BottomNavigationView


class OnboardingActivity : AppCompatActivity(), FragmentComunicator {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun showLoader(value: Boolean) {
        binding.loaderContainerView.visibility = if (value) View.VISIBLE else View.GONE
    }

    override fun enviarMensaje(mensaje: String) {

    }
}