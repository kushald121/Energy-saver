package com.energysaver.app

import android.Manifest
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
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
 * Tests for camera functionality
 */
@RunWith(AndroidJUnit4::class)
class CameraFunctionalityTest {

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.CAMERA
    )

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        // Wait for camera initialization
        Thread.sleep(3000)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun testCameraPreviewIsVisible() {
        onView(withId(R.id.camera_preview))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testCameraPreviewIsEnabled() {
        onView(withId(R.id.camera_preview))
            .check(matches(isEnabled()))
    }

    @Test
    fun testCameraInitializesSuccessfully() {
        // Wait for full initialization
        Thread.sleep(2000)
        
        // Verify no error is shown
        onView(withId(R.id.error_text))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun testCameraPreviewHasCorrectLayout() {
        onView(withId(R.id.camera_preview))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
    }

    @Test
    fun testActivityHandlesCameraLifecycle() {
        // Simulate activity pause and resume
        scenario.moveToState(androidx.lifecycle.Lifecycle.State.STARTED)
        Thread.sleep(500)
        scenario.moveToState(androidx.lifecycle.Lifecycle.State.RESUMED)
        Thread.sleep(500)
        
        // Verify camera preview is still displayed
        onView(withId(R.id.camera_preview))
            .check(matches(isDisplayed()))
    }
}
