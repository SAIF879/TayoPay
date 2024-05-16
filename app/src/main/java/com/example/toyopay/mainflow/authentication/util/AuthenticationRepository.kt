package com.example.toyopay.mainflow.authentication.util

import com.example.toyopay.networkServices.ApiResult
import com.example.toyopay.networkServices.CommonApiService
import com.example.toyopay.networkServices.data.LoginRequestBody
import com.example.toyopay.networkServices.data.LoginUserResponse
import com.example.toyopay.networkServices.data.RegisterRequestBody
import com.example.toyopay.networkServices.data.RegisterUserData
import com.example.toyopay.networkServices.data.RegisterUserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthenticationRepository  @Inject constructor(private val api : CommonApiService){

    suspend fun postUserRegistration(requestBody: RegisterRequestBody): Flow<ApiResult<RegisterUserResponse>> =
        flow {
            emit(ApiResult.Loading)
            try {
                val registerData = api.registerUser(requestBody)
                emit(ApiResult.Success(registerData.body()))
            } catch (e: Exception) {
                emit(ApiResult.Error(e.localizedMessage ?: "Unknown error"))
            }
        }

    suspend fun postUserLoginDetails(requestBody: LoginRequestBody): Flow<ApiResult<LoginUserResponse>> =
        flow {
            emit(ApiResult.Loading)
            try {
                val loginData = api.loginUser(requestBody)
                emit(ApiResult.Success(loginData.body()))
            } catch (e: Exception) {
                emit(ApiResult.Error(e.localizedMessage ?: "Unknown error"))
            }
        }



}