package com.energysaver.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.energysaver.app.databinding.ActivityMainBinding
import com.energysaver.app.ui.MainViewModel
import com.energysaver.app.utils.PermissionManager
import com.energysaver.app.status.StatusManager

class MainActivity : AppCompatActivity() {
    
    private var binding: ActivityMainBinding? = null
    private var viewModel: MainViewModel? = null
    private var permissionManager: PermissionManager? = null
    private var statusManager: StatusManager? = null
    private var isInitialized = false
    
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        try {
            val allGranted = permissions.values.all { it }
            if (allGranted) {
                initializeApp()
            } else {
                showPermissionDeniedMessage()
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Permission result error", e)
            showError("Permission handling failed")
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            // Set up basic UI first
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding?.root)
            
            // Show initial UI state
            binding?.apply {
                toggleButton.isEnabled = false
                loadingProgress.visibility = android.view.View.VISIBLE
                detectionStatusText.text = "Initializing..."
                torchStatusText.text = "Torch: OFF"
            }
            
            // Initialize components safely
            initializeComponents()
            
            // Request permissions
            requestPermissions()
            
        } catch (e: Exception) {
            Log.e("MainActivity", "onCreate error", e)
            // Even on error, try to show something
            try {
                Toast.makeText(this, "App initialization error: ${e.message}", Toast.LENGTH_LONG).show()
            } catch (toastError: Exception) {
                Log.e("MainActivity", "Cannot show toast", toastError)
            }
        }
    }
    
    private fun initializeComponents() {
        try {
            viewModel = ViewModelProvider(this)[MainViewModel::class.java]
            permissionManager = PermissionManager(this, permissionLauncher)
            statusManager = StatusManager(this)
            
            binding?.toggleButton?.setOnClickListener {
                try {
                    viewModel?.toggleEnergySaver()
                } catch (e: Exception) {
                    Log.e("MainActivity", "Toggle error", e)
                    showError("Toggle failed: ${e.message}")
                }
            }
            
            // Set up observers
            setupObservers()
            
        } catch (e: Exception) {
            Log.e("MainActivity", "Component initialization error", e)
            showError("Failed to initialize components")
        }
    }
    
    private fun setupObservers() {
        try {
            viewModel?.uiState?.observe(this) { state ->
                try {
                    updateUI(state)
                } catch (e: Exception) {
                    Log.e("MainActivity", "UI update error", e)
                }
            }
            
            viewModel?.errorMessage?.observe(this) { message ->
                try {
                    if (message.isNotEmpty()) {
                        showError(message)
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error message display error", e)
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Observer setup error", e)
        }
    }
    
    private fun requestPermissions() {
        try {
            val requiredPermissions = arrayOf(Manifest.permission.CAMERA)
            
            val allGranted = requiredPermissions.all { permission ->
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
            }
            
            if (allGranted) {
                initializeApp()
            } else {
                permissionLauncher.launch(requiredPermissions)
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Permission request error", e)
            showError("Permission request failed")
            binding?.loadingProgress?.visibility = android.view.View.GONE
        }
    }
    
    private fun initializeApp() {
        if (isInitialized) return
        
        try {
            binding?.loadingProgress?.visibility = android.view.View.VISIBLE
            
            viewModel?.initializeApp(this)
            
            // Try to set up camera, but don't crash if it fails
            try {
                val imageAnalyzer = viewModel?.getObjectDetector()?.createImageAnalyzer()
                if (imageAnalyzer != null) {
                    viewModel?.getCameraManager()?.bindToLifecycle(
                        this,
                        this,
                        binding?.cameraPreview!!,
                        imageAnalyzer
                    )
                }
            } catch (cameraError: Exception) {
                Log.e("MainActivity", "Camera setup error", cameraError)
                showError("Camera failed, but app continues: ${cameraError.message}")
            }
            
            isInitialized = true
            binding?.apply {
                loadingProgress.visibility = android.view.View.GONE
                toggleButton.isEnabled = true
            }
            
        } catch (e: Exception) {
            Log.e("MainActivity", "App initialization error", e)
            showError("Initialization failed: ${e.message}")
            binding?.apply {
                loadingProgress.visibility = android.view.View.GONE
                toggleButton.isEnabled = false
            }
        }
    }
    
    private fun updateUI(state: MainViewModel.UIState) {
        try {
            binding?.apply {
                toggleButton.isEnabled = state.isReady
                loadingProgress.visibility = if (state.isLoading) android.view.View.VISIBLE else android.view.View.GONE
                
                // Update detection status
                detectionStatusText.text = if (state.humanDetected) {
                    getString(R.string.status_detected)
                } else {
                    getString(R.string.status_searching)
                }
                
                // Update torch status
                torchStatusText.text = if (state.torchEnabled) {
                    getString(R.string.status_torch_on)
                } else {
                    getString(R.string.status_torch_off)
                }
                
                // Try animations, but don't crash if they fail
                try {
                    statusManager?.animateDetectionIndicator(detectionIndicator, state.humanDetected)
                    statusManager?.animateTorchIndicator(torchIndicator, state.torchEnabled)
                    statusManager?.animateStatusChange(toggleButton, state.isReady)
                } catch (animError: Exception) {
                    Log.e("MainActivity", "Animation error", animError)
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "updateUI error", e)
        }
    }
    
    private fun showError(message: String) {
        try {
            binding?.apply {
                errorText.text = message
                errorText.visibility = android.view.View.VISIBLE
            }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            Log.e("MainActivity", "Error shown: $message")
        } catch (e: Exception) {
            Log.e("MainActivity", "Cannot show error", e)
        }
    }
    
    private fun showPermissionDeniedMessage() {
        try {
            binding?.apply {
                errorText.text = getString(R.string.permission_denied_message)
                errorText.visibility = android.view.View.VISIBLE
                toggleButton.isEnabled = false
                loadingProgress.visibility = android.view.View.GONE
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Cannot show permission denied message", e)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        try {
            viewModel?.cleanup()
        } catch (e: Exception) {
            Log.e("MainActivity", "Cleanup error", e)
        }
    }
}
