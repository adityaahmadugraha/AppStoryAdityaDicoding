package com.aditya.appstoryaditya.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.aditya.appstoryaditya.models.RegisterRequest
import com.aditya.appstoryaditya.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    fun registerUser(request: RegisterRequest) = repository.registerUser(request).asLiveData()

    fun getUser() = repository.getUser().asLiveData()
}