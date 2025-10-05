package com.energysaver.app.utils

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetectionDebouncer(
    private val debounceDelayMs: Long = 500L,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) {
    
    private val _debouncedDetection = MutableStateFlow(false)
    val debouncedDetection: StateFlow<Boolean> = _debouncedDetection.asStateFlow()
    
    private var detectionJob: Job? = null
    private var lastDetectionValue = false
    
    fun updateDetection(detected: Boolean) {
        lastDetectionValue = detected
        
        // Cancel previous job
        detectionJob?.cancel()
        
        // Start new debounced job
        detectionJob = coroutineScope.launch {
            delay(debounceDelayMs)
            _debouncedDetection.value = detected
        }
    }
    
    fun getCurrentValue(): Boolean = lastDetectionValue
    
    fun cleanup() {
        detectionJob?.cancel()
        coroutineScope.cancel()
    }
}

