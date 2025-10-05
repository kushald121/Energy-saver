package com.energysaver.app

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for permission handling
 */
@RunWith(AndroidJUnit4::class)
class PermissionTest {

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.CAMERA
    )

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        Thread.sleep(1000)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun testCameraPermissionGranted() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val permission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        assertEquals(
            "Camera permission should be granted",
            PackageManager.PERMISSION_GRANTED,
            permission
        )
    }

    @Test
    fun testFlashFeatureAvailable() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val hasFlash = context.packageManager.hasSystemFeature(
            android.content.pm.PackageManager.FEATURE_CAMERA_FLASH
        )
        assertTrue("Device should have camera flash", hasFlash)
    }

    @Test
    fun testAppHasRequiredPermissions() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        
        val cameraPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        assertTrue(
            "Camera permission should be granted",
            cameraPermission == PackageManager.PERMISSION_GRANTED
        )
    }

    @Test
    fun testDeviceHasCameraFeature() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val hasCamera = context.packageManager.hasSystemFeature(
            android.content.pm.PackageManager.FEATURE_CAMERA_ANY
        )
        assertTrue("Device should have camera", hasCamera)
    }

    @Test
    fun testDeviceHasFlashlightFeature() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val hasFlash = context.packageManager.hasSystemFeature(
            android.content.pm.PackageManager.FEATURE_CAMERA_FLASH
        )
        assertTrue("Device should have flashlight", hasFlash)
    }
}
