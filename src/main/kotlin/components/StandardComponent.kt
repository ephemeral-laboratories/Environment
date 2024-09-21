package garden.ephemeral.audio.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import garden.ephemeral.audio.uimodel.AudioEnvironmentScope

@Composable
fun AudioEnvironmentScope.StandardComponent(
    name: String,
    content: @Composable AudioEnvironmentScope.() -> Unit
) {
    Card(backgroundColor = MaterialTheme.colors.background) {
        val localContentColor = LocalContentColor.current
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(modifier = Modifier
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    val y = size.height - strokeWidth / 2
                    drawLine(
                        localContentColor,
                        Offset(0f, y),
                        Offset(size.width, y),
                        strokeWidth
                    )
                }
            ) {
                Text(text = name)
            }

            this@StandardComponent.content()
        }
    }
}
