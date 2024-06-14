package com.salmakhd.android.cryptography.data

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.salmakhd.android.cryptography.domain.EncryptedPreferences
import com.salmakhd.android.cryptography.model.Keys

const val PREFERENCES_NAME = "Api_keys"
const val PREFERENCES_FIRST_KEY = "preferences_first_key"
const val PREFERENCES_SECOND_KEY = "preferences_second_key"
class EncryptedPreferencesImpl (context: Context): EncryptedPreferences{
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build() as? MasterKey

    private val preferences = masterKey?.let {
        EncryptedSharedPreferences.create(
            context,
            PREFERENCES_NAME,
            it,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    override fun saveEncryptedData(keys: Keys): Boolean {
        if(preferences!=null) {
            preferences.edit {
                putString(PREFERENCES_FIRST_KEY, keys.firstKey)
                putString(PREFERENCES_SECOND_KEY, keys.secondKey)
            }
            return true
        }
        return false
    }

    override fun readEncryptedData(): Keys? {
        val firstKey = preferences?.getString(PREFERENCES_FIRST_KEY, null)
        val secondKey = preferences?.getString(PREFERENCES_SECOND_KEY, null)
        return if(firstKey!=null && secondKey!=null) {
             Keys(
                firstKey,
                secondKey
            )
        } else null
    }

    override fun areApiKeysReady(): Boolean {
        val firstCondition = preferences!=null
                && preferences.contains(PREFERENCES_FIRST_KEY)
                && preferences.contains(PREFERENCES_SECOND_KEY)
        val secondCondition = readEncryptedData() !=null
        return firstCondition && secondCondition
    }
}