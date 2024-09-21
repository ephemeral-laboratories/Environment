package garden.ephemeral.audio.uimodel

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import garden.ephemeral.audio.model.OctaveFormat
import garden.ephemeral.audio.model.Tuning
import garden.ephemeral.audio.units.Hertz
import garden.ephemeral.audio.units.Hz
import kotlin.reflect.KClass

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

@Composable
fun ColumnScope.AudioEnvironment(
    environment: AudioEnvironment = rememberAudioEnvironment(),
    content: @Composable AudioEnvironmentScope.() -> Unit
) {
    CompositionLocalProvider(LocalPortRegistry provides environment.portRegistry) {
        content(AudioEnvironmentScopeImpl(environment = environment, columnScope = this))
    }
}
