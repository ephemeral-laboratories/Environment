package garden.ephemeral.audio.model

/**
 * Enumeration of supported formats when printing octave numbers.
 *
 * Different standards disagreed on which C was middle C, so we have this situation where you can
 * choose which C is middle C.
 *
 * For our own conventions, we decided to number middle C as 0, because that's the most logical
 * for all calculations and the most sensible default to choose.
 */
enum class OctaveFormat(val middleOctave: Int) {
    INTERNAL(0),
    ROLAND_GS(4),
    YAMAHA_XG(5),
    ;
}
