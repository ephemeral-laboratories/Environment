package garden.ephemeral.audio.model

/**
 * Standardised model of which octave a note is in. In our model, the middle octave is octave zero.
 * Covers a range of pitch far wider than what a human can distinguish.
 */
@JvmInline
value class Octave internal constructor(val value: Int) {
    fun up() = of(value + 1)
    fun down()  = of(value - 1)

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

        val Up1 = Octave(1)
        val Up2 = Octave(2)
        val Up3 = Octave(3)
        val Up4 = Octave(4)
        val Up5 = Octave(5)

        val Down1 = Octave(-1)
        val Down2 = Octave(-2)
        val Down3 = Octave(-3)
        val Down4 = Octave(-4)
        val Down5 = Octave(-5)

        private val fastInstances = listOf(Down5, Down4, Down3, Down2, Down1, Middle, Up1, Up2, Up3, Up4, Up5)

        /**
         * Gets an octave of a given value. For commonly-used values (which are the only values you're
         * going to be using in practice) this will return a shared instance, so it's better to use
         * this than the constructor.
         *
         * @param value the octave number, following our own convention where the middle octave is 0.
         * @return the octave.
         */
        fun of(value: Int): Octave {
            val fastInstancesIndex = value - 5
            return if (fastInstancesIndex in fastInstances.indices) fastInstances[fastInstancesIndex] else Octave(value)
        }
    }
}
