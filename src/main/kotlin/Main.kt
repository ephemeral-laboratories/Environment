package garden.ephemeral.audio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import garden.ephemeral.audio.components.AudioOut
import garden.ephemeral.audio.components.FrequencySlider
import garden.ephemeral.audio.components.FrequencyToPitch
import garden.ephemeral.audio.components.Oscillator
import garden.ephemeral.audio.components.PushButton
import garden.ephemeral.audio.model.BufferSupplier
import garden.ephemeral.audio.uimodel.AudioEnvironment
import garden.ephemeral.audio.units.Hertz

fun main() = application {
    Window(title = "Beeper", state = rememberWindowState(size = DpSize(800.dp, 1000.dp)), onCloseRequest = ::exitApplication) {
        MaterialTheme(colors = darkColors()) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    AudioEnvironment {
                        PushButton()
                        FrequencySlider()
                        Oscillator()
                        FrequencyToPitch()
                        AudioOut()

                        // TODO: Remove eventually
                        // Hard-wire for now
                        println("Hard-wiring environment $environment")
                        val portRegistry = environment.portRegistry
                        val pushButtonOutput by rememberUpdatedState(portRegistry.outputsByClass(Boolean::class).getOrNull(0))
                        val frequencySliderOutput by rememberUpdatedState(portRegistry.outputsByClass(Hertz::class).getOrNull(0))
                        val oscillatorSwitch by rememberUpdatedState(portRegistry.inputsByClass(Boolean::class).getOrNull(0))
                        val oscillatorFrequency by rememberUpdatedState(portRegistry.inputsByClass(Hertz::class).getOrNull(0))
                        val sineGeneratorOutput by rememberUpdatedState(portRegistry.outputsByClass(BufferSupplier::class).getOrNull(0))
                        val frequencyToPitchInput by rememberUpdatedState(portRegistry.inputsByClass(Hertz::class).getOrNull(1))
                        val audioOutInput by rememberUpdatedState(portRegistry.inputsByClass(BufferSupplier::class).getOrNull(0))
                        HardWire(pushButtonOutput, oscillatorSwitch)
                        HardWire(frequencySliderOutput, oscillatorFrequency)
                        HardWire(frequencySliderOutput, frequencyToPitchInput)
                        HardWire(sineGeneratorOutput, audioOutInput)
                    }
                }
            }
        }
    }
}
