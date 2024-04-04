package garden.ephemeral.audio.model

import garden.ephemeral.audio.units.Hz
import garden.ephemeral.audio.util.row
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.floats.plusOrMinus
import io.kotest.matchers.shouldBe

class TuningSpec : FreeSpec({
    val epsilon = 0.01f

    "equal temperament returns the right frequency for various pitches" - {
        with(Tuning.A440) {
            withData(
                row(Note.C, 130.81f, 261.63f, 523.25f),
                row(Note.C_SHARP, 138.59f, 277.18f, 554.37f),
                row(Note.D, 146.83f, 293.66f, 587.33f),
                row(Note.D_SHARP, 155.56f, 311.13f, 622.25f),
                row(Note.E, 164.81f, 329.63f, 659.25f),
                row(Note.F, 174.61f, 349.23f, 698.46f),
                row(Note.F_SHARP, 185.00f, 369.99f, 739.99f),
                row(Note.G, 196.00f, 392.00f, 783.99f),
                row(Note.G_SHARP, 207.65f, 415.30f, 830.61f),
                row(Note.A, 220.00f, 440.00f, 880.00f),
                row(Note.A_SHARP, 233.08f, 466.16f, 932.33f),
                row(Note.B, 246.94f, 493.88f, 987.77f),
            ) { (note, expectedFrequency1, expectedFrequency2, expectedFrequency3) ->
                Pitch.of(note, Octave(-1)).toFrequency() shouldBe (expectedFrequency1 plusOrMinus epsilon).Hz
                Pitch.of(note, Octave(0)).toFrequency() shouldBe (expectedFrequency2 plusOrMinus epsilon).Hz
                Pitch.of(note, Octave(1)).toFrequency() shouldBe (expectedFrequency3 plusOrMinus epsilon).Hz
            }
        }
    }

    "alternative A432 tuning returns a different frequency" {
        with(Tuning.A432) {
            Pitch.A_ABOVE_MIDDLE_C.toFrequency() shouldBe (432.0f plusOrMinus epsilon).Hz
        }
    }
})
