package com.energysaver.app

import android.Manifest
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Integration tests for complete app functionality
 */
@RunWith(AndroidJUnit4::class)
class IntegrationTest {

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.CAMERA
    )

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        Thread.sleep(3000)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun testCompleteUserFlow() {
        // 1. Verify initial state
        onView(withId(R.id.camera_preview))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.toggle_button))
            .check(matches(isEnabled()))
        
        // 2. Enable energy saver
        onView(withId(R.id.toggle_button))
            .perform(click())
        
        Thread.sleep(2000)
        
        // 3. Verify system is active
        onView(withId(R.id.detection_status_text))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.torch_status_text))
            .check(matches(isDisplayed()))
        
        // 4. Wait for detection to work
        Thread.sleep(3000)
        
        // 5. Disable energy saver
        onView(withId(R.id.toggle_button))
            .perform(click())
        
        Thread.sleep(1000)
        
        // 6. Verify torch is off
        onView(withId(R.id.torch_status_text))
            .check(matches(withText(R.string.status_torch_off)))
    }

    @Test
    fun testAppDoesNotCrashOnMultipleInteractions() {
        // Rapid toggling
        for (i in 1..5) {
            onView(withId(R.id.toggle_button))
                .perform(click())
            Thread.sleep(300)
        }
        
        // Verify app is still responsive
        onView(withId(R.id.camera_preview))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.toggle_button))
            .check(matches(isEnabled()))
    }

    @Test
    fun testAppHandlesLifecycleCorrectly() {
        // Enable energy saver
        onView(withId(R.id.toggle_button))
            .perform(click())
        
        Thread.sleep(1000)
        
        // Simulate lifecycle changes
        scenario.moveToState(androidx.lifecycle.Lifecycle.State.STARTED)
        Thread.sleep(500)
        scenario.moveToState(androidx.lifecycle.Lifecycle.State.RESUMED)
        Thread.sleep(500)
        
        // Verify app recovers properly
        onView(withId(R.id.camera_preview))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.toggle_button))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testAllComponentsWorkTogether() {
        // Verify camera is working
        onView(withId(R.id.camera_preview))
            .check(matches(isDisplayed()))
        
        // Verify detection system is initialized
        onView(withId(R.id.detection_indicator))
            .check(matches(isDisplayed()))
        
        // Verify torch system is initialized
        onView(withId(R.id.torch_indicator))
            .check(matches(isDisplayed()))
        
        // Verify control button works
        onView(withId(R.id.toggle_button))
            .check(matches(isEnabled()))
            .perform(click())
        
        Thread.sleep(2000)
        
        // Verify all systems respond
        onView(withId(R.id.detection_status_text))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.torch_status_text))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testErrorHandlingDoesNotCrashApp() {
        // Enable and disable rapidly to stress test
        for (i in 1..10) {
            onView(withId(R.id.toggle_button))
                .perform(click())
            Thread.sleep(100)
        }
        
        // Wait for system to stabilize
        Thread.sleep(2000)
        
        // Verify app is still functional
        onView(withId(R.id.camera_preview))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.toggle_button))
            .check(matches(isEnabled()))
        
        onView(withId(R.id.error_text))
            .check(matches(isDisplayed())) // Error text might be visible or gone
    }

    @Test
    fun testLongRunningSession() {
        // Enable energy saver
        onView(withId(R.id.toggle_button))
            .perform(click())
        
        // Run for extended period
        for (i in 1..10) {
            Thread.sleep(1000)
            
            // Verify systems are still responsive
            onView(withId(R.id.detection_status_text))
                .check(matches(isDisplayed()))
            
            onView(withId(R.id.torch_status_text))
                .check(matches(isDisplayed()))
        }
        
        // Disable and verify cleanup
        onView(withId(R.id.toggle_button))
            .perform(click())
        
        Thread.sleep(1000)
        
        onView(withId(R.id.torch_status_text))
            .check(matches(withText(R.string.status_torch_off)))
    }
}
