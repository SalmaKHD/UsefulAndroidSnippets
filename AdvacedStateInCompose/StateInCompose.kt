package com.salmakhd.android.fibo.di

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class StateInCompose {

}

@Composable
fun CustomLazyList(
    modifier: Modifier = Modifier,
    triggerRecompose: Int = 0,
    timeOut: Long = 2000L,
) {
    /*
    side effect #1: LaunchedEffect
    notes:
    - LaunchedEffect launches a coroutine whose lifecycle is tied to the lifecycle of the parent composable
    - LaunchedEffect block can be run only once via passing Unit/true as a parameter or whenever the value of its keys change.

     */
    LaunchedEffect(triggerRecompose) {
        // you're within a coroutine now
    }
    LaunchedEffect(key1 = Unit) {
        // this will be executed only once
    }

    /*
    side effect #2: rememberUpdatedState
    notes:
    - rememberUpdatedState is useful for cases where a long block of code like a LaunchedEffect block 
    is using a value that may be passed as a parameter to a composable, but we want to make sure that the
    value that it accesses is the most updated one.
     */
    val value by rememberUpdatedState(newValue = timeOut)
    LaunchedEffect(key1 = Unit) {
        delay(10000)
        // what happens if timeOut changes while the delay is being executed? well, by using rememberUpdatedState(),
        // the most updated value will be used in this line
        print(value)
    }

    /*
    side effect #3: rememberCoroutineScope
    This is very similar to LaunchedEffect(), but its lifecycle is tied to the point at which it's called and the time
    when the composable leaved the composition. It can also be called from outside a @Composable block making it a perfect
    choice for launching coroutines from within scopes outside the Composable from within a Composable

     */
    val scope = rememberCoroutineScope()
    Button(
        onClick = {
            scope.launch {
                // do some asynchronous work
                // will be canceled automatically when the composable leaves the composition
            }
        }
    ) {}

    /*
    side effect #4: produceState()
    notes: produceState() is low-level function that allows us to create and emit values which will trigger a recomposition.
    - collectAsStateWitLifecycle() uses this API to turn a Flow into a State
     */

// used to push values to a life-cycle aware State
    // fact: collectAsStateWithLifecycle() uses produceState() under the hood
    val uiState by produceState(initialValue = UiState.Loading)

        /*
        side effect #4: snapShotFlow
        notes:
        snapShotFlow() can be used for converting a State into a Flow to be used outside a composable

         */

    val filedState by remember { mutableStateOf(true)}
    LaunchedEffect(Unit) {
        // snapshotFlow -> converts a State to a flow
        snapshotFlow { filedState }
            .filter {filedState } // filedState is now a Flow
            .collect {
                viewModel.currentOnDestinationChanged(editableUserInputState.text)
            }
    }

    /*
    derivedStateOf()
    notes: it is used for creating a state that depends on another state: it will trigger a recomposition when the
    value of the State it is associated with changes.
     */
    // example: item in a list
    val listState = rememberLazyListState()
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    /*
    DisposableEffect {
    }
    notes:
    - it's called right before the composable leaves the composition
    - the dispose{} block must be the last statement in the block.
     */
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
}

sealed interface UiState {
    object Loading: UiState
    object Idle: UiState
}