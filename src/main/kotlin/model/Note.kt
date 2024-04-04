package garden.ephemeral.audio.model

/**
 * Enumeration of notes.
 */
enum class Note(private val prettyName: String) {
    C("C"), C_SHARP("C♯"),
    D("D"), D_SHARP("D♯"),
    E("E"),
    F("F"), F_SHARP("F♯"),
    G("G"), G_SHARP("G♯"),
    A("A"), A_SHARP("A♯"),
    B("B"),
    ;

    override fun toString() = prettyName

    companion object {
        fun semitonesFromC(value: Int) = Note.entries[value]
    }
}
