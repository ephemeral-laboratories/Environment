package garden.ephemeral.audio.units

/**
 * A frequency in Hertz.
 */
@JvmInline
value class Hertz(val value: Float) : Comparable<Hertz> {
    override fun compareTo(other: Hertz) = value.compareTo(other.value)

    override fun toString() = "%,.1f Hz".format(value)

    operator fun times(scalar: Int) = Hertz(value * scalar)
    operator fun times(scalar: Float) = Hertz(value * scalar)
    operator fun div(scalar: Int) = Hertz(value / scalar)
    operator fun div(scalar: Float) = Hertz(value / scalar)
    operator fun div(frequency: Hertz) = value / frequency.value
}

val Int.Hz get() = toFloat().Hz
val Float.Hz get() = Hertz(this)
