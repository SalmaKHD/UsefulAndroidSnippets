package com.salmakhd.android.testing

/**
 * written by @SalmaKHD
 */
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Topics of Investigation:
 * Local tests + Instrumented tests
 */
/*
A quick tutorial on instrumented tests:
1. an instrumented test is used for testing Android frameworks + the UI
2. when conducting instrumented tests, a new environment, application and Context object (instrumentation
object) must be created. -> How? Use a *Runner. This Runner may be JUnitRunner or a HiltRunner. A *Runner
will provide all these components and prepare the environment for testing.
3. How to know whether to use a default runner or a custom Hilt runner?
Well...
if (hilt_for_testing_version < 2.8.8) {
 // create and use a hilt runner
  class MyApplicationRunner: AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
  }

  // declare the new runner in the manifest file
  android {
    defaultConfig {
           // replace testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
           // with
          testInstrumentationRunner "com.example.android.MyApplicationRunner"
    }
   }

 else { use the default android runner specified in the manifest file }

 4. Testing Jetpack compose? add this dependency for getting access to all the methods needed
 for verifying the behavior of Composables
    androidTestImplementation "androidx.compose.ui:ui-test-junit4:$compose_version"

  5. Annotations:
   A -> @SmallTest, @MediumTest, @LargeTest are used for giving clues about how long a test
  should be expected to run for (since instrumented tests are slow in nature)
   B -> @Test lets the compiler know that the function can be executed as a test

  6. See code snippet below for the remaining points.
 Important Side Note: After going through this guide, read the documentation for complete details (be
 careful not to get confused by the HiltRunner section. The documentation may be out-dated)
 */

/*
A quick tutorial on local tests:
1. local tests are executed on the JVM; meaning they are not connected to the emulator by any means.
Any piece of custom code that is not tied to the Android framework must be tested through local tests
for edge cases and potential misbehavior.
2. Should we test ViewModels using unit tests or instrumented tests? If the viewModel injects
dependencies and doesn't use the Context object (you should seriously avoid this anyways),
create Fake repositories and inject them instead in unit tests (or just create an instance of them manually).
3. Unit tests are a lot faster and the recommended mechanism for testing all custom logic added to
your application.
 */


// Code snippet: Instrumented test
// need Hilt injection? add this.
@HiltAndroidTest
// add this if you use a combination of junit3 and 4 (not needed for newer versions)
@RunWith(AndroidJUnit4::class)
class TestingGuide {
    /*
    need to inject with hilt? add this rule
     */
    /*
    important note: you'll need to replace the default AndroidJUnitRunner in build.gradle with 
  
     <testInstrumentationRunner "com.example.android.testing.YourHiltRunner">
     YourHiltRunner implementation:
     
     class YourHiltRunner: AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
    *** if you're not using hilt in your tests, don't change the default runner
     */
    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before // executed before every function annotated with @Test
    fun setUp() {
        // inject dependencies
        hiltTestRule.inject()
    }
    // need access to the Context object? try not to, as it should be used by low-level
    // APIs only, but if it's necessary then add this
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    // need to get access to methods specifically created for compose components?
    // use this
    val composeRule = createComposeRule()

    @Test
    fun testErrorMessage() {

    }

}

/*
Testing Coroutines?
* Most importnat rule to keep in mind about coroutines: A single TestScheduler must be used throughout your tests (no
need to worry about this point if you're using StandardTestDispatcher or UnconfinedTestDispatcher, but not a custom dispatcher)
Some background: All coroutines are treated as tasks that have to be executed (sometimes with intervention) for the code inside coroutines to take effect.
Kotlin makes 3 dispatchers available for this purpose (forget about the actual scheduler, dispatchers and threads):
1. StandardTestDispatcher: all tasks are put in a queue for being executed (not executed automatically)
2. UnconfinedTestDispatcher: all tasks (coroutines) are executed eagerly (not put in a queue, but executed as soon as
we hit the line that contains them)
3. CustomDispatcher: You can alternatively build a custom dispatcher and pass to the runTest() builder. -> be careful
with using a single scheduler. This is not an issue in the previous cases, as Kotlin ensures that a single one is shared
among all coroutines automatically.
What CoroutineBuilder to use?
1. RunBlocking: Can be used for very simple tests where there are no parallel coroutines running together (only a single
coroutine that is executed before test assertions are used typically)
2. RunTest(Dispatcher): This gives you more control over how coroutines are executed (order + if they are executed at all).
For tests that require creating multiple parallel coroutines, use this only.)
* Important Point: Even if you don't launch coroutines manually in tests, all coroutines declared inside the class
that you're testing will adopt the behavior you enforce (like a coroutine initialized in the init{} block of a ViewModel)
 */
@HiltAndroidTest
class UnitTest {
    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    // inject a dependency
    //private lateinit var myRepository: MyRepository

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    @Test
    fun initialStateCorrect() {

    }

    @After // executed after functions annotated with @Test
    fun tearDown() {
        // do clean-up as needed
    }
}