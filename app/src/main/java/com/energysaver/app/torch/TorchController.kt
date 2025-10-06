package com.energysaver.app.torch

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TorchController(
    private val torchManager: TorchManager,
    private val activationDelayMs: Long = 200L,
    private val deactivationDelayMs: Long = 1000L,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) {
    
    private val _torchState = MutableStateFlow(TorchState.OFF)
    val torchState: StateFlow<TorchState> = _torchState.asStateFlow()
    
    private var activationJob: Job? = null
    private var deactivationJob: Job? = null
    
    fun requestTorchActivation() {
        android.util.Log.d("TorchController", "requestTorchActivation called")
        // Cancel any pending deactivation
        deactivationJob?.cancel()
        
        // Cancel any pending activation and start new one
        activationJob?.cancel()
        activationJob = coroutineScope.launch {
            android.util.Log.d("TorchController", "Waiting ${activationDelayMs}ms before activating torch")
            delay(activationDelayMs)
            try {
                android.util.Log.d("TorchController", "Calling torchManager.enableTorch()")
                val success = torchManager.enableTorch()
                android.util.Log.d("TorchController", "enableTorch returned: $success")
                _torchState.value = if (success) TorchState.ON else TorchState.ERROR
            } catch (e: Exception) {
                android.util.Log.e("TorchController", "Failed to enable torch", e)
                _torchState.value = TorchState.ERROR
            }
        }
    }
    
    fun requestTorchDeactivation() {
        android.util.Log.d("TorchController", "requestTorchDeactivation called")
        // Cancel any pending activation
        activationJob?.cancel()
        
        // Cancel any pending deactivation and start new one
        deactivationJob?.cancel()
        deactivationJob = coroutineScope.launch {
            android.util.Log.d("TorchController", "Waiting ${deactivationDelayMs}ms before deactivating torch")
            delay(deactivationDelayMs)
            try {
                android.util.Log.d("TorchController", "Calling torchManager.disableTorch()")
                val success = torchManager.disableTorch()
                android.util.Log.d("TorchController", "disableTorch returned: $success")
                _torchState.value = if (success) TorchState.OFF else TorchState.ERROR
            } catch (e: Exception) {
                android.util.Log.e("TorchController", "Failed to disable torch", e)
                _torchState.value = TorchState.ERROR
            }
        }
    }
    
    fun isTorchOn(): Boolean = _torchState.value == TorchState.ON
    
    fun cleanup() {
        activationJob?.cancel()
        deactivationJob?.cancel()
        coroutineScope.cancel()
    }
    
    enum class TorchState {
        ON, OFF, ERROR
    }
}

