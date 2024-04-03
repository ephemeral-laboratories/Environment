package garden.ephemeral.audio.components

import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import garden.ephemeral.audio.model.AudioEnvironmentScope
import garden.ephemeral.audio.model.AudioPlayer
import garden.ephemeral.audio.model.NO_BUFFER_SUPPLIER
import garden.ephemeral.audio.openal.AudioFormat

@Composable
fun AudioEnvironmentScope.AudioOut() = StandardComponent("Audio Out") {
    // TODO: Volume is not wired up
    var volume by remember { mutableStateOf(50.0f) }

    val bufferSupplier = remember { mutableStateOf(NO_BUFFER_SUPPLIER) }
    AdvertiseBufferInputEffect(bufferSupplier)

    val player = remember {
        // TODO: Audio format should be selectable
        AudioPlayer(
            bufferSupplier = bufferSupplier.value,
            format = AudioFormat.MONO16,
            sampleRate = environment.sampleRate
        )
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
        Text(text = "TODO How to get controls like volume")
    }
}
