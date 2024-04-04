package garden.ephemeral.audio.model

import garden.ephemeral.audio.units.Hz
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.roundToInt

/**
 * An instrument's tuning.
 */
interface Tuning {
    fun Pitch.toFrequency(): Hz
    fun Hz.toNearestPitch(): Pitch

    class EqualTemperament(private val frequencyOfAAboveMiddleC: Hz) : Tuning {
        private val semitone = ln(2.0f) / 12

        // By definition, our Pitch model uses 0 for A above middle C, so...
        override fun Pitch.toFrequency() = frequencyOfAAboveMiddleC * exp(this.semitonesFromAAboveMiddleC * semitone)

        override fun Hz.toNearestPitch() = Pitch((ln(this / frequencyOfAAboveMiddleC) / semitone).roundToInt())
    }

    companion object {
        /**
         * Equal temperament where the A above middle C is 440Hz. The standard.
         */
        val A440 = EqualTemperament(440.Hz)

        /**
         * Alternative equal temperament where the A above middle C is 432Hz instead.
         */
        val A432 = EqualTemperament(432.Hz)
    }
}
