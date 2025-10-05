package com.energysaver.app

import com.energysaver.app.utils.DetectionDebouncer
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Test
import org.junit.Assert.*

class DetectionDebouncerTest {
    
    @Test
    fun testDetectionDebouncerInitialization() = runTest {
        val debouncer = DetectionDebouncer(
            debounceDelayMs = 100L,
            coroutineScope = this
        )
        
        assertNotNull("DetectionDebouncer should be created", debouncer)
        assertEquals("Initial value should be false", false, debouncer.getCurrentValue())
    }
    
    @Test
    fun testDetectionUpdate() = runTest {
        val debouncer = DetectionDebouncer(
            debounceDelayMs = 50L,
            coroutineScope = this
        )
        
        // Test immediate value update
        debouncer.updateDetection(true)
        assertEquals("Current value should be true", true, debouncer.getCurrentValue())
        
        debouncer.updateDetection(false)
        assertEquals("Current value should be false", false, debouncer.getCurrentValue())
    }
}

