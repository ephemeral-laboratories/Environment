package garden.ephemeral.audio.util

// Home for things which we think should be in the standard library, but for whatever reason are not.

/**
 * The number of radians in one turn. Now with half the precision out of the box!
 */
const val TAU = (Math.PI * 2.0).toFloat()

/**
 * Correctly-behaving substitute for the modulus operator.
 */
infix fun Float.floorMod(other: Float) = ((this % other) + other) % other

/**
 * Correctly-behaving substitute for the modulus operator.
 */
infix fun Int.floorMod(other: Int) = ((this % other) + other) % other

/**
 * Correctly-behaving substitute for the modulus operator.
 */
infix fun Float.floorMod(other: Int) = this floorMod other.toFloat()

/**
 * Correctly-behaving substitute for the modulus operator.
 */
infix fun Int.floorMod(other: Float) = toFloat() floorMod other
