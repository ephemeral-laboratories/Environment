package garden.ephemeral.audio.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

val ConnectionPointRadius = 12.dp
val ConnectionPointStrokeWidth = 2.dp

@Composable
fun ConnectionPointVisual(
    isConnected: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.size(48.dp)) {
        drawCircle(
            color = color,
            radius = ConnectionPointRadius.toPx(),
            style = if (isConnected) Fill else Stroke(width = ConnectionPointStrokeWidth.toPx()),
        )
    }
}

@Composable
@Preview
fun ConnectionPointVisualPreview() {
    Row {
        ConnectionPointVisual(isConnected = true, color = Color.Blue)
        ConnectionPointVisual(isConnected = false, color = Color.Blue)
    }
}