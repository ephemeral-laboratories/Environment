package garden.ephemeral.audio.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import garden.ephemeral.audio.model.AudioEnvironmentScope
import garden.ephemeral.audio.model.BufferSupplier
import garden.ephemeral.audio.model.NO_BUFFER_SUPPLIER
import garden.ephemeral.audio.units.Hz
import garden.ephemeral.audio.util.MIDDLE_A
import garden.ephemeral.audio.util.TAU
import garden.ephemeral.audio.util.scaleToShort
import kotlin.math.sin

@Composable
fun AudioEnvironmentScope.SineGenerator() = StandardComponent(name = "Sine Generator") {
    val shouldGenerate = remember { mutableStateOf(false) }
    AdvertiseBooleanInputEffect(shouldGenerate)

    val frequency = remember { mutableStateOf(MIDDLE_A) }

    // TODO: Try to further collapse this to a single supplier, I think it's possible?
    val sineWaveBufferSupplier = object : BufferSupplier {
        private var wavePos = 0

        override fun invoke(bufferSize: Int): ShortArray = if (!shouldGenerate.value) {
            ShortArray(0)
        } else {
            ShortArray(bufferSize).also { s ->
                for (i in 0 until bufferSize) {
                    // TODO: Decide on a / b * c vs a * c / b
                    s[i] = sin(((frequency.value * TAU) / environment.sampleRate) * wavePos).scaleToShort()
                    wavePos++
                }
            }
        }
    }
    val bufferSupplier = derivedStateOf { if (shouldGenerate.value) sineWaveBufferSupplier else NO_BUFFER_SUPPLIER }
    AdvertiseBufferOutputEffect(bufferSupplier)

    Column {
        val frequencyValue = frequency.value
        Text("Frequency = $frequencyValue")
        Slider(value = frequencyValue.value, onValueChange = { frequency.value = it.Hz }, valueRange = 1.0f..800.0f)
    }
}
