package garden.ephemeral.audio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import garden.ephemeral.audio.components.AudioOut
import garden.ephemeral.audio.components.Oscillator
import garden.ephemeral.audio.components.PushButton
import garden.ephemeral.audio.model.BufferSupplier
import garden.ephemeral.audio.uimodel.AudioEnvironment

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
                        Oscillator()
                        AudioOut()

                        // TODO: Remove eventually
                        // Hard-wire for now
                        println("Hard-wiring environment $environment")
                        val pushButtonOutput =
                            environment.outputsByClass[Boolean::class]!!.getOrNull(0) as State<Boolean>?
                        val sineGeneratorSwitch =
                            environment.inputsByClass[Boolean::class]!!.getOrNull(0) as MutableState<Boolean>?
                        val sineGeneratorOutput =
                            environment.outputsByClass[BufferSupplier::class]!!.getOrNull(0) as State<BufferSupplier>?
                        val audioOutInput =
                            environment.inputsByClass[BufferSupplier::class]!!.getOrNull(0) as MutableState<BufferSupplier>?
                        HardWire(pushButtonOutput, sineGeneratorSwitch)
                        HardWire(sineGeneratorOutput, audioOutInput)
                    }
                }
            }
        }
    }
}
