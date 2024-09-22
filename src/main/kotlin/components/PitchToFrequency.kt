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
fun AudioEnvironmentScope.PitchToFrequency() = StandardComponent("Pitch to Frequency") {
    val pitch = remember { mutableStateOf(Pitch.A_ABOVE_MIDDLE_C) }
    val frequency = derivedStateOf { with(environment.tuning) { pitch.value.toFrequency() } }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            InputPort(pitch)
            Text(text = "Pitch = ${pitch.value}")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Frequency = ${frequency.value}")
            OutputPort(frequency)
        }
    }
}
