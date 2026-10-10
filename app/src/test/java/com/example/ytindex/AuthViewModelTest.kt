package com.example.ytindex

import com.example.ytindex.data.repository.AuthRepository
import com.example.ytindex.ui.auth.AuthViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val authRepository = mockk<AuthRepository>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { authRepository.sessionStatus } returns flowOf(SessionStatus.NotAuthenticated(false))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testValidationFailsForInvalidEmail() = runTest {
        val vm = AuthViewModel(authRepository)
        advanceUntilIdle()

        vm.onEmailChange("invalid-email")
        vm.onPasswordChange("password123")
        vm.submit()

        assertNotNull(vm.state.value.errorMessage)
        assertTrue(vm.state.value.errorMessage?.contains("email", ignoreCase = true) == true)
        assertFalse(vm.state.value.isAuthenticated)
    }

    @Test
    fun testValidationFailsForShortPassword() = runTest {
        val vm = AuthViewModel(authRepository)
        advanceUntilIdle()

        vm.onEmailChange("user@test.com")
        vm.onPasswordChange("123")
        vm.submit()

        assertNotNull(vm.state.value.errorMessage)
        assertTrue(vm.state.value.errorMessage?.contains("minimal", ignoreCase = true) == true)
        assertFalse(vm.state.value.isAuthenticated)
    }

    @Test
    fun testSuccessfulSignIn() = runTest {
        coEvery { authRepository.signIn("user@test.com", "secret123") } returns Result.success(Unit)

        val vm = AuthViewModel(authRepository)
        advanceUntilIdle()

        vm.onEmailChange("user@test.com")
        vm.onPasswordChange("secret123")
        vm.submit()
        advanceUntilIdle()

        assertFalse(vm.state.value.isLoading)
        assertTrue(vm.state.value.isAuthenticated)
        assertEquals(null, vm.state.value.errorMessage)
    }

    @Test
    fun testSignInFailureMapsToFriendlyMessage() = runTest {
        coEvery {
            authRepository.signIn("user@test.com", "wrongpass")
        } returns Result.failure(Exception("Invalid login credentials"))

        val vm = AuthViewModel(authRepository)
        advanceUntilIdle()

        vm.onEmailChange("user@test.com")
        vm.onPasswordChange("wrongpass")
        vm.submit()
        advanceUntilIdle()

        assertFalse(vm.state.value.isLoading)
        assertFalse(vm.state.value.isAuthenticated)
        assertTrue(vm.state.value.errorMessage?.contains("salah", ignoreCase = true) == true)
    }
}
