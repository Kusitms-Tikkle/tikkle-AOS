package com.team7.tikkle

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionHelper(private val fragment: Fragment) {
    
    fun requestPermissions(permissions: Array<String>, requestCode: Int) {
        if (!hasPermissions(permissions)) {
            fragment.requestPermissions(permissions, requestCode)
        }
    }
    
    private fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(fragment.requireContext(), it) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray, grantedAction: () -> Unit, deniedAction: () -> Unit) {
        if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            grantedAction()
        } else {
            deniedAction()
        }
    }
}
