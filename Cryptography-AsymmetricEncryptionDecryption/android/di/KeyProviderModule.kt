package com.salmakhd.android.cryptography.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.salmakhd.android.cryptography.domain.KeyProviderService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "http://10.0.2.2:8080"
@Module
@InstallIn(SingletonComponent::class)
object KeyProviderModule {
    @Provides
    @Singleton
    fun provideApiService(): KeyProviderService {
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(KeyProviderService::class.java)
    }


}