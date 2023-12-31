package com.aditya.appstoryaditya.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aditya.appstoryaditya.models.LoginRequest
import com.aditya.appstoryaditya.models.User
import com.aditya.appstoryaditya.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

    private val repository: AppRepository
) : ViewModel() {

    fun loginUser(request: LoginRequest) = repository.loginUser(request).asLiveData()

    fun saveUser(user: User) = viewModelScope.launch {
        repository.saveUser(user)
    }
    fun getUser() = repository.getUser().asLiveData()


}