package com.energysaver.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.energysaver.app.R

class PermissionManager(
    private val activity: Activity,
    private val permissionLauncher: ActivityResultLauncher<Array<String>>
) {
    
    fun requestPermissions(
        permissions: Array<String>,
        callback: (Boolean) -> Unit
    ) {
        val permissionsToRequest = permissions.filter { permission ->
            ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
        }
        
        if (permissionsToRequest.isEmpty()) {
            callback(true)
        } else {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }
    
    fun handlePermissionResults(permissions: Map<String, Boolean>) {
        val allGranted = permissions.values.all { it }
        
        if (!allGranted) {
            showPermissionDeniedDialog()
        }
    }
    
    private fun showPermissionDeniedDialog() {
        androidx.appcompat.app.AlertDialog.Builder(activity)
            .setTitle(activity.getString(R.string.permission_denied_title))
            .setMessage(activity.getString(R.string.permission_denied_message))
            .setPositiveButton(activity.getString(R.string.go_to_settings)) { _, _ ->
                openAppSettings()
            }
            .setNegativeButton(activity.getString(R.string.cancel), null)
            .show()
    }
    
    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", activity.packageName, null)
        }
        activity.startActivity(intent)
    }
    
    fun hasRequiredPermissions(): Boolean {
        val requiredPermissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.FLASHLIGHT
        )
        
        return requiredPermissions.all { permission ->
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
}

