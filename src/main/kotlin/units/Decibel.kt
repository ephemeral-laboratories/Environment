package garden.ephemeral.audio.units

import kotlin.math.pow

/**
 * A loudness in decibels.
 */
@JvmInline
value class Decibel(val value: Float) : Comparable<Decibel> {
    override fun compareTo(other: Decibel) = value.compareTo(other.value)

    override fun toString() = "%,.1f dB".format(value)

    /**
     * Converts the decibel value to a gain value.
     * Decibels are a logarithmic scale, modeled on human perception of sound.
     * Gain is a physical factor by which voltage is increased in an amplifier, and is linear.
     *
     * @param scaling the type of scaling to perform. Depends on what sort of units you are working with;
     *        defaults to voltage as that seems to be the scaling expected by audio APIs.
     * @return the gain value, dimensionless.
     */
    fun toGain(scaling: Scaling = Scaling.Voltage): Float {
        val result = 10.0f.pow(value / scaling.divisor)

        // Special case for reasons that aren't immediately apparent, but Wolfram agrees.
        if (result <= 1.0E-6f) {
            return 0.0f
        }

        return result
    }

    /**
     * Enumeration of scaling types.
     *
     * @property divisor the divisor used when converting to gain.
     */
    enum class Scaling(internal val divisor: Float) {
        /**
         * The scaling most people are familiar with, where 10 decibels = 10 times the power.
         */
        Power(10.0f),

        /**
         * The appropriate scaling when dealing with voltages - results in decibels having half the
         * effect, as power scales with the square of the voltage.
         */
        Voltage(20.0f),
    }
}

val Int.dB get() = toFloat().dB
val Double.dB get() = toFloat().dB
val Float.dB get() = Decibel(this)
