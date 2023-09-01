package com.salmakhd.android.fibo.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.filter

/* How to create a state for a composable that contains logic */

// create a state for a composable
class EditableUserInputState(private val hint: String, initialText: String) {
    var text by mutableStateOf(initialText)
        private set
    fun updateText(newText: String) {
        text = newText
    }

    val isHint: Boolean
        get() = text == hint

    companion object {
        val Saver: Saver<EditableUserInputState, *> = listSaver(
            save = { listOf(it.hint, it.text) },
            restore = {
                EditableUserInputState(
                    hint = it[0],
                    initialText = it[1],
                )
            }
        )
    }
}
@Composable
fun rememberEditableUserInputState(hint: String): EditableUserInputState {
    return rememberSaveable (hint) {
        EditableUserInputState(hint, hint)
    }
}

@Composable
fun CustomField(
    editableUserInputState: EditableUserInputState,
    onToDestinationChanged: Boolean
) {
    // this will ensure that the latest value of onToDestinationChanged is always used in a big lambda
    // like LaunchedEffect
    val currentOnDestinationChanged by rememberUpdatedState(onToDestinationChanged)

    LaunchedEffect(editableUserInputState) {
        // snapshotFlow -> converts a State to a flow
        snapshotFlow { editableUserInputState.text }
            .filter { !editableUserInputState.isHint }
            .collect {
                viewModel.currentOnDestinationChanged(editableUserInputState.text)
            }
    }
}

/*
// another example
@Composable
fun CraneEditableUserInput(
    state: EditableUserInputState = rememberEditableUserInputState("")
            caption: String? = null,
    @DrawableRes vectorImageId: Int? = null,
) {
    // TODO Codelab: Encapsulate this state in a state holder
    var textState by remember { mutableStateOf(hint) }
    val isHint = { textState == hint }

    CraneBaseUserInput(
        caption = caption,
        tintIcon = { !state.isHint() },
        showCaption = { !state.isHint() },
        vectorImageId = vectorImageId
    ) {
        BasicTextField(
            value = state.text,
            onValueChange = { state.updateText(it) },
            textStyle = if (state.isHint) {
                captionTextStyle.copy(color = LocalContentColor.current)
            } else {
                MaterialTheme.typography.body1.copy(color = LocalContentColor.current)
            },
            cursorBrush = SolidColor(LocalContentColor.current)
        )
    }
}
*/

/*
@Composable
fun ToDestinationUserInput(onToDestinationChanged: (String) -> Unit) {
    val editableUserInputState = rememberEditableUserInputState(hint = "Choose Destination")
    CraneEditableUserInput(
        state = editableUserInputState,
        caption = "To",
        vectorImageId = R.drawable.ic_plane,
    )

    // this will ensure that the latest value of onToDestinationChanged is always used in a big lambda
        // like LaunchedEffect
    val currentOnDestinationChanged by rememberUpdatedState(onToDestinationChanged)
    LaunchedEffect(editableUserInputState) {
        // snapshotFlow -> converts a State to a flow
        snapshotFlow { editableUserInputState.text }
            .filter { !editableUserInputState.isHint }
            .collect {
                currentOnDestinationChanged(editableUserInputState.text)
            }
    }
}
 */