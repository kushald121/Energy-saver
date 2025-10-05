package com.energysaver.app

import com.energysaver.app.torch.TorchManager
import org.junit.Test
import org.junit.Assert.*

class TorchManagerTest {
    
    @Test
    fun testTorchManagerInitialization() {
        val torchManager = TorchManager()
        assertNotNull("TorchManager should be created", torchManager)
    }
    
    @Test
    fun testTorchStateEnum() {
        val states = TorchManager.TorchState.values()
        assertEquals("Should have 3 torch states", 3, states.size)
        assertTrue("Should contain ON state", states.contains(TorchManager.TorchState.ON))
        assertTrue("Should contain OFF state", states.contains(TorchManager.TorchState.OFF))
        assertTrue("Should contain UNAVAILABLE state", states.contains(TorchManager.TorchState.UNAVAILABLE))
    }
}

