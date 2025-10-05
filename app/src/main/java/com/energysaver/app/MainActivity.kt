package com.energysaver.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
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
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var permissionManager: PermissionManager
    private lateinit var statusManager: StatusManager
    
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionManager.handlePermissionResults(permissions)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initializeComponents()
        setupObservers()
        requestPermissions()
    }
    
    private fun initializeComponents() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        permissionManager = PermissionManager(this, permissionLauncher)
        statusManager = StatusManager(this)
        
        binding.toggleButton.setOnClickListener {
            viewModel.toggleEnergySaver()
        }
    }
    
    private fun setupObservers() {
        viewModel.uiState.observe(this) { state ->
            updateUI(state)
        }
        
        viewModel.errorMessage.observe(this) { message ->
            if (message.isNotEmpty()) {
                showError(message)
            }
        }
    }
    
    private fun requestPermissions() {
        val requiredPermissions = arrayOf(
            Manifest.permission.CAMERA
        )
        
        permissionManager.requestPermissions(requiredPermissions) { granted ->
            if (granted) {
                initializeApp()
            } else {
                showPermissionDeniedMessage()
            }
        }
    }
    
    private fun initializeApp() {
        try {
            viewModel.initializeApp(this)
            
            // Set up camera and detection
            val imageAnalyzer = viewModel.getObjectDetector().createImageAnalyzer()
            viewModel.getCameraManager().bindToLifecycle(
                this,
                this,
                binding.cameraPreview,
                imageAnalyzer
            )
        } catch (e: Exception) {
            showError("Failed to initialize camera: ${e.message}")
            binding.toggleButton.isEnabled = false
        }
    }
    
    private fun updateUI(state: MainViewModel.UIState) {
        binding.toggleButton.isEnabled = state.isReady
        binding.loadingProgress.visibility = if (state.isLoading) android.view.View.VISIBLE else android.view.View.GONE
        
        // Update detection status with animation
        binding.detectionStatusText.text = if (state.humanDetected) {
            getString(R.string.status_detected)
        } else {
            getString(R.string.status_searching)
        }
        statusManager.animateDetectionIndicator(binding.detectionIndicator, state.humanDetected)
        
        // Update torch status with animation
        binding.torchStatusText.text = if (state.torchEnabled) {
            getString(R.string.status_torch_on)
        } else {
            getString(R.string.status_torch_off)
        }
        statusManager.animateTorchIndicator(binding.torchIndicator, state.torchEnabled)
        
        // Animate button state changes
        statusManager.animateStatusChange(binding.toggleButton, state.isReady)
    }
    
    private fun showError(message: String) {
        binding.errorText.text = message
        binding.errorText.visibility = android.view.View.VISIBLE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    private fun showPermissionDeniedMessage() {
        binding.errorText.text = getString(R.string.permission_denied_message)
        binding.errorText.visibility = android.view.View.VISIBLE
        binding.toggleButton.isEnabled = false
    }
    
    override fun onDestroy() {
        super.onDestroy()
        viewModel.cleanup()
    }
}
