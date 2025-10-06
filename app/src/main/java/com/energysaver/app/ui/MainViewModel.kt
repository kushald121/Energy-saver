package com.energysaver.app.ui

import android.content.Context
import android.util.Log
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
    
    companion object {
        private const val TAG = "MainViewModel"
    }
    
    private var cameraManager: CameraManager? = null
    private var objectDetector: ObjectDetector? = null
    private var torchManager: TorchManager? = null
    private var torchController: TorchController? = null
    private var detectionDebouncer: DetectionDebouncer? = null
    
    private val _uiState = MutableLiveData(UIState())
    val uiState: LiveData<UIState> = _uiState
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    private var isEnergySaverEnabled = false
    private var isInitialized = false
    
    data class UIState(
        val isLoading: Boolean = false,
        val isReady: Boolean = false,
        val humanDetected: Boolean = false,
        val torchEnabled: Boolean = false,
        val error: String? = null
    )
    
    fun initializeApp(context: Context) {
        if (isInitialized) {
            Log.d(TAG, "Already initialized, skipping")
            return
        }
        
        Log.d(TAG, "Starting initialization")
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value?.copy(isLoading = true)
                
                // Initialize components with error handling
                Log.d(TAG, "Initializing CameraManager")
                cameraManager = CameraManager().apply {
                    try {
                        initialize()
                    } catch (e: Exception) {
                        Log.e(TAG, "CameraManager initialization failed", e)
                        throw e
                    }
                }
                
                Log.d(TAG, "Initializing ObjectDetector")
                objectDetector = ObjectDetector().apply {
                    try {
                        initialize()
                    } catch (e: Exception) {
                        Log.e(TAG, "ObjectDetector initialization failed", e)
                        throw e
                    }
                }
                
                Log.d(TAG, "Initializing TorchManager")
                torchManager = TorchManager().apply {
                    try {
                        initialize()
                        setContext(context)
                    } catch (e: Exception) {
                        Log.e(TAG, "TorchManager initialization failed", e)
                        throw e
                    }
                }
                
                // Initialize controllers
                Log.d(TAG, "Initializing TorchController")
                torchController = torchManager?.let { TorchController(it) }
                
                Log.d(TAG, "Initializing DetectionDebouncer")
                detectionDebouncer = DetectionDebouncer(
                    debounceDelayMs = 500L,
                    coroutineScope = viewModelScope
                )
                
                // Set up detection callback
                objectDetector?.setOnDetectionCallback { detected ->
                    detectionDebouncer?.updateDetection(detected)
                }
                
                // Observe debounced detection
                detectionDebouncer?.debouncedDetection
                    ?.onEach { detected ->
                        _uiState.value = _uiState.value?.copy(humanDetected = detected)
                        if (isEnergySaverEnabled) {
                            updateTorchState(detected)
                        }
                    }
                    ?.launchIn(viewModelScope)
                
                // Observe torch state
                torchController?.torchState
                    ?.onEach { state ->
                        _uiState.value = _uiState.value?.copy(
                            torchEnabled = state == TorchController.TorchState.ON
                        )
                    }
                    ?.launchIn(viewModelScope)
                
                isInitialized = true
                Log.d(TAG, "Initialization completed successfully")
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    isReady = true,
                    error = null
                )
                
            } catch (e: Exception) {
                Log.e(TAG, "Initialization failed", e)
                _errorMessage.value = "Failed to initialize: ${e.message}"
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    isReady = false,
                    error = "Initialization failed: ${e.message}"
                )
            }
        }
    }
    
    fun getCameraManager(): CameraManager? = cameraManager
    fun getObjectDetector(): ObjectDetector? = objectDetector
    
    fun toggleEnergySaver() {
        if (!isInitialized || objectDetector == null || torchManager == null) {
            Log.e(TAG, "Cannot toggle energy saver - not initialized")
            _errorMessage.value = "System not ready. Please wait for initialization."
            return
        }
        
        isEnergySaverEnabled = !isEnergySaverEnabled
        Log.d(TAG, "Energy saver toggled: $isEnergySaverEnabled")
        
        if (isEnergySaverEnabled) {
            // Start detection and control torch based on detection
            objectDetector?.startDetection()
            updateTorchState(_uiState.value?.humanDetected ?: false)
        } else {
            // Stop detection and turn off torch
            objectDetector?.stopDetection()
            torchManager?.disableTorch()
            _uiState.value = _uiState.value?.copy(torchEnabled = false)
        }
    }
    
    private fun updateTorchState(humanDetected: Boolean) {
        torchController?.let { controller ->
            if (humanDetected) {
                controller.requestTorchActivation()
            } else {
                controller.requestTorchDeactivation()
            }
        }
    }
    
    fun cleanup() {
        Log.d(TAG, "Cleaning up resources")
        cameraManager?.cleanup()
        objectDetector?.cleanup()
        torchManager?.cleanup()
        torchController?.cleanup()
        detectionDebouncer?.cleanup()
    }
}
