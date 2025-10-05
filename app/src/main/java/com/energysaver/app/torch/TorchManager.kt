package com.energysaver.app.torch

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import kotlinx.coroutines.delay

class TorchManager {
    
    private var cameraManager: CameraManager? = null
    private var cameraId: String? = null
    private var isTorchEnabled = false
    private var context: Context? = null
    
    fun initialize() {
        // Context will be set when needed
    }
    
    fun setContext(context: Context) {
        this.context = context
        this.cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        
        // Find the back camera with flash
        cameraId = findCameraWithFlash()
    }
    
    private fun findCameraWithFlash(): String? {
        val cameraManager = cameraManager ?: return null
        
        return try {
            for (id in cameraManager.cameraIdList) {
                val characteristics = cameraManager.getCameraCharacteristics(id)
                val flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
                val lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)
                
                if (flashAvailable == true && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                    return id
                }
            }
            null
        } catch (e: CameraAccessException) {
            null
        }
    }
    
    fun isTorchAvailable(): Boolean {
        return cameraId != null
    }
    
    fun enableTorch() {
        val cameraManager = cameraManager ?: throw IllegalStateException("Camera manager not initialized")
        val cameraId = cameraId ?: throw IllegalStateException("No camera with flash found")
        
        try {
            cameraManager.setTorchMode(cameraId, true)
            isTorchEnabled = true
        } catch (e: CameraAccessException) {
            throw RuntimeException("Failed to enable torch", e)
        }
    }
    
    fun disableTorch() {
        val cameraManager = cameraManager ?: throw IllegalStateException("Camera manager not initialized")
        val cameraId = cameraId ?: throw IllegalStateException("No camera with flash found")
        
        try {
            cameraManager.setTorchMode(cameraId, false)
            isTorchEnabled = false
        } catch (e: CameraAccessException) {
            throw RuntimeException("Failed to disable torch", e)
        }
    }
    
    fun toggleTorch() {
        if (isTorchEnabled) {
            disableTorch()
        } else {
            enableTorch()
        }
    }
    
    fun isTorchEnabled(): Boolean = isTorchEnabled
    
    fun getTorchState(): TorchState {
        return when {
            !isTorchAvailable() -> TorchState.UNAVAILABLE
            isTorchEnabled -> TorchState.ON
            else -> TorchState.OFF
        }
    }
    
    enum class TorchState {
        ON, OFF, UNAVAILABLE
    }
    
    fun cleanup() {
        if (isTorchEnabled) {
            try {
                disableTorch()
            } catch (e: Exception) {
                // Ignore cleanup errors
            }
        }
        cameraManager = null
        cameraId = null
        context = null
    }
}

