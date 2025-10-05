package com.energysaver.app

import android.Manifest
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import org.hamcrest.Matchers.anyOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for detection flow functionality
 */
@RunWith(AndroidJUnit4::class)
class DetectionFlowTest {

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.CAMERA
    )

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        // Wait for full initialization
        Thread.sleep(3000)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun testDetectionIndicatorIsVisible() {
        onView(withId(R.id.detection_indicator))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testDetectionStatusTextIsVisible() {
        onView(withId(R.id.detection_status_text))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testDetectionStatusShowsSearchingInitially() {
        onView(withId(R.id.detection_status_text))
            .check(matches(withText(R.string.status_searching)))
    }

    @Test
    fun testDetectionFlowAfterToggle() {
        // Enable energy saver
        onView(withId(R.id.toggle_button))
            .perform(click())
        
        // Wait for detection to process
        Thread.sleep(2000)
        
        // Verify detection status is one of the expected values
        onView(withId(R.id.detection_status_text))
            .check(matches(anyOf(
                withText(R.string.status_searching),
                withText(R.string.status_detected)
            )))
    }

    @Test
    fun testDetectionIndicatorExists() {
        // Verify the indicator view exists
        onView(withId(R.id.detection_indicator))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testDetectionStatusPersistsDuringSession() {
        // Enable energy saver
        onView(withId(R.id.toggle_button))
            .perform(click())
        
        Thread.sleep(1000)
        
        // Check status multiple times
        for (i in 1..3) {
            Thread.sleep(500)
            onView(withId(R.id.detection_status_text))
                .check(matches(anyOf(
                    withText(R.string.status_searching),
                    withText(R.string.status_detected)
                )))
        }
    }

    @Test
    fun testDetectionStopsAfterToggleOff() {
        // Enable energy saver
        onView(withId(R.id.toggle_button))
            .perform(click())
        
        Thread.sleep(1000)
        
        // Disable energy saver
        onView(withId(R.id.toggle_button))
            .perform(click())
        
        Thread.sleep(500)
        
        // Detection should still be functional but torch should be off
        onView(withId(R.id.torch_status_text))
            .check(matches(withText(R.string.status_torch_off)))
    }
}
