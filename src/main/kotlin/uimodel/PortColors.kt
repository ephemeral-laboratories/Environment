package garden.ephemeral.audio.uimodel

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import kotlin.reflect.KClass

class PortColors(private val colors: Colors = darkColors()) {
    private val storage = mutableMapOf<KClass<*>, Color>()

    fun <T : Any> colorForType(type: KClass<T>) = storage[type] ?: colors.primaryVariant
}

val LocalPortColors = staticCompositionLocalOf { PortColors() }
