package garden.ephemeral.audio.model

import garden.ephemeral.audio.util.floorMod

/**
 * A note with a given octave.
 *
 * This uses American Standard Pitch Notation whereby middle C is in octave 4.
 *
 * @param packedValue the packed pitch value, which is the number of semitones plus or minus
 *        from middle C.
 */
@JvmInline
value class Pitch(internal val packedValue: Int) {
    internal val semitonesFromAAboveMiddleC get() = this.packedValue - 9

    val note: Note
        get() {
            return Note.semitonesFromC(packedValue.floorMod(Note.entries.size))
        }

    val octave: Octave
        get() {
            return Octave(packedValue.floorDiv(Note.entries.size))
        }

    /**
     * Gets the note one octave up.
     *
     * @return the note one octave up.
     */
    fun oneOctaveUp() = Pitch(packedValue + 12)

    /**
     * Gets the note one octave down.
     *
     * @return the note one octave down.
     */
    fun oneOctaveDown() = Pitch(packedValue - 12)

    /**
     * Gets a note within the next octave up.
     * If the note is the same as this one then you will get the same note an octave up.
     *
     * @param note the note to get.
     * @return that MIDI note.
     */
    fun ascendTo(note: Note): Pitch {
        val semitonesAbove = (note.ordinal - this.note.ordinal)
        return when {
            semitonesAbove == 0 -> oneOctaveUp()
            semitonesAbove < 0 -> Pitch(packedValue + semitonesAbove + 12)
            else -> Pitch(packedValue + semitonesAbove)
        }
    }

    /**
     * Gets a note within the next octave down.
     * If the note is the same as this one then you will get the same note an octave down.
     *
     * @param note the note to get.
     * @return that MIDI note.
     */
    fun descendTo(note: Note): Pitch {
        val semitonesBelow = (this.note.ordinal - note.ordinal) % 12
        return when {
            semitonesBelow == 0 -> oneOctaveDown()
            semitonesBelow < 0 -> Pitch(packedValue - semitonesBelow - 12)
            else -> Pitch(packedValue - semitonesBelow)
        }
    }

    fun toMidiNote() = MidiNote(packedValue + MidiNote.MIDDLE_C.midiValue - MIDDLE_C.packedValue)

    fun toString(octaveFormat: OctaveFormat) = "$note${octave.toString(octaveFormat)}"

    override fun toString() = toString(OctaveFormat.INTERNAL)

    companion object {
        fun of(note: Note, octave: Octave): Pitch {
            return Pitch(octave.value * 12 + note.ordinal)
        }

        val MIDDLE_C = of(note = Note.C, octave = Octave.Middle)
        val A_ABOVE_MIDDLE_C = MIDDLE_C.ascendTo(Note.A)
    }
}
