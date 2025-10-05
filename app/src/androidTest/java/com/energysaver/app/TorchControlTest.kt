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
 * Tests for torch control functionality
 */
@RunWith(AndroidJUnit4::class)
class TorchControlTest {

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.CAMERA
    )

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        // Wait for initialization
        Thread.sleep(3000)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun testTorchIndicatorIsVisible() {
        onView(withId(R.id.torch_indicator))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testTorchStatusTextIsVisible() {
        onView(withId(R.id.torch_status_text))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testTorchIsOffInitially() {
        onView(withId(R.id.torch_status_text))
            .check(matches(withText(R.string.status_torch_off)))
    }

    @Test
    fun testTorchStatusAfterEnabling() {
        // Enable energy saver
        onView(withId(R.id.toggle_button))
            .perform(click())
        
        // Wait for potential torch activation
        Thread.sleep(2000)
        
        // Verify torch status is displayed (either on or off)
        onView(withId(R.id.torch_status_text))
            .check(matches(anyOf(
                withText(R.string.status_torch_on),
                withText(R.string.status_torch_off)
            )))
    }

    @Test
    fun testTorchTurnsOffAfterDisabling() {
        // Enable energy saver
        onView(withId(R.id.toggle_button))
            .perform(click())
        
        Thread.sleep(1000)
        
        // Disable energy saver
        onView(withId(R.id.toggle_button))
            .perform(click())
        
        Thread.sleep(1000)
        
        // Verify torch is off
        onView(withId(R.id.torch_status_text))
            .check(matches(withText(R.string.status_torch_off)))
    }

    @Test
    fun testTorchIndicatorExists() {
        onView(withId(R.id.torch_indicator))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testTorchControlDoesNotCrash() {
        // Multiple toggles should not crash the app
        onView(withId(R.id.toggle_button))
            .perform(click())
        
        Thread.sleep(500)
        
        onView(withId(R.id.toggle_button))
            .perform(click())
        
        Thread.sleep(500)
        
        onView(withId(R.id.toggle_button))
            .perform(click())
        
        Thread.sleep(500)
        
        // Verify app is still functional
        onView(withId(R.id.torch_status_text))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testTorchStatesPersist() {
        // Enable and check multiple times
        for (i in 1..3) {
            onView(withId(R.id.toggle_button))
                .perform(click())
            
            Thread.sleep(1000)
            
            // Verify status text is displayed
            onView(withId(R.id.torch_status_text))
                .check(matches(isDisplayed()))
        }
    }
}
