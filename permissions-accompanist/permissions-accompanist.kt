   
const val MAIN_ACTIVITY_TAG = "Main Activity Tag"
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

   setContent {
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
                if(event== Lifecycle.Event.ON_START) {
                    permissionState.launchMultiplePermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
        )

        Column {
            permissionState.permissions.forEach {perm ->
                when (perm.permission) {
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        when {
                            perm.status.isGranted -> {
                                Text(
                                    text = "${perm.permission} permission granted"
                                )
                            }
                            perm.status.shouldShowRationale -> {
                                Text(
                                    text = "${perm.permission} should show rationale"
                                )
                            }
                            perm.isPermanentlyDeclined() -> {
                                Text(
                                    text = "${perm.permission} is permanently declined."
                                )
                            }
                        }
                    }

                }
            }
        }
   }
}
