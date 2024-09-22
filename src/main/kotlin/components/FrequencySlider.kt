package garden.ephemeral.audio.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import garden.ephemeral.audio.model.Pitch
import garden.ephemeral.audio.uimodel.AudioEnvironmentScope
import garden.ephemeral.audio.units.Hz

@Composable
fun AudioEnvironmentScope.FrequencySlider() {
    StandardComponent(name = "Frequency Slider") {
        val frequency = remember {
            mutableStateOf(with(environment.tuning) { Pitch.A_ABOVE_MIDDLE_C.toFrequency() })
        }

        Column {
            val frequencyValue = frequency.value
            Text("Frequency = $frequencyValue")
            Row {
                Slider(
                    value = frequencyValue.value,
                    onValueChange = { frequency.value = it.Hz },
                    valueRange = 40.0f..3_000.0f,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                OutputPort(frequency)
            }
        }
    }
}
