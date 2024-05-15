package com.example.toyopay.di

import com.example.toyopay.networkServices.CommonApiService
import com.example.toyopay.networkServices.OkHttpClientHelper
import com.example.toyopay.util.others.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun providesTayoPayApi(): CommonApiService {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(OkHttpClientHelper().getOkHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(CommonApiService::class.java)
    }

}