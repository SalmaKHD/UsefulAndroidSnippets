//        requestServicePermissions(
//            Manifest.permission.POST_NOTIFICATIONS,
//            Manifest.permission.ACCESS_COARSE_LOCATION, // the two last permissions must be requested simultaneously
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )


//            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) &&
//                !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
//            ){
//                // show a dialog
//                PermissionDialog(
//                    permissionText = "برای استفاده از نقشه, سرویس لوکیشن را روشن نمایید.",
//                    onGotoAppSettingsClicked = { goToAppSettings() }
//                )
//            }

    // use permissions again if the user has granted course-location-access only (requesting permissions again should suffice)
    private fun Context.requestServicePermissions(vararg permissions: String) {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { isGranted ->
            isGranted.entries.forEach {
                Log.d("StepCounterActivity", "${it.key} = ${it.value}")
            }
        }
        requestPermissionLauncher.launch(permissions.asList().toTypedArray())
    }

        private fun goToAppSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null )
        ).also(::startActivity)
    }

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.isPermanentlyDeclined(): Boolean {
    return !this.status.shouldShowRationale
}

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                permissionState.launchMultiplePermissionRequest()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    )

    var isLocationPermissionGranted by remember {
        mutableStateOf(
            checkPermissionFor(
                context = context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }



    // ask for permission from within composable
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(), onResult = {
            if (it) {
                isLocationPermissionGranted = true
            }
        }
    )

      when {
                                        perm.status.isGranted -> {
                                            // don't do anything
                                        }

                                        perm.status.shouldShowRationale -> {
                                            permissionState.launchMultiplePermissionRequest()
                                        }

                                        !perm.status.shouldShowRationale && perm.status is PermissionStatus.Denied -> {
                                            viewModel.onEvent(PedometerScreenEvent.PermissionDeniedDetected)
                                        }
                                    }
                                    /* ALTERNATIVE
                                    when (perm.permission) {
                                        Manifest.permission.ACCESS_COARSE_LOCATION -> {
                                            when {
                                                perm.status.isGranted -> {
                                                    // don't do anything
                                                }

                                                perm.status.shouldShowRationale -> {
                                                    permissionState.launchMultiplePermissionRequest()
                                                }

                                                !perm.status.shouldShowRationale && perm.status is PermissionStatus.Denied -> {
                                                    viewModel.onEvent(PedometerScreenEvent.PermissionDeniedDetected)
                                                }
                                            }
                                        }
                                     */
                                }
                            }
