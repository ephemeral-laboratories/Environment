package garden.ephemeral.audio.uimodel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.google.common.flogger.FluentLogger
import garden.ephemeral.audio.components.CableAlpha
import garden.ephemeral.audio.components.CableWidth
import garden.ephemeral.audio.components.ConnectionPointCenter
import garden.ephemeral.audio.model.OctaveFormat
import garden.ephemeral.audio.model.Tuning
import garden.ephemeral.audio.units.Hertz
import garden.ephemeral.audio.units.Hz
import kotlin.reflect.KClass

private val logger: FluentLogger = FluentLogger.forEnclosingClass()

class AudioEnvironment {
    val sampleRate: Hertz = DEFAULT_SAMPLE_RATE
    val tuning = Tuning.A440
    val octaveFormat = OctaveFormat.INTERNAL
    val portRegistry = PortRegistry()

    internal val inputsByClass = mutableStateMapOf<KClass<*>, SnapshotStateList<MutableState<*>>>()
    internal val outputsByClass = mutableStateMapOf<KClass<*>, SnapshotStateList<State<*>>>()

    companion object {
        private val DEFAULT_SAMPLE_RATE = 44100.Hz
    }
}

@Composable
fun rememberAudioEnvironment() = remember { AudioEnvironment() }

private fun DrawScope.drawCables(portRegistry: PortRegistry, portColors: PortColors) {
    val centerOffset = Offset(ConnectionPointCenter.toPx(), ConnectionPointCenter.toPx())
    val cableWidth = CableWidth.toPx()
    portRegistry.allCables.forEach { cable ->
        val sourceOffset = portRegistry.getPortOffset(cable.sourcePortId)
        val destinationOffset = portRegistry.getPortOffset(cable.destinationPortId)
        val color = portColors.colorForType(portRegistry.outputById(cable.sourcePortId).type)
        drawLine(
            color = color,
            start = sourceOffset + centerOffset,
            end = destinationOffset + centerOffset,
            strokeWidth = cableWidth,
            alpha = CableAlpha,
        )
    }
}

@Composable
fun ColumnScope.AudioEnvironment(
    environment: AudioEnvironment = rememberAudioEnvironment(),
    content: @Composable AudioEnvironmentScope.() -> Unit
) {
    val portRegistry = environment.portRegistry
    val portColors = PortColors(MaterialTheme.colors)

    CompositionLocalProvider(
        LocalPortRegistry provides portRegistry,
        LocalPortColors provides portColors,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .onGloballyPositioned { location ->
                    portRegistry.updateRootLocation(location)
                }
                .drawWithContent {
                    drawContent()
                    drawCables(portRegistry = portRegistry, portColors = portColors)
                }
        ) {
            val scope = AudioEnvironmentScopeImpl(environment = environment, columnScope = this@AudioEnvironment)
            scope.content()
        }

        portRegistry.allCables.forEach { cable ->
            LaunchedEffect(cable.sourceState.value, cable.destinationState.value) {
                logger.atFine().log("Syncing input ${cable.sourceState} to output ${cable.destinationState}")
                cable.updateValue()
            }
        }
    }
}
