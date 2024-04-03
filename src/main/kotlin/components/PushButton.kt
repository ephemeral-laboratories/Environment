package garden.ephemeral.audio.components

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import garden.ephemeral.audio.model.AudioEnvironmentScope

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AudioEnvironmentScope.PushButton() = StandardComponent("Push button") {
    val isPressed = remember { mutableStateOf(false) }
    AdvertiseBooleanOutputEffect(isPressed)

    Button(
        onClick = {},
        modifier = Modifier
            .onPointerEvent(PointerEventType.Press) { _ -> isPressed.value = true }
            .onPointerEvent(PointerEventType.Release) { _ -> isPressed.value = false },
    ) {
        Text(text = "Beep")
    }
}
