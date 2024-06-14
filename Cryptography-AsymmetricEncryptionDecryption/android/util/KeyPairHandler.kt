package com.salmakhd.android.cryptography.util

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.nio.charset.StandardCharsets
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.Base64
import javax.crypto.Cipher

const val KEYSTORE_ALIAS = "API_KEYS"
const val ANDROID_KEYSTORE = "Android_KeyStore"
const val TRANSFORMATION = "RSA/ECB/PKCS1Padding"
// generate public and private keys using KeyStore,
// store private key in keystore,
// send public key to server for encryption
object KeyPairHandler {
    private val keystore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
        load(null)
    }
    fun generateKeypair() {
        if(keystore!=null && keystore.containsAlias(KEYSTORE_ALIAS)) return

        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA,
            ANDROID_KEYSTORE
        )
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEYSTORE_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setUserAuthenticationRequired(false)
            .build()

        keyPairGenerator.initialize(keyGenParameterSpec)
        keyPairGenerator.generateKeyPair()
    }

    // for sending to server
    fun getPublicKey(): String {
        val privateKeyEntry = keystore?.getEntry(KEYSTORE_ALIAS, null) as? KeyStore.PrivateKeyEntry
        val encodedPublicKey = privateKeyEntry?.certificate?.publicKey?.encoded
        return Base64.getEncoder().encodeToString(encodedPublicKey)
    }

    fun decryptData(data: String) : String{
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val privateKeyEntry = keystore.getEntry(KEYSTORE_ALIAS, null) as? KeyStore.PrivateKeyEntry
        cipher.init(Cipher.DECRYPT_MODE, privateKeyEntry?.privateKey)
        val descryptedData = cipher.doFinal(Base64.getDecoder().decode(data))
        return String(descryptedData, StandardCharsets.UTF_8)

    }
}