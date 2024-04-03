package garden.ephemeral.audio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import garden.ephemeral.audio.components.AudioOut
import garden.ephemeral.audio.components.PushButton
import garden.ephemeral.audio.components.SineGenerator
import garden.ephemeral.audio.model.AudioEnvironment

fun main() = application {
    Window(title = "Beeper", onCloseRequest = ::exitApplication) {
        MaterialTheme(colors = darkColors()) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    AudioEnvironment {
                        PushButton()
                        SineGenerator()
                        AudioOut()

                        // TODO: Remove eventually
                        // Hard-wire for now
                        println("Hard-wiring environment $environment")
                        val pushButtonOutput = environment.booleanOutputs.getOrNull(0)
                        val sineGeneratorSwitch = environment.booleanInputs.getOrNull(0)
                        val sineGeneratorOutput = environment.bufferOutputs.getOrNull(0)
                        val audioOutInput = environment.bufferInputs.getOrNull(0)
                        HardWire(pushButtonOutput, sineGeneratorSwitch)
                        HardWire(sineGeneratorOutput, audioOutInput)
                    }
                }
            }
        }
    }
}
