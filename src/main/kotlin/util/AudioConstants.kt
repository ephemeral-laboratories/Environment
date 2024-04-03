package garden.ephemeral.audio.util

import garden.ephemeral.audio.units.Hz

/**
 * The number of radians in one turn. Now with half the precision out of the box!
 */
const val TAU = (Math.PI * 2.0).toFloat()

// TODO: These guys should end up in some kind of Tuning class. Converting note to frequency.

/**
 * The frequency of middle A.
 */
val MIDDLE_A = 440.Hz

/**
 * The frequency of middle A.
 */
val ALTERNATIVE_MIDDLE_A = 432.Hz
