package com.example.weatherx.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean> get() = _loaderState

    private val _passwordResetState = MutableLiveData<Boolean>()
    val passwordResetState: LiveData<Boolean> get() = _passwordResetState

    fun sendPasswordResetEmail(email: String) {
        _loaderState.postValue(true)

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                _loaderState.postValue(false)

                _passwordResetState.postValue(task.isSuccessful)
            }
            .addOnFailureListener {
                _loaderState.postValue(false)
                _passwordResetState.postValue(false)
            }
    }

}
