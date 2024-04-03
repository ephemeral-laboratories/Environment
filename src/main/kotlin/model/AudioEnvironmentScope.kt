package garden.ephemeral.audio.model

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State

interface AudioEnvironmentScope : ColumnScope {
    val environment: AudioEnvironment

    @Composable
    fun AdvertiseBooleanInputEffect(state: MutableState<Boolean>)

    @Composable
    fun AdvertiseBooleanOutputEffect(state: State<Boolean>)

    @Composable
    fun AdvertiseBufferInputEffect(state: MutableState<BufferSupplier>)

    @Composable
    fun AdvertiseBufferOutputEffect(state: State<BufferSupplier>)

    @Composable
    fun <T> HardWire(output: State<T>?, input: MutableState<T>?)
}
