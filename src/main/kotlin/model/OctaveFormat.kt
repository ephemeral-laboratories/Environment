package garden.ephemeral.audio.model

/**
 * Enumeration of supported formats when printing octave numbers.
 *
 * Different standards disagreed on which C was middle C, so we have this situation where you can
 * choose which C is middle C.
 */
enum class OctaveFormat(val middleOctave: Int) {
    INTERNAL(0),
    ROLAND_GS(4),
    YAMAHA_XG(5),
    ;
}
