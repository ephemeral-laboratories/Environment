package garden.ephemeral.audio.uimodel

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import com.google.common.flogger.FluentLogger

internal class AudioEnvironmentScopeImpl(
    override val environment: AudioEnvironment, columnScope: ColumnScope
) : AudioEnvironmentScope, ColumnScope by columnScope {

    @Composable
    override fun <T : Any> HardWire(output: OutputPortInfo<T>?, input: InputPortInfo<T>?) {
        if (input != null && output != null) {
            val cable = remember {
                @Suppress("UNCHECKED_CAST")
                Cable(
                    sourcePortId = output.id,
                    destinationPortId = input.id,
                    sourceState = environment.portRegistry.outputById(output.id).valueState as State<T>,
                    destinationState = environment.portRegistry.inputById(input.id).valueState as MutableState<T>,
                )
            }
            DisposableEffect(cable) {
                environment.portRegistry.addCable(cable)
                onDispose {
                    environment.portRegistry.removeCable(cable)
                }
            }
        }
    }

    companion object {
        private val logger: FluentLogger = FluentLogger.forEnclosingClass()
    }
}
