package com.example.userrepo.data

class AuthManager(private val encryptedPrefs: EncryptedPreferences) {
    private var tempUserName: String = ""
    private var tempToken: String = ""

    fun saveLoginData(userName: String, token: String, isTempLogin: Boolean) {
        if (isTempLogin) {
            tempUserName = userName
            tempToken = token
            encryptedPrefs.userName = ""
            encryptedPrefs.token = ""
        } else {
            tempUserName = ""
            tempToken = ""
            encryptedPrefs.userName = userName
            encryptedPrefs.token = token
        }
    }

    fun isUserAuthorize(): Boolean {
        return (tempUserName.isNotBlank() && tempToken.isNotBlank())
                || (encryptedPrefs.userName.isNotBlank() && encryptedPrefs.token.isNotBlank())
    }

    fun getUserCredentials(): Pair<String, String> {
        return if (tempUserName.isNotBlank() && tempToken.isNotBlank()) {
            Pair(tempUserName, tempToken)
        } else {
            Pair(encryptedPrefs.userName, encryptedPrefs.token)
        }
    }

    fun logOut() {
        tempUserName = ""
        tempToken = ""
        encryptedPrefs.userName = ""
        encryptedPrefs.token = ""
    }
}