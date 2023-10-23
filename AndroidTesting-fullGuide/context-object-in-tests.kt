package com.salmakhd.android.forpractice

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith

// a Runner: it runs tests (sequence, assertions, etc.)
@RunWith(AndroidJUnit4::class) // this will allow the right Context object to be generated

class ExampleUnitTest {
    /*
Testing in Android:
getting the Context object in a local test vs. in an instrumented test:
1. in a local test, a simulated environment will be provided with the help of Roboelectric (much faster)
2. in instrumented tests, the actual Context object of the emulator will be created.
 */
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun testRepo_TestMethod_TestResultSuccessful() {
        assertEquals(context.getString(R.string.app_name), "ForPractice") // accessing resources from within unit tests
    }
}