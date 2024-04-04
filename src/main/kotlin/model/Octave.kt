package garden.ephemeral.audio.model

/**
 * Standardised model of which octave a note is in. In our model, the middle octave is octave zero.
 * Covers a range of pitch far wider than what a human can distinguish.
 */
@JvmInline
value class Octave(val value: Int) {
    fun up() = Octave(value + 1)
    fun down()  = Octave(value - 1)

    init {
        require(value in -6..6) { "Octave out of range -6..6: $value" }
    }

    fun toString(format: OctaveFormat) = (value + format.middleOctave).toString()

    override fun toString() = toString(OctaveFormat.INTERNAL)

    companion object {
        /**
         * The "middle" octave.
         */
        val Middle = Octave(0)
    }
}
