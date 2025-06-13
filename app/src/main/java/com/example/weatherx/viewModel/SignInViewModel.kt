package com.example.proyectoshopifyka.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class SignInViewModel: ViewModel() {


    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState
    private val _sessionValid = MutableLiveData<Boolean>()
    val sessionValid: LiveData<Boolean>
        get() = _sessionValid
    private val firebase by lazy { FirebaseAuth.getInstance() }

    fun requestSignIn(email: String, password: String) {
        _loaderState.value = true
        viewModelScope.launch {
            try{
            val result = firebase.signInWithEmailAndPassword(email, password).await()
            _loaderState.value = false
            _sessionValid.value = result.user != null

            } catch (e: Exception) {
            _sessionValid.value = false
            _loaderState.value = false
            Log.i("Firebase", "Se ha generado un problema")
            }
        }
    }


}