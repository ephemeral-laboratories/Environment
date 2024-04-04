package garden.ephemeral.audio.model

/**
 * | Octave Number |   C |  C♯ |   D |  D♯ |   E |   F |  F♯ |   G |  G♯ |   A |  A♯ |   B |
 * | ------------: | --: | --: | --: | --: | --: | --: | --: | --: | --: | --: | --: | --: |
 * |             0 |   0 |   1 |   2 |   3 |   4 |   5 |   6 |   7 |   8 |   9 |  10 |  11 |
 * |             1 |  12 |  13 |  14 |  15 |  16 |  17 |  18 |  19 |  20 |  21 |  22 |  23 |
 * |             2 |  24 |  25 |  26 |  27 |  28 |  29 |  30 |  31 |  32 |  33 |  34 |  35 |
 * |             3 |  36 |  37 |  38 |  39 |  40 |  41 |  42 |  43 |  44 |  45 |  46 |  47 |
 * |             4 |  48 |  49 |  50 |  51 |  52 |  53 |  54 |  55 |  56 |  57 |  58 |  59 |
 * |          -> 5 |  60 |  61 |  62 |  63 |  64 |  65 |  66 |  67 |  68 |  69 |  70 |  71 | <- middle octave
 * |             6 |  72 |  73 |  74 |  75 |  76 |  77 |  78 |  79 |  80 |  81 |  82 |  83 |
 * |             7 |  84 |  85 |  86 |  87 |  88 |  89 |  90 |  91 |  92 |  93 |  94 |  95 |
 * |             8 |  96 |  97 |  98 |  99 | 100 | 101 | 102 | 103 | 104 | 105 | 106 | 107 |
 * |             9 | 108 | 109 | 110 | 111 | 112 | 113 | 114 | 115 | 116 | 117 | 118 | 119 |
 * |            10 | 120 | 121 | 122 | 123 | 124 | 125 | 126 | 127 |   — |   — |   — |   — |
 */
@JvmInline
value class MidiNote(val midiValue: Int) {
    init {
        require(midiValue in 0..127) { "Illegal MIDI note value: $midiValue" }
    }

    val note: Note get() = noteTable[midiValue % 12]

    /**
     * Gets the note one octave up.
     *
     * @return the note one octave up.
     */
    fun oneOctaveUp(): MidiNote = MidiNote(midiValue + 12)

    /**
     * Gets the note one octave down.
     *
     * @return the note one octave down.
     */
    fun oneOctaveDown(): MidiNote = MidiNote(midiValue - 12)

    /**
     * Gets a note within the next octave up.
     * If the note is the same as this one then you will get the same note an octave up.
     *
     * @param note the note to get.
     * @return that MIDI note.
     */
    fun ascendTo(note: Note): MidiNote {
        val semitonesAbove = (note.ordinal - this.note.ordinal) % 12
        return if (semitonesAbove == 0) {
            oneOctaveUp()
        } else {
            MidiNote(midiValue + semitonesAbove)
        }
    }

    /**
     * Gets a note within the next octave down.
     * If the note is the same as this one then you will get the same note an octave down.
     *
     * @param note the note to get.
     * @return that MIDI note.
     */
    fun descendTo(note: Note): MidiNote {
        val semitonesBelow = (this.note.ordinal - note.ordinal) % 12
        return if (semitonesBelow == 0) {
            oneOctaveDown()
        } else {
            MidiNote(midiValue - semitonesBelow)
        }
    }

    fun toPitch() = Pitch(midiValue + Pitch.MIDDLE_C.packedValue - MIDDLE_C.midiValue)

    companion object {
        val MIDDLE_C = MidiNote(60)
        val A_ABOVE_MIDDLE_C = MidiNote(69)

        private val noteTable = arrayOf(
            Note.C,
            Note.C_SHARP,
            Note.D,
            Note.D_SHARP,
            Note.E,
            Note.F,
            Note.F_SHARP,
            Note.G,
            Note.G_SHARP,
            Note.A,
            Note.A_SHARP,
            Note.B
        )
    }
}
