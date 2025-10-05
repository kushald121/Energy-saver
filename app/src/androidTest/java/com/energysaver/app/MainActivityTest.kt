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
 * Instrumented test for MainActivity
 * Tests the main UI components and interactions
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.CAMERA
    )

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        // Wait for initialization
        Thread.sleep(2000)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun testActivityLaunches() {
        // Verify activity launches successfully
        onView(withId(R.id.camera_preview))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testCameraPreviewIsDisplayed() {
        // Verify camera preview is visible
        onView(withId(R.id.camera_preview))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
    }

    @Test
    fun testToggleButtonExists() {
        // Verify toggle button is present
        onView(withId(R.id.toggle_button))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.toggle_energy_saver)))
    }

    @Test
    fun testToggleButtonBecomesEnabled() {
        // Wait for initialization
        Thread.sleep(2000)
        
        // Verify button becomes enabled after initialization
        onView(withId(R.id.toggle_button))
            .check(matches(isEnabled()))
    }

    @Test
    fun testDetectionStatusIndicatorExists() {
        // Verify detection status indicator is present
        onView(withId(R.id.detection_indicator))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.detection_status_text))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testTorchStatusIndicatorExists() {
        // Verify torch status indicator is present
        onView(withId(R.id.torch_indicator))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.torch_status_text))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testControlPanelIsDisplayed() {
        // Verify control panel is visible
        onView(withId(R.id.control_panel))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testToggleButtonCanBeClicked() {
        // Wait for button to be enabled
        Thread.sleep(2000)
        
        // Test clicking the toggle button
        onView(withId(R.id.toggle_button))
            .check(matches(isEnabled()))
            .perform(click())
        
        // Verify click was processed (button should still be visible)
        onView(withId(R.id.toggle_button))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testInitialDetectionStatus() {
        // Verify initial detection status shows "searching"
        onView(withId(R.id.detection_status_text))
            .check(matches(withText(R.string.status_searching)))
    }

    @Test
    fun testInitialTorchStatus() {
        // Verify initial torch status shows "off"
        onView(withId(R.id.torch_status_text))
            .check(matches(withText(R.string.status_torch_off)))
    }

    @Test
    fun testLoadingProgressInitiallyHidden() {
        // Wait for initialization
        Thread.sleep(2000)
        
        // Verify loading progress is hidden after initialization
        onView(withId(R.id.loading_progress))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun testErrorTextInitiallyHidden() {
        // Verify error text is hidden initially
        onView(withId(R.id.error_text))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun testAllUIComponentsPresent() {
        // Comprehensive check for all UI components
        onView(withId(R.id.camera_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.control_panel)).check(matches(isDisplayed()))
        onView(withId(R.id.detection_indicator)).check(matches(isDisplayed()))
        onView(withId(R.id.detection_status_text)).check(matches(isDisplayed()))
        onView(withId(R.id.torch_indicator)).check(matches(isDisplayed()))
        onView(withId(R.id.torch_status_text)).check(matches(isDisplayed()))
        onView(withId(R.id.toggle_button)).check(matches(isDisplayed()))
    }

    @Test
    fun testToggleButtonDoubleTap() {
        // Wait for initialization
        Thread.sleep(2000)
        
        // Test double-clicking the toggle button
        onView(withId(R.id.toggle_button))
            .perform(click())
            .perform(click())
        
        // Verify app doesn't crash and button is still functional
        onView(withId(R.id.toggle_button))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
    }

    @Test
    fun testUILayoutStructure() {
        // Verify the layout hierarchy is correct
        onView(withId(R.id.camera_preview))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.control_panel))
            .check(matches(isDisplayed()))
            .check(matches(isCompletelyDisplayed()))
    }
}
