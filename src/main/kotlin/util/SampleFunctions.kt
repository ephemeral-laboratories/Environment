package garden.ephemeral.audio.util

/**
 * Clamps a double value to the range 0-1.
 */
fun Float.clamp10() = this.coerceIn(0.0f, 1.0f)

/**
 * Scales a double in the range 0-1 to a short in the range [Short.MIN_VALUE] to [Short.MAX_VALUE].
 */
fun Float.scaleToShort() = (Short.MAX_VALUE * this.clamp10()).toInt().toShort()

/**
 * Unscales a value in the range [Short.MIN_VALUE] to [Short.MAX_VALUE] to a double in tha range 0-1.
 */
fun Float.unscaleToFloat() = (this / Short.MAX_VALUE).clamp10()
