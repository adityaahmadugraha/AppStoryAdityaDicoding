package com.aditya.appstoryaditya.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.aditya.appstoryaditya.models.Story
import com.aditya.appstoryaditya.models.User
import com.aditya.appstoryaditya.models.UserPreference
import com.aditya.appstoryaditya.repository.StoryAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: StoryAppRepository,
    private val userPreference: UserPreference
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    @ExperimentalPagingApi
    fun getStories(
        token: String,
        onSuccess: (PagingData<Story>) -> Unit
    ) = viewModelScope.launch {
        repository.getAllStories(token).collect {
            onSuccess(it)
        }
    }

    fun getUser(
        user: (User) -> Unit
    ) = viewModelScope.launch {
        userPreference.getUser().collect{
            user(it)
        }
    }

    fun logout(user: User) = viewModelScope.launch {
        userPreference.deleteUser(user)
    }



}