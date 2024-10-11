package com.example.userrepo.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class EncryptedPreferences(context: Context) {
    private val key = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val masterKey = "GithubAppPrefs"
    private val encryptedPref = EncryptedSharedPreferences.create(
        key,
        masterKey,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val USER_NAME_KEY = "userNameKey"
    private val TOKEN_KEY = "tokenKey"

    var userName: String
        get() = getString(USER_NAME_KEY)
        set(value) = setString(USER_NAME_KEY, value)

    var token: String
        get() = getString(TOKEN_KEY)
        set(value) = setString(TOKEN_KEY, value)

    private fun getString(key: String): String {
        return encryptedPref.getString(key, "").orEmpty()
    }

    private fun setString(key: String, string: String) {
        return encryptedPref.edit().putString(key, string).apply()
    }
}