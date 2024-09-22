package garden.ephemeral.audio.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import garden.ephemeral.audio.uimodel.LocalPortColors
import garden.ephemeral.audio.uimodel.LocalPortRegistry
import garden.ephemeral.audio.uimodel.PortColors
import garden.ephemeral.audio.uimodel.PortRegistry
import java.util.UUID

/**
 * An input port.
 *
 * Can be added to a component to provide a place to input a value via a cables.
 * A cable can set the input value into the state, having retrieved it from something
 * at the other end.
 *
 * @param valueState a state object holding the input value.
 * @param T the type of the value.
 */
@Composable
inline fun <reified T : Any> InputPort(
    valueState: MutableState<T>,
    portColors: PortColors = LocalPortColors.current,
    modifier: Modifier = Modifier,
) {
    val portId = remember { UUID.randomUUID() }

    val portRegistry: PortRegistry = LocalPortRegistry.current
    DisposableEffect(portRegistry, valueState) {
        val registration = portRegistry.registerInputPort(id = portId, type = T::class, valueState = valueState)
        onDispose {
            registration.unregister()
        }
    }

    val isConnected by rememberUpdatedState(portRegistry.isPortConnected(portId))

    ConnectionPoint(
        valueType = T::class,
        isConnected = isConnected,
        portColors = portColors,
        modifier = modifier
            .onGloballyPositioned { location ->
                portRegistry.updatePortLocation(portId, location)
            },
    )
}
