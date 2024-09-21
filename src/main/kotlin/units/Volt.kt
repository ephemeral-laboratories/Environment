package garden.ephemeral.audio.units

/**
 * A voltage in Volts.
 */
@JvmInline
value class Volt(val value: Float) : Comparable<Volt> {
    override fun compareTo(other: Volt) = value.compareTo(other.value)

    override fun toString() = "%,.1f V".format(value)

    operator fun times(scalar: Int) = Volt(value * scalar)
    operator fun times(scalar: Float) = Volt(value * scalar)
    operator fun div(scalar: Int) = Volt(value / scalar)
    operator fun div(scalar: Float) = Volt(value / scalar)
    operator fun div(frequency: Volt) = value / frequency.value
}

val Int.V get() = toFloat().V
val Float.V get() = Volt(this)
