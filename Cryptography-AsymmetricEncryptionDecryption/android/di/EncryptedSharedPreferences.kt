package com.salmakhd.android.cryptography.di

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import com.salmakhd.android.cryptography.data.EncryptedPreferencesImpl
import com.salmakhd.android.cryptography.domain.EncryptedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EncryptedSharedPreferences {
    @Provides
    @Singleton
    fun providesSharedPreferences(
        @ApplicationContext context: Context
    ): EncryptedPreferences {
        return EncryptedPreferencesImpl(context = context)
    }
}