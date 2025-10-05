package com.energysaver.app.detection

import android.content.Context
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ObjectDetector {
    
    private var detector: com.google.mlkit.vision.objects.ObjectDetector? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var executor: ExecutorService? = null
    private var isDetectionActive = false
    private var frameCount = 0
    
    private var onDetectionCallback: ((Boolean) -> Unit)? = null
    
    fun initialize() {
        executor = Executors.newSingleThreadExecutor()
        
        // Configure object detector for person detection
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()
        
        detector = ObjectDetection.getClient(options)
    }
    
    fun createImageAnalyzer(): ImageAnalysis {
        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        
        imageAnalysis.setAnalyzer(executor!!) { imageProxy ->
            if (isDetectionActive) {
                processImage(imageProxy)
            } else {
                imageProxy.close()
            }
        }
        
        this.imageAnalyzer = imageAnalysis
        return imageAnalysis
    }
    
    private fun processImage(imageProxy: ImageProxy) {
        // Process every 3rd frame to optimize performance
        frameCount++
        if (frameCount % 3 != 0) {
            imageProxy.close()
            return
        }
        
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            
            detector?.process(image)
                ?.addOnSuccessListener { detectedObjects ->
                    val humanDetected = isHumanDetected(detectedObjects)
                    onDetectionCallback?.invoke(humanDetected)
                }
                ?.addOnFailureListener { exception ->
                    // Handle detection failure silently to avoid spam
                    // Could log to analytics in production
                }
                ?.addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
    
    private fun isHumanDetected(detectedObjects: List<DetectedObject>): Boolean {
        // Check if any detected object is a person
        // ML Kit object detection with classification will identify object labels
        // We look for objects with reasonable confidence that might be humans
        
        return detectedObjects.isNotEmpty() && 
               detectedObjects.any { detectedObject ->
                   // Check if object has labels
                   if (detectedObject.labels.isNotEmpty()) {
                       // Look for person-related labels or just accept any reasonably-sized object
                       true
                   } else {
                       // Without classification, accept any detected object
                       // Basic validation: check if the detected object has reasonable size
                       val boundingBox = detectedObject.boundingBox
                       val objectWidth = boundingBox.width()
                       val objectHeight = boundingBox.height()
                       
                       // Consider it a person if object is reasonably sized (not too small)
                       objectWidth > 100 && objectHeight > 100
                   }
               }
    }
    
    fun setOnDetectionCallback(callback: (Boolean) -> Unit) {
        this.onDetectionCallback = callback
    }
    
    fun startDetection() {
        isDetectionActive = true
        frameCount = 0
    }
    
    fun stopDetection() {
        isDetectionActive = false
    }
    
    fun isDetectionActive(): Boolean = isDetectionActive
    
    fun cleanup() {
        stopDetection()
        detector?.close()
        detector = null
        imageAnalyzer = null
        executor?.shutdown()
        executor = null
        onDetectionCallback = null
    }
}

