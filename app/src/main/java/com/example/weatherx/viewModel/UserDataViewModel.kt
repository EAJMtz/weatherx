package com.example.proyectoshopifyka.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserDataViewModel : ViewModel() {

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    private val _sessionValid = MutableLiveData<Boolean>()
    val sessionValid: LiveData<Boolean>
        get() = _sessionValid

    private val firestore = FirebaseFirestore.getInstance()

    fun requestDataUser(nombre: String, apellidos: String, usuario: String, fecha: String) {
        _loaderState.value = true

        val userData = hashMapOf(
            "nombre" to nombre,
            "apellidos" to apellidos,
            "usuario" to usuario,
            "fecha" to fecha
        )

        viewModelScope.launch {
            try {
                // Guardar en la colección "usuarios"
                firestore.collection("usuarios")
                    .add(userData)
                    .await()

                _loaderState.value = false
                _sessionValid.value = true
            } catch (e: Exception) {
                _loaderState.value = false
                Log.e("Firestore", "Error al guardar datos: ${e.message}")
            }
        }
    }
}