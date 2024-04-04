package garden.ephemeral.audio.model

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import garden.ephemeral.audio.units.Hz

class AudioEnvironment {
    val sampleRate: Hz = DEFAULT_SAMPLE_RATE
    val tuning = Tuning.A440
    val octaveFormat = OctaveFormat.INTERNAL

    internal val booleanInputs = mutableStateListOf<MutableState<Boolean>>()
    internal val booleanOutputs = mutableStateListOf<State<Boolean>>()
    internal val bufferInputs = mutableStateListOf<MutableState<BufferSupplier>>()
    internal val bufferOutputs = mutableStateListOf<State<BufferSupplier>>()

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
    content(AudioEnvironmentScopeImpl(environment = environment, columnScope = this))
}
