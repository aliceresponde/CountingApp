package com.aliceresponde.countingapp.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class MockkableTestBase {

    @get:Rule
    val synchronousRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutinesTestRule()

    val testDispatcher = testCoroutineRule.testDispatcher

    @Before
    open fun before() {
        // Set Coroutine Dispatcher.
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        // Reset Coroutine Dispatcher and Scope.
        testDispatcher.cleanupTestCoroutines()
        testCoroutineRule.testScope.cleanupTestCoroutines()
    }
}