package com.aditya.appstoryaditya.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.aditya.appstoryaditya.models.Story
import com.aditya.appstoryaditya.models.User
import com.aditya.appstoryaditya.models.UserPreference
import com.aditya.appstoryaditya.repository.AppRepository
import com.aditya.appstoryaditya.repository.RemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val repository: AppRepository,
    private val userPreference: UserPreference
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    @ExperimentalPagingApi
    fun getStories(
        token: String,
        onSuccess: (PagingData<Story>) -> Unit
    ) = viewModelScope.launch {
        remoteDataSource.getAllStories(token).collect {
            onSuccess(it)
        }
    }

//    fun getUser(
//        user: (User) -> Unit
//    ) = viewModelScope.launch {
//        userPreference.getUser().collect{
//            user(it)
//        }
//    }

    fun getUser() = repository.getUser().asLiveData()

    fun logout() = viewModelScope.launch {
        repository.deleteUser()
    }

}