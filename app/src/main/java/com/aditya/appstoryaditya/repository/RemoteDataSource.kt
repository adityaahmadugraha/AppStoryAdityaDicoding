package com.aditya.appstoryaditya.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.aditya.appstoryaditya.api.ApiService
import com.aditya.appstoryaditya.database.StoryDatabase
import com.aditya.appstoryaditya.models.LoginRequest
import com.aditya.appstoryaditya.models.RegisterRequest
import com.aditya.appstoryaditya.models.ServerResponse
import com.aditya.appstoryaditya.models.Story
import com.aditya.appstoryaditya.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val db: StoryDatabase,

) {
    fun registerUser(registerRequest: RegisterRequest) = flow {
        emit(Resource.Loading())
        val response = apiService.registerUser(registerRequest)
        emit(Resource.Success(response))
    }.catch {
        emit(Resource.Error(it.message ?: ""))
    }.flowOn(Dispatchers.IO)


    fun loginUser(loginRequest: LoginRequest) = flow {
        emit(Resource.Loading())
        val response = apiService.loginUser(loginRequest)
        emit(Resource.Success(response))
    }.catch {
        emit(Resource.Error(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    @ExperimentalPagingApi
    fun getAllStories(
        token: String
    ): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(apiService, db, token),
            pagingSourceFactory = {
                db.storyDao().getAllStory()
            }
        ).flow
    }

    fun getStoryWithLocation(
        token: String,
        location: Int?
    ) = flow<Resource<ServerResponse>> {
        emit(Resource.Loading())
        val response = apiService.getStories(token, location = location)
        response.let {
            if (!it.error) emit(Resource.Success(it))
            else emit(Resource.Error(it.message))
        }
    }.catch {
        emit(Resource.Error(it.message ?: ""))
    }.flowOn(Dispatchers.IO)


    fun insertStori(
        token: String,
        requestBody: RequestBody
    ) = flow<Resource<ServerResponse>> {
        emit(Resource.Loading())
        val response = apiService.insertStori(token, requestBody)
        response.let {
            if (!it.error) emit(Resource.Success(it))
            else emit(Resource.Error("Data Tidak Ditemuan"))
        }
    }.catch {
        emit(Resource.Error(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

}
