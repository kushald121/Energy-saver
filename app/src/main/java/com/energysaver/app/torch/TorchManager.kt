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
    
    fun enableTorch(): Boolean {
        android.util.Log.d("TorchManager", "enableTorch called")
        val cameraManager = cameraManager
        val cameraId = cameraId
        
        if (cameraManager == null) {
            android.util.Log.e("TorchManager", "CameraManager is null")
            return false
        }
        
        if (cameraId == null) {
            android.util.Log.e("TorchManager", "CameraId is null - no camera with flash found")
            return false
        }
        
        return try {
            android.util.Log.d("TorchManager", "Setting torch mode to true for camera $cameraId")
            cameraManager.setTorchMode(cameraId, true)
            isTorchEnabled = true
            android.util.Log.d("TorchManager", "Torch enabled successfully")
            true
        } catch (e: CameraAccessException) {
            android.util.Log.e("TorchManager", "CameraAccessException: ${e.message}", e)
            isTorchEnabled = false
            false
        } catch (e: Exception) {
            android.util.Log.e("TorchManager", "Exception enabling torch: ${e.message}", e)
            isTorchEnabled = false
            false
        }
    }
    
    fun disableTorch(): Boolean {
        android.util.Log.d("TorchManager", "disableTorch called")
        val cameraManager = cameraManager
        val cameraId = cameraId
        
        if (cameraManager == null) {
            android.util.Log.e("TorchManager", "CameraManager is null")
            return false
        }
        
        if (cameraId == null) {
            android.util.Log.e("TorchManager", "CameraId is null - no camera with flash found")
            return false
        }
        
        return try {
            android.util.Log.d("TorchManager", "Setting torch mode to false for camera $cameraId")
            cameraManager.setTorchMode(cameraId, false)
            isTorchEnabled = false
            android.util.Log.d("TorchManager", "Torch disabled successfully")
            true
        } catch (e: CameraAccessException) {
            android.util.Log.e("TorchManager", "CameraAccessException: ${e.message}", e)
            isTorchEnabled = false
            false
        } catch (e: Exception) {
            android.util.Log.e("TorchManager", "Exception disabling torch: ${e.message}", e)
            isTorchEnabled = false
            false
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

