package garden.ephemeral.audio.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import garden.ephemeral.audio.model.Pitch
import garden.ephemeral.audio.uimodel.AdvertiseOutputEffect
import garden.ephemeral.audio.uimodel.AudioEnvironmentScope
import garden.ephemeral.audio.units.Hz

@Composable
fun AudioEnvironmentScope.FrequencySlider() {
    StandardComponent(name = "Frequency Slider") {
        val frequency = remember {
            mutableStateOf(with(environment.tuning) { Pitch.A_ABOVE_MIDDLE_C.toFrequency() })
        }
        AdvertiseOutputEffect(frequency)

        Column {
            val frequencyValue = frequency.value
            Text("Frequency = $frequencyValue")
            Slider(value = frequencyValue.value, onValueChange = { frequency.value = it.Hz }, valueRange = 1.0f..800.0f)
        }
    }
}
