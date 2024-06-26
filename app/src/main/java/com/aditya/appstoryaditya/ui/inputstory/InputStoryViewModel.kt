package com.aditya.appstoryaditya.ui.inputstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya.appstoryaditya.models.ServerResponse
import com.aditya.appstoryaditya.models.User
import com.aditya.appstoryaditya.models.UserPreference
import com.aditya.appstoryaditya.repository.RemoteDataSource
import com.aditya.appstoryaditya.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class InputStoryViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val userPreference: UserPreference
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun inputStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null,
        onSuccess: (ServerResponse) -> Unit
    ) = viewModelScope.launch {
        remoteDataSource.inputStory(token, file, description, lat, lon).collect { response ->
        when(response){
                is Resource.Loading -> {
                    _isLoading.value = true
                }
                is Resource.Success -> {
                    _isLoading.value = false
                    if(!response.data.error){
                        onSuccess(response.data)
                    }
                }
                is Resource.Error -> {
                    _isLoading.value = false
                }
            }
        }
    }


    fun getUser(
        user: (User) -> Unit
    ) = viewModelScope.launch {
        userPreference.getUser().collect{
            user(it)
        }
    }

}