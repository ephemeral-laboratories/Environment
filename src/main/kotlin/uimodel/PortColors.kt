package garden.ephemeral.audio.uimodel

import androidx.compose.ui.graphics.Color
import kotlin.reflect.KClass

class PortColors {
    fun <T : Any> colorForType(type: KClass<T>): Color {
        // TODO: Come up with a sensible colour scheme
        return Color.Black
    }
}
