package garden.ephemeral.audio.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import garden.ephemeral.audio.uimodel.LocalPortRegistry
import garden.ephemeral.audio.uimodel.PortColors
import garden.ephemeral.audio.uimodel.PortRegistry
import java.util.UUID

/**
 * An output port.
 *
 * Can be added to a component to provide a place to output a value via a cables.
 * A cable can take the output value from the state object and set it into an
 * input value at the other end.
 *
 * @param valueState a state object holding the output value.
 * @param T the type of the value.
 */
@Composable
inline fun <reified T : Any> OutputPort(
    valueState: State<T>,
    isConnected: Boolean,
    portColors: PortColors,
    modifier: Modifier = Modifier,
) {
    val portId = remember { UUID.randomUUID() }

    val portRegistry: PortRegistry = LocalPortRegistry.current
    DisposableEffect(portRegistry, valueState) {
        val registration = portRegistry.registerOutputPort(id = portId, type = T::class, valueState = valueState)
        onDispose {
            registration.unregister()
        }
    }

    ConnectionPoint(
        valueType = T::class,
        isConnected = isConnected,
        portColors = portColors,
        modifier = modifier.onGloballyPositioned { coordinates ->
            // TODO: Is this where we should register to get the location?
            // coords.transformFrom(...)
        },
    )
}
