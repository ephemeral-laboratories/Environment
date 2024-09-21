package garden.ephemeral.audio.components

import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import garden.ephemeral.audio.model.AudioPlayer
import garden.ephemeral.audio.model.NO_BUFFER_SUPPLIER
import garden.ephemeral.audio.openal.AudioFormat
import garden.ephemeral.audio.uimodel.AdvertiseInputEffect
import garden.ephemeral.audio.uimodel.AudioEnvironmentScope
import garden.ephemeral.audio.units.dB

@Composable
fun AudioEnvironmentScope.AudioOut() = StandardComponent("Audio Out") {
    val volume = remember { mutableStateOf((-24).dB) }

    val bufferSupplier = remember { mutableStateOf(NO_BUFFER_SUPPLIER) }
    AdvertiseInputEffect(bufferSupplier)

    val player = remember {
        // TODO: Audio format should be selectable
        AudioPlayer(
            bufferSupplier = bufferSupplier.value,
            format = AudioFormat.MONO16,
            sampleRate = environment.sampleRate
        )
    }

    LaunchedEffect(volume.value) {
        player.volume = volume.value
    }

    DisposableEffect(Unit) {
        player.start()
        onDispose {
            player.close()
        }
    }

    val currentBufferSupplier = rememberUpdatedState(bufferSupplier.value)
    LaunchedEffect(currentBufferSupplier.value) {
        player.bufferSupplier = currentBufferSupplier.value
    }

    Card {
        Text(text = "Volume = ${volume.value}")
        DecibelSlider(
            value = volume.value,
            onValueChange = { value -> volume.value = value },
            valueRange = player.volumeRange,
        )
    }
}
