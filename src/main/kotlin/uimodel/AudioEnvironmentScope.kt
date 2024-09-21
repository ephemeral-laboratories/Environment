package garden.ephemeral.audio.uimodel

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import garden.ephemeral.audio.model.MidiNote
import kotlin.reflect.KClass

interface AudioEnvironmentScope : ColumnScope {
    val environment: AudioEnvironment

    @Composable
    fun <T : Any> AdvertiseInputEffect(type: KClass<T>, state: MutableState<T>)

    @Composable
    fun <T : Any> AdvertiseOutputEffect(type: KClass<T>, state: State<T>)

    @Composable
    fun <T> HardWire(output: State<T>?, input: MutableState<T>?)

    /**
     * Converts a MIDI note to its frequency in Hz.
     *
     * @return the frequency in Hz.
     */
    fun MidiNote.toFrequency() = with(environment.tuning) {
        toPitch().toFrequency()
    }
}

@Composable
inline fun <reified T : Any> AudioEnvironmentScope.AdvertiseInputEffect(state: MutableState<T>) = AdvertiseInputEffect(T::class, state)

@Composable
inline fun <reified T : Any> AudioEnvironmentScope.AdvertiseOutputEffect(state: State<T>) = AdvertiseOutputEffect(T::class, state)
