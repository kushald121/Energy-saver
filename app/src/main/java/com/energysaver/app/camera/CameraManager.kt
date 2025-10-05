package com.energysaver.app.camera

import android.content.Context
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraManager {
    
    private var cameraProvider: ProcessCameraProvider? = null
    private var camera: Camera? = null
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var cameraExecutor: ExecutorService? = null
    
    private var context: Context? = null
    private var lifecycleOwner: LifecycleOwner? = null
    private var previewView: PreviewView? = null
    
    fun initialize() {
        cameraExecutor = Executors.newSingleThreadExecutor()
    }
    
    fun bindToLifecycle(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        imageAnalyzer: ImageAnalysis
    ) {
        this.context = context
        this.lifecycleOwner = lifecycleOwner
        this.previewView = previewView
        this.imageAnalyzer = imageAnalyzer
        
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()
                startCamera()
            } catch (e: Exception) {
                throw RuntimeException("Failed to initialize camera", e)
            }
        }, ContextCompat.getMainExecutor(context))
    }
    
    private fun startCamera() {
        val cameraProvider = cameraProvider ?: throw IllegalStateException("Camera provider not initialized")
        val lifecycleOwner = lifecycleOwner ?: throw IllegalStateException("Lifecycle owner not set")
        val previewView = previewView ?: throw IllegalStateException("Preview view not set")
        val imageAnalyzer = imageAnalyzer ?: throw IllegalStateException("Image analyzer not set")
        
        // Create preview use case
        preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }
        
        // Create camera selector (back camera only)
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        
        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()
            
            // Bind use cases to camera
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalyzer
            )
            
        } catch (e: Exception) {
            throw RuntimeException("Failed to bind camera use cases", e)
        }
    }
    
    fun stopCamera() {
        cameraProvider?.unbindAll()
        camera = null
    }
    
    fun isCameraAvailable(): Boolean {
        return camera != null
    }
    
    fun getCamera(): Camera? = camera
    
    fun cleanup() {
        stopCamera()
        cameraExecutor?.shutdown()
        cameraExecutor = null
        cameraProvider = null
        context = null
        lifecycleOwner = null
        previewView = null
        preview = null
        imageAnalyzer = null
    }
}

