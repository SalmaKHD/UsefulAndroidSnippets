/*
TOPIC OF INVESTIGATION: Drawing in Compose
Reference: [https://www.youtube.com/watch?v=1yiuxWK74vI&list=PLWz5rJ2EKKc9Ty3Zl1hvMVUsXfkn93NRk]
 */
package com.salmakhd.android.forpractice

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun DrawingInCompose() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // with transformations
        inset(horizontal = 100.dp.toPx()) { // adds padding
            rotate(degrees = 360f) { // not noticeable in this example
                // translate
                translate(left = 20.dp.toPx()) {// affects center
                    // scale
                    scale(scaleX = 2.dp.toPx(), scaleY = 2.dp.toPx()) { // affects radius
                        drawCircle(
                            color = Color.White,
                            center = size.center, // use offset
                            radius = 20.dp.toPx()
                        )
                    }
                }
            }
        }

        // combine all transformations
        withTransform( {
            inset(horizontal = 100.dp.value)
            rotate(degrees = 360f)
            translate(left = 20.dp.value)
            scale(scaleX = 2.dp.value, scaleY = 2.dp.value)
        }) {
            drawCircle(
                color = Color.White,
                center = size.center, // use offset
                radius = 20.dp.toPx()
            )
        }

        // without transformations
        drawCircle(
            color = Color.Red.copy(alpha = 0.5f),
            center = size.center, // use offset
            radius = 20.dp.toPx()
        )

    }
}
