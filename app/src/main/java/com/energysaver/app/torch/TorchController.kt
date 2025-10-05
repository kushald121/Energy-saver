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
        // Cancel any pending deactivation
        deactivationJob?.cancel()
        
        // Cancel any pending activation and start new one
        activationJob?.cancel()
        activationJob = coroutineScope.launch {
            delay(activationDelayMs)
            try {
                torchManager.enableTorch()
                _torchState.value = TorchState.ON
            } catch (e: Exception) {
                _torchState.value = TorchState.ERROR
            }
        }
    }
    
    fun requestTorchDeactivation() {
        // Cancel any pending activation
        activationJob?.cancel()
        
        // Cancel any pending deactivation and start new one
        deactivationJob?.cancel()
        deactivationJob = coroutineScope.launch {
            delay(deactivationDelayMs)
            try {
                torchManager.disableTorch()
                _torchState.value = TorchState.OFF
            } catch (e: Exception) {
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

