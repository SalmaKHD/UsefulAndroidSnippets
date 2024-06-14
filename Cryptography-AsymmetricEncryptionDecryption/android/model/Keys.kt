package com.salmakhd.android.cryptography.model

import kotlinx.serialization.Serializable

@Serializable
data class Keys (
    val firstKey: String,
    val secondKey:String
)