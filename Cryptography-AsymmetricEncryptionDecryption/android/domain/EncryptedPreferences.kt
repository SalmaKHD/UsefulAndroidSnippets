package com.salmakhd.android.cryptography.domain

import com.salmakhd.android.cryptography.model.Keys

interface EncryptedPreferences {
    fun saveEncryptedData(keys: Keys): Boolean
    fun readEncryptedData(): Keys?
    fun areApiKeysReady(): Boolean
}