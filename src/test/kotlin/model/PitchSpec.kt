package garden.ephemeral.audio.model

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class PitchSpec : FreeSpec({
    "assembling and disassembling packed values" - {
        "assembling pitch value from note and octave" {
            Pitch.of(Note.C, Octave.Middle) shouldBe Pitch(0)
            Pitch.of(Note.A, Octave(-1)) shouldBe Pitch(-3)
            Pitch.of(Note.D, Octave(2)) shouldBe Pitch(26)
        }

        "extracting values back out of packed value" {
            val pitch1 = Pitch(0)
            pitch1.note shouldBe Note.C
            pitch1.octave shouldBe Octave.Middle

            val pitch2 = Pitch(-3)
            pitch2.note shouldBe Note.A
            pitch2.octave shouldBe Octave(-1)

            val pitch3 = Pitch(26)
            pitch3.note shouldBe Note.D
            pitch3.octave shouldBe Octave(2)
        }
    }

    "going up and down octaves" - {
        "going up one octave" {
            Pitch.of(Note.C, Octave.Middle).oneOctaveUp() shouldBe Pitch.of(Note.C, Octave(1))
        }
        "going down one octave" {
            Pitch.of(Note.C, Octave.Middle).oneOctaveDown() shouldBe Pitch.of(Note.C, Octave(-1))
        }
    }

    "ascending and descending" - {
        "C up to G stays in the same octave" {
            Pitch.of(Note.C, Octave.Middle).ascendTo(Note.G) shouldBe Pitch.of(Note.G, Octave.Middle)
        }
        "C up to A stays in the same octave" {
            Pitch.of(Note.C, Octave.Middle).ascendTo(Note.A) shouldBe Pitch.of(Note.A, Octave.Middle)
        }

        "A up to C goes an octave up" {
            Pitch.of(Note.A, Octave.Middle).ascendTo(Note.C) shouldBe Pitch.of(Note.C, Octave(1))
        }
        "C up to C goes an octave up" {
            Pitch.of(Note.C, Octave.Middle).ascendTo(Note.C) shouldBe Pitch.of(Note.C, Octave(1))
        }

        "C down to A goes an octave down" {
            Pitch.of(Note.C, Octave.Middle).descendTo(Note.A) shouldBe Pitch.of(Note.A, Octave(-1))
        }
        "C down to C goes an octave down" {
            Pitch.of(Note.C, Octave.Middle).descendTo(Note.C) shouldBe Pitch.of(Note.C, Octave(-1))
        }
    }

    "converting to MidiNote" {
        Pitch.of(Note.C, Octave.Middle).toMidiNote() shouldBe MidiNote(60)
    }

    "converting to string" - {
        "default" {
            Pitch.of(Note.C, Octave.Middle).toString() shouldBe "C0"
        }
        "with Roland convention" {
            Pitch.of(Note.D, Octave.Middle).toString(OctaveFormat.ROLAND_GS) shouldBe "D4"
        }
        "with Yamaha convention" {
            Pitch.of(Note.F_SHARP, Octave.Middle).toString(OctaveFormat.YAMAHA_XG) shouldBe "F♯5"
        }
    }
})
