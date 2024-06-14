package com.salmakhd.android.cryptography

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salmakhd.android.cryptography.domain.EncryptedPreferences
import com.salmakhd.android.cryptography.domain.KeyProviderService
import com.salmakhd.android.cryptography.model.Keys
import com.salmakhd.android.cryptography.util.KeyPairHandler
import com.salmakhd.android.cryptography.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.lang.Exception
import java.security.KeyPairGenerator

@HiltViewModel
class MainViewModel constructor(
    private val preferences: EncryptedPreferences,
    private val keyProviderService: KeyProviderService
) : ViewModel() {
    private var _apiKeysReady: MutableState<RequestState<Boolean>> =
        mutableStateOf(RequestState.Idle)
    val apiKeysReady : State<RequestState<Boolean>> = _apiKeysReady

    private var _apiKeys: MutableState<Keys?> = mutableStateOf(null)
    val apiKeys: State<Keys?> = _apiKeys

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            _apiKeysReady.value = RequestState.Loading
            delay(1000)
            _apiKeysReady.value = fetchApiKeysAndStoreThemSecurely()
        }
    }
    private suspend fun fetchApiKeysAndStoreThemSecurely(): RequestState<Boolean> {
        return try {
            KeyPairHandler.generateKeypair()
            val publicKey = KeyPairHandler.getPublicKey()
            val fetchedData = fetchEncryptedApikeys(publicKey)
            if(fetchedData!=null){
                val decryptedData = KeyPairHandler.decryptData(fetchedData)
                val keys = Json.decodeFromString<Keys>(decryptedData)
                val result = preferences.saveEncryptedData(keys)
                _apiKeys.value = preferences.readEncryptedData()
                 RequestState.Success(data=result)
            } else {
                throw Exception ("Failed to fetch api keys")
            }
        } catch (e: Exception) {
            RequestState.Error(message = "Error: ${e.message}")
        }
    }

    private  suspend fun  fetchEncryptedApikeys(publicKey: String): String? {
        // send a request to server with a public key to get encrypted data
         val response = keyProviderService.getEncryptedApiKeys(publicKey)
        if (response.isSuccessful) return response.body()
        else throw Exception(response.message())
    }
}