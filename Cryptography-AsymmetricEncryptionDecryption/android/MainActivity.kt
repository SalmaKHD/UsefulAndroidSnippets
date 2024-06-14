package com.salmakhd.android.cryptography

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.salmakhd.android.cryptography.screen.MainScreen
import com.salmakhd.android.cryptography.ui.theme.CryptographyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptographyTheme {
                val viewModel: MainViewModel = hiltViewModel()
                val apiKeysReady by viewModel.apiKeysReady
                val apiKeys by viewModel.apiKeys
                MainScreen(
                    apiKeysReady = apiKeysReady,
                    apiKeys = apiKeys,
                    onTryAgain = { viewModel.fetchData() }
                )
            }
        }
    }
}