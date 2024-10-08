package garden.ephemeral.audio.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import garden.ephemeral.audio.model.BufferSupplier
import garden.ephemeral.audio.model.NO_BUFFER_SUPPLIER
import garden.ephemeral.audio.model.Pitch
import garden.ephemeral.audio.uimodel.AudioEnvironmentScope
import garden.ephemeral.audio.util.TAU
import garden.ephemeral.audio.util.floorMod
import garden.ephemeral.audio.util.scaleToShort
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.sign
import kotlin.math.sin

private enum class Waveform(val localizedName: String) {
    Sine("Sine"),
    Square("Square"),
    Saw("Saw"),
    Triangle("Triangle"),
}

@Composable
fun AudioEnvironmentScope.Oscillator() {
    StandardComponent(name = "Oscillator") {
        val shouldGenerate = remember { mutableStateOf(false) }

        val waveform = remember { mutableStateOf(Waveform.Sine) }

        val frequency = remember {
            mutableStateOf(with(environment.tuning) { Pitch.A_ABOVE_MIDDLE_C.toFrequency() })
        }

        val waveformBufferSupplier = object : BufferSupplier {
            private var wavePos = 0

            override fun supply(bufferSize: Int) = ShortArray(bufferSize).also { s ->
                for (i in 0 until bufferSize) {
                    val tDivP = frequency.value * wavePos / environment.sampleRate

                    val sample = when (waveform.value) {
                        // These curves all line up, in the sense that when one is high, all are high;
                        // when one is low, all are low. https://www.desmos.com/calculator/sktssekdi9
                        Waveform.Sine -> sin(TAU * tDivP)
                        Waveform.Square -> sign(0.5f - (tDivP floorMod 1.0f))
                        Waveform.Saw -> 2.0f * (tDivP + 0.25f - floor(tDivP + 0.75f)) + 1.0f
                        Waveform.Triangle -> 2.0f * abs(2.0f * (tDivP - floor(0.5f + tDivP)))
                    }
                    s[i] = sample.scaleToShort()
                    wavePos++
                }
            }
        }
        val bufferSupplier = derivedStateOf { if (shouldGenerate.value) waveformBufferSupplier else NO_BUFFER_SUPPLIER }

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Waveform.entries.forEach { option ->
                    RadioButton(selected = waveform.value == option, onClick = { waveform.value = option })
                    Text(text = option.localizedName)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                InputPort(shouldGenerate)
                Text("Active")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                InputPort(frequency)
                Text("Frequency = ${frequency.value}")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Resulting Waveform")
                OutputPort(bufferSupplier)
            }
        }
    }
}
