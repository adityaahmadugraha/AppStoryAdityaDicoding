package com.aditya.appstoryaditya.ui.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya.appstoryaditya.models.Story
import com.aditya.appstoryaditya.models.User
import com.aditya.appstoryaditya.models.UserPreference
import com.aditya.appstoryaditya.repository.StoryAppRepository
import com.aditya.appstoryaditya.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserLocationViewModel @Inject constructor(
    private val repository: StoryAppRepository,
    private val userPref: UserPreference
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    fun getStoryWithLocation(
        token: String,
        location : Int = 1,
        onSuccess: (List<Story>) -> Unit
    ) = viewModelScope.launch {
        repository.getStoryWithLocation(token, location).collect{ response ->
            when(response){
                is Resource.Loading -> {
                    _isLoading.value = true
                }
                is Resource.Success -> {
                    _isLoading.value = false
                    if(!response.data.error){
                        onSuccess(response.data.listStory as List<Story>)
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
        userPref.getUser().collect{
            user(it)
        }
    }

}