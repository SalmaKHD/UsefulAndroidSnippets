package com.salmakhd.android.forpractice.RequestingPermissionsInCompose

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity(), LifecycleObserver {
    private val persmissionsToRequest =
        arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), 0
        )
        setContent {
            val viewModel = viewModel<PermissionTutorialViewModel>()
            val dialogQueue = viewModel.visiblePermissionDialogQueue

            val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    viewModel.onPermissionResult(
                        Manifest.permission.CAMERA,
                        isGranted = isGranted
                    )
                }
            )

            val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { perms ->
                    persmissionsToRequest.forEach { permission ->
                        viewModel.onPermissionResult(
                            permission = permission,
                            isGranted = perms[permission] == true
                        )
                    }

                }
            )

            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        multiplePermissionResultLauncher.launch(persmissionsToRequest)
                    },
                ) {
                    Text("Request one permission")
                }
                Spacer(modifier = Modifier.height(14.dp))
                dialogQueue
                    .reversed()
                    .forEach { permission ->
                        PermissionDialog(
                            permissionTextProvider =
                            when (permission) {
                                else -> {
                                    object : PermissionTextProvider {
                                        override fun getDescription(isPermanentlyDeclined: Boolean): String {
                                            return if (isPermanentlyDeclined) {
                                                "Go to settings to grant access"

                                            } else {
                                                "You don't have this permission, grant it"
                                            }
                                        }
                                    }
                                }
                            },
                            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                permission
                            ),
                            onDismiss = viewModel::dismissDialog,
                            onOkClick = {
                                viewModel.dismissDialog()
                                arrayOf(permission)
                            },
                            onGotoAppSettingsClicked = ::openAppSettings
                        )
                    }
            }
        }
    }
}
fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null )
    ).also(::startActivity)
}