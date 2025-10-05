package com.energysaver.app.status

import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.core.content.ContextCompat
import com.energysaver.app.R

class StatusManager(private val context: Context) {
    
    fun animateStatusChange(view: View, isActive: Boolean) {
        val animation = if (isActive) {
            createActivationAnimation()
        } else {
            createDeactivationAnimation()
        }
        
        view.startAnimation(animation)
    }
    
    fun animateDetectionIndicator(view: View, detected: Boolean) {
        val color = if (detected) {
            ContextCompat.getColor(context, R.color.status_detected)
        } else {
            ContextCompat.getColor(context, R.color.status_searching)
        }
        
        view.setBackgroundTintList(ContextCompat.getColorStateList(context, color))
        
        // Add a subtle pulse animation
        val pulseAnimation = ScaleAnimation(
            1.0f, 1.2f, 1.0f, 1.2f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 200
            repeatCount = 1
            repeatMode = Animation.REVERSE
        }
        
        view.startAnimation(pulseAnimation)
    }
    
    fun animateTorchIndicator(view: View, torchOn: Boolean) {
        val color = if (torchOn) {
            ContextCompat.getColor(context, R.color.torch_on)
        } else {
            ContextCompat.getColor(context, R.color.torch_off)
        }
        
        view.setBackgroundTintList(ContextCompat.getColorStateList(context, color))
        
        if (torchOn) {
            // Add a glow effect for torch on
            val glowAnimation = AlphaAnimation(0.5f, 1.0f).apply {
                duration = 300
                repeatCount = 2
                repeatMode = Animation.REVERSE
            }
            view.startAnimation(glowAnimation)
        }
    }
    
    private fun createActivationAnimation(): Animation {
        return ScaleAnimation(
            0.8f, 1.0f, 0.8f, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 200
            fillAfter = true
        }
    }
    
    private fun createDeactivationAnimation(): Animation {
        return AlphaAnimation(1.0f, 0.6f).apply {
            duration = 150
            fillAfter = true
        }
    }
    
    fun getStatusText(detected: Boolean, torchOn: Boolean): String {
        return when {
            detected && torchOn -> "Human detected - Torch ON"
            detected && !torchOn -> "Human detected - Torch turning on..."
            !detected && torchOn -> "No human - Torch turning off..."
            else -> "Searching for humans..."
        }
    }
}

