package garden.ephemeral.audio.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import garden.ephemeral.audio.uimodel.PortColors
import kotlin.reflect.KClass

@Composable
fun ConnectionPoint(
    valueType: KClass<*>,
    isConnected: Boolean,
    portColors: PortColors,
    modifier: Modifier = Modifier,
) {
    ConnectionPointVisual(
        isConnected = isConnected,
        color = portColors.colorForType(valueType),
        modifier = modifier,
    )
}
