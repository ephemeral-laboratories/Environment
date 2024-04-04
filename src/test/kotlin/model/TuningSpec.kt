package garden.ephemeral.audio.model

import garden.ephemeral.audio.units.Hz
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.floats.plusOrMinus
import io.kotest.matchers.shouldBe

class TuningSpec : FreeSpec({
    "equal temperament returns the right frequency for various pitches" - {
        with (Tuning.A440) {
            val epsilon = 0.000001f
            Pitch.of(Note.A, Octave.Middle).toFrequency() shouldBe (440.0f plusOrMinus epsilon).Hz
        }
    }
})
