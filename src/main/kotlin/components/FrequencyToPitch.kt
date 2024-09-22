package garden.ephemeral.audio.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import garden.ephemeral.audio.model.Pitch
import garden.ephemeral.audio.uimodel.AudioEnvironmentScope

@Composable
fun AudioEnvironmentScope.FrequencyToPitch() = StandardComponent("Frequency to Pitch") {
    val frequency = remember { mutableStateOf(with(environment.tuning) { Pitch.A_ABOVE_MIDDLE_C.toFrequency() }) }
    val pitch = derivedStateOf { with(environment.tuning) { frequency.value.toNearestPitch() } }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            InputPort(frequency)
            Text(text = "Frequency = ${frequency.value}")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Pitch = ${pitch.value}")
            OutputPort(pitch)
        }
    }
}
