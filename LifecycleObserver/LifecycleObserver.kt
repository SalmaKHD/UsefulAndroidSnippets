package com.salmakhd.android.fibo.di

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

// How to perform operations based on the state of a lifecycle owner
/*
Operations:
1. add observer
2. remove observer
 */
class LifecycleObserver {

}

// define an observer
private fun getMapLifecycleObserver(mapView: MapView) : LifecycleObserver =
    LifecycleEventObserver { _, event ->
        when(event) {
            Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
            Lifecycle.Event.ON_START -> mapView.onStart()
            Lifecycle.Event.ON_RESUME -> mapView.onResume()
            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            Lifecycle.Event.ON_STOP -> mapView.onStop()
            Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
            else -> throw IllegalStateException()
        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }

    // get the lifecycle owner
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(key1 = lifecycle, key2 = mapView) {
        val lifecycleObserver = getMapLifecycleObserver(mapView)
        // add an observer
        lifecycle.addObserver(lifecycleObserver)
        // must always be the last statement in the block
        onDispose {
            // remove an observer
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
    return mapView
}