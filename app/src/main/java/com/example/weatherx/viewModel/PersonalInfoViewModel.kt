package com.example.proyectoshopifyka.viewModel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoshopifyka.model.User
import com.example.proyectoshopifyka.network.UserRepository
import com.example.proyectoshopifyka.core.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PersonalInfoViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean>
        get() = _operationSuccess

    fun createUserInfo(userId: String, name: String, lastName: String, userName: String, bornDate: Date) {
        if (userId.isEmpty()) {
            Log.e("Firestore", "Error: userId está vacío")
            _operationSuccess.value = false
            return
        }

        val user = User(id = userId, name, lastName, userName, bornDate)
        _loaderState.value = true
        viewModelScope.launch {
            when (val result = repository.createUser(user)) {
                is ResultWrapper.Success -> {
                    _loaderState.value = false
                    _operationSuccess.value = true
                }
                is ResultWrapper.Error -> {
                    _loaderState.value = false
                    _operationSuccess.value = false
                    Log.e("Firestore", "Error al registrar usuario: ${result.exception.message}")
                }
            }
        }
    }
}