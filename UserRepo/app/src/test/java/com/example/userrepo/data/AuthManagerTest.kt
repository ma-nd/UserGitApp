package com.example.userrepo.data

import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthManagerTest {
    private val encryptedPrefs = mockk<EncryptedPreferences>(relaxed = true)
    private val authManager = AuthManager(encryptedPrefs)
    private val testName = "User test name"
    private val testToken = "User token"

    @After
    fun after() {
        unmockkAll()
    }

    @Test
    fun saveLoginData_isTempLogin() = runTest {
        authManager.saveLoginData(userName = testName, token = testToken, isTempLogin = true)
        assertEquals("", encryptedPrefs.token)
        assertEquals("", encryptedPrefs.userName)
        assertTrue(authManager.isUserAuthorize())
    }

    @Test
    fun saveLoginData_isNotTempLogin() = runTest {
        authManager.saveLoginData(userName = testName, token = testToken, isTempLogin = false)
        verify { encryptedPrefs setProperty "userName" value testName }
        verify { encryptedPrefs setProperty "token" value testToken }

        every { encryptedPrefs.userName } returns testName
        every { encryptedPrefs.token } returns testToken
        assertTrue(authManager.isUserAuthorize())
    }

    @Test
    fun isAuthorize_false() = runTest {
        authManager.logOut()
        assertFalse(authManager.isUserAuthorize())
    }

    @Test
    fun getUserCredentials_isTempLogin() = runTest {
        authManager.saveLoginData(userName = testName, token = testToken, isTempLogin = true)

        assertEquals(Pair(testName, testToken), authManager.getUserCredentials())
    }

    @Test
    fun getUserCredentials_isNotTempLogin() = runTest {
        authManager.saveLoginData(userName = testName, token = testToken, isTempLogin = false)
        every { encryptedPrefs.userName } returns testName
        every { encryptedPrefs.token } returns testToken
        assertEquals(Pair(testName, testToken), authManager.getUserCredentials())
    }
}