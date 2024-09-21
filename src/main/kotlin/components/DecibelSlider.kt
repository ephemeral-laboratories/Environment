package garden.ephemeral.audio.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Slider
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import garden.ephemeral.audio.units.Decibel
import garden.ephemeral.audio.units.dB

@Composable
fun DecibelSlider(
    value: Decibel,
    onValueChange: (Decibel) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedRange<Decibel>,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: SliderColors = SliderDefaults.colors(),
) {
    Slider(
        value = value.value,
        onValueChange = { newValue -> onValueChange(newValue.dB) },
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange.start.value..valueRange.endInclusive.value,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        interactionSource = interactionSource,
        colors = colors,
    )
}
