package com.salmakhd.android.forpractice.RequestingPermissionsInCompose

import androidx.lifecycle.ViewModel

class PermissionTutorialViewModel: ViewModel() {
    val visiblePermissionDialogQueue = mutableListOf<String>()

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

}