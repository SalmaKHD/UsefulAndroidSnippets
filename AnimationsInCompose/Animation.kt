/*
Topic of investigation: Animation in Compose
 */
package com.salmakhd.android.forpractice

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun AnimationDemo() {
    var editable by remember { mutableStateOf(true) }
    // save the current state of the animation
    // alternative to editable
    val state = MutableTransitionState(false).apply {
        targetState=true
    }

    val density = LocalDensity.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
    ) {
        AnimatedVisibility(
            visibleState = state, // animation will be played when this value changes
            enter = slideInVertically(
                animationSpec = TweenSpec(durationMillis = 2000)
            ) {
                with(density) {+40.dp.roundToPx() }
            }
                    +
                    expandVertically (
                        expandFrom = Alignment.Bottom,
                        animationSpec = TweenSpec(durationMillis = 2000)
                    ) +
                    fadeIn(
                        initialAlpha = 0.2f,
                        animationSpec = TweenSpec(durationMillis = 2000)
                    ),
            exit = shrinkVertically()
        ) {
            // take action based on the current state of the animation
            /*
            current state? initially == initialState
            then == targetState
             */
            if (!state.currentState) {
                Text(text = "Philippe")
            } else {
                Text(text="Salma")
            }
        }

        // animate individual elements on the screen based on a common state
        val state1 = MutableTransitionState(false).apply {
            targetState = true
        }

        AnimatedVisibility(
            // common animation
            enter = fadeIn(animationSpec = TweenSpec(durationMillis = 2000)),
            visibleState = state1
        ) {
            Column {

                Text(
                    modifier = Modifier
                        .animateEnterExit(
                            enter = expandVertically(
                                animationSpec = TweenSpec(6000),
                                expandFrom = Alignment.Top
                            )
                        ),
                    text = "Hello"
                )
                Text("Phillipe") // not specifying any animations
            }
        }
    }

    var minutes by remember { mutableStateOf(0) }
    AnimatedContent(
        modifier = Modifier.draggable(
            orientation = Orientation.Vertical,
            state = DraggableState {
                minutes++
            }
        ),
        targetState = minutes) { targetState ->
        Text(text = targetState.toString())
    }

    AnimatedContent(
        targetState = minutes,
        transitionSpec = {
            // Compare the incoming number with the previous number.
            if (targetState > initialState) {
                // If the target number is larger, it slides up and fades in
                // while the initial (smaller) number slides up and fades out.
                slideInVertically { height -> height } + fadeIn() with
                        slideOutVertically { height -> -height } + fadeOut()
            } else {
                // If the target number is smaller, it slides down and fades in
                // while the initial number slides down and fades out.
                slideInVertically { height -> -height } + fadeIn() with
                        slideOutVertically { height -> height } + fadeOut()
            }.using(
                // Disable clipping since the faded slide-in/out should
                // be displayed out of bounds.
                SizeTransform(clip = false)
            )
        }
    ) { targetCount ->
        Text(text = "$targetCount")
    }
}