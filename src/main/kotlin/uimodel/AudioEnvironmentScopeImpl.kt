package garden.ephemeral.audio.uimodel

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.common.flogger.FluentLogger
import kotlin.reflect.KClass

internal class AudioEnvironmentScopeImpl(
    override val environment: AudioEnvironment, columnScope: ColumnScope
) : AudioEnvironmentScope, ColumnScope by columnScope {

    @Composable
    override fun <T : Any> AdvertiseInputEffect(type: KClass<T>, state: MutableState<T>) =
        AdvertiseEffect(state, environment.inputsByClass.computeIfAbsent(type) { mutableStateListOf() })

    @Composable
    override fun <T : Any> AdvertiseOutputEffect(type: KClass<T>, state: State<T>) =
        AdvertiseEffect(state, environment.outputsByClass.computeIfAbsent(type) { mutableStateListOf() })

    @Composable
    private fun <S: State<*>> AdvertiseEffect(thing: S, list: SnapshotStateList<in S>) {
        DisposableEffect(thing) {
            logger.atFine().log("Advertising $thing to $environment")
            list.add(thing)
            logger.atFine().log("Advertised")
            onDispose {
                logger.atFine().log("Removing advertisement for $thing from $environment")
                list.remove(thing)
                logger.atFine().log("Removed")
            }
        }
    }

    @Composable
    override fun <T> HardWire(output: State<T>?, input: MutableState<T>?) {
        if (input != null && output != null) {
            LaunchedEffect(input.value, output.value) {
                logger.atFine().log("Syncing input $input to output $output")
                input.value = output.value
            }
        }
    }

    companion object {
        private val logger: FluentLogger = FluentLogger.forEnclosingClass()
    }
}
