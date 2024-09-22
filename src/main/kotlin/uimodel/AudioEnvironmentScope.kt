package garden.ephemeral.audio.uimodel

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import garden.ephemeral.audio.model.MidiNote

interface AudioEnvironmentScope : ColumnScope {
    val environment: AudioEnvironment

    @Composable
    fun <T : Any> HardWire(output: OutputPortInfo<T>?, input: InputPortInfo<T>?)

    /**
     * Converts a MIDI note to its frequency in Hz.
     *
     * @return the frequency in Hz.
     */
    fun MidiNote.toFrequency() = with(environment.tuning) {
        toPitch().toFrequency()
    }
}
