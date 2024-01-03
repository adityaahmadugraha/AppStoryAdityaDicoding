package com.aditya.appstoryaditya.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.aditya.appstoryaditya.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    fun inputLaporan(
        token: String,
        requestBody: RequestBody
    ) = repository.insertStori(token, requestBody).asLiveData()

    fun getUser() = repository.getUser().asLiveData()
}