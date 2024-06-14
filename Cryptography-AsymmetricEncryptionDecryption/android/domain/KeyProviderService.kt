package com.salmakhd.android.cryptography.domain

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.security.PublicKey

interface KeyProviderService {
    @Headers("Content-Type: application/json")
    @POST("api/provide")
    suspend fun getEncryptedApiKeys(@Body publicKey: String): Response<String>
}