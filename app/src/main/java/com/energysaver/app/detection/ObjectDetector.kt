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
                    android.util.Log.d("ObjectDetector", "Detected ${detectedObjects.size} objects")
                    val humanDetected = isHumanDetected(detectedObjects)
                    android.util.Log.d("ObjectDetector", "Human detected: $humanDetected")
                    onDetectionCallback?.invoke(humanDetected)
                }
                ?.addOnFailureListener { exception ->
                    android.util.Log.e("ObjectDetector", "Detection failed", exception)
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
        
        val result = detectedObjects.isNotEmpty() && 
               detectedObjects.any { detectedObject ->
                   // Check if object has labels
                   if (detectedObject.labels.isNotEmpty()) {
                       android.util.Log.d("ObjectDetector", "Object labels: ${detectedObject.labels.joinToString { it.text }}")
                       // Look for person-related labels or just accept any reasonably-sized object
                       true
                   } else {
                       // Without classification, accept any detected object
                       // Basic validation: check if the detected object has reasonable size
                       val boundingBox = detectedObject.boundingBox
                       val objectWidth = boundingBox.width()
                       val objectHeight = boundingBox.height()
                       
                       android.util.Log.d("ObjectDetector", "Object size: ${objectWidth}x${objectHeight}")
                       
                       // Consider it a person if object is reasonably sized (lowered threshold from 100)
                       objectWidth > 50 && objectHeight > 50
                   }
               }
        
        android.util.Log.d("ObjectDetector", "isHumanDetected result: $result")
        return result
    }
    
    fun setOnDetectionCallback(callback: (Boolean) -> Unit) {
        android.util.Log.d("ObjectDetector", "Detection callback set")
        this.onDetectionCallback = callback
    }
    
    fun startDetection() {
        android.util.Log.d("ObjectDetector", "startDetection called")
        isDetectionActive = true
        frameCount = 0
    }
    
    fun stopDetection() {
        android.util.Log.d("ObjectDetector", "stopDetection called")
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

