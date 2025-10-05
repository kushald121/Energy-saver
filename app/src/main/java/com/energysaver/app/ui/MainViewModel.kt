package com.energysaver.app.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.energysaver.app.camera.CameraManager
import com.energysaver.app.detection.ObjectDetector
import com.energysaver.app.torch.TorchManager
import com.energysaver.app.torch.TorchController
import com.energysaver.app.utils.DetectionDebouncer
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel : ViewModel() {
    
    private val cameraManager = CameraManager()
    private val objectDetector = ObjectDetector()
    private val torchManager = TorchManager()
    private lateinit var torchController: TorchController
    private lateinit var detectionDebouncer: DetectionDebouncer
    
    private val _uiState = MutableLiveData(UIState())
    val uiState: LiveData<UIState> = _uiState
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    private var isEnergySaverEnabled = false
    private var isInitialized = false
    
    data class UIState(
        val isLoading: Boolean = true,
        val isReady: Boolean = false,
        val humanDetected: Boolean = false,
        val torchEnabled: Boolean = false,
        val error: String? = null
    )
    
    fun initializeApp(context: Context) {
        if (isInitialized) return
        
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value?.copy(isLoading = true)
                
                // Initialize components
                cameraManager.initialize()
                objectDetector.initialize()
                torchManager.initialize()
                torchManager.setContext(context)
                
                // Initialize controllers
                torchController = TorchController(torchManager)
                detectionDebouncer = DetectionDebouncer(
                    debounceDelayMs = 500L,
                    coroutineScope = viewModelScope
                )
                
                // Set up detection callback
                objectDetector.setOnDetectionCallback { detected ->
                    detectionDebouncer.updateDetection(detected)
                }
                
                // Observe debounced detection
                detectionDebouncer.debouncedDetection
                    .onEach { detected ->
                        _uiState.value = _uiState.value?.copy(humanDetected = detected)
                        if (isEnergySaverEnabled) {
                            updateTorchState(detected)
                        }
                    }
                    .launchIn(viewModelScope)
                
                // Observe torch state
                torchController.torchState
                    .onEach { state ->
                        _uiState.value = _uiState.value?.copy(
                            torchEnabled = state == TorchController.TorchState.ON
                        )
                    }
                    .launchIn(viewModelScope)
                
                isInitialized = true
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    isReady = true
                )
                
            } catch (e: Exception) {
                _errorMessage.value = "Initialization failed: ${e.message}"
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    isReady = false,
                    error = e.message
                )
            }
        }
    }
    
    fun getCameraManager(): CameraManager = cameraManager
    fun getObjectDetector(): ObjectDetector = objectDetector
    
    fun toggleEnergySaver() {
        isEnergySaverEnabled = !isEnergySaverEnabled
        
        if (isEnergySaverEnabled) {
            // Start detection and control torch based on detection
            objectDetector.startDetection()
            updateTorchState(_uiState.value?.humanDetected ?: false)
        } else {
            // Stop detection and turn off torch
            objectDetector.stopDetection()
            torchManager.disableTorch()
            _uiState.value = _uiState.value?.copy(torchEnabled = false)
        }
    }
    
    private fun updateTorchState(humanDetected: Boolean) {
        if (humanDetected) {
            torchController.requestTorchActivation()
        } else {
            torchController.requestTorchDeactivation()
        }
    }
    
    fun cleanup() {
        cameraManager.cleanup()
        objectDetector.cleanup()
        torchManager.cleanup()
        if (::torchController.isInitialized) {
            torchController.cleanup()
        }
        if (::detectionDebouncer.isInitialized) {
            detectionDebouncer.cleanup()
        }
    }
}
