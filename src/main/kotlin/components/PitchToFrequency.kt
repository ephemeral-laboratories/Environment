package garden.ephemeral.audio.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import garden.ephemeral.audio.model.Pitch
import garden.ephemeral.audio.uimodel.AdvertiseInputEffect
import garden.ephemeral.audio.uimodel.AdvertiseOutputEffect
import garden.ephemeral.audio.uimodel.AudioEnvironmentScope

@Composable
fun AudioEnvironmentScope.PitchToFrequency() = StandardComponent("Pitch to Frequency") {

    val pitch = remember { mutableStateOf(Pitch.A_ABOVE_MIDDLE_C) }
    AdvertiseInputEffect(pitch)

    val frequency = derivedStateOf { with(environment.tuning) { pitch.value.toFrequency() } }
    AdvertiseOutputEffect(frequency)

    Column {
        Text(text = "Pitch = ${pitch.value}")
        Text(text = "Frequency = ${frequency.value}")
    }
}
