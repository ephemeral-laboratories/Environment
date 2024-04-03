package garden.ephemeral.audio.units

/**
 * A frequency in Hertz.
 */
@JvmInline
value class Hz(val value: Float) {
    override fun toString() = "%,.1f Hz".format(value)

    operator fun times(scalar: Float) = Hz(value * scalar)
    operator fun div(scalar: Float) = Hz(value / scalar)
    operator fun div(frequency: Hz) = value / frequency.value
}

val Int.Hz get() = toFloat().Hz
val Float.Hz get() = Hz(this)
