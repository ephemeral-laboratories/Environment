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
fun AudioEnvironmentScope.FrequencyToPitch() = StandardComponent("Frequency to Pitch") {

    val frequency = remember { mutableStateOf(with(environment.tuning) { Pitch.A_ABOVE_MIDDLE_C.toFrequency() }) }
    AdvertiseInputEffect(frequency)

    val pitch = derivedStateOf { with(environment.tuning) { frequency.value.toNearestPitch() } }
    AdvertiseOutputEffect(pitch)

    Column {
        Text(text = "Frequency = ${frequency.value}")
        Text(text = "Pitch = ${pitch.value}")
    }
}
