package garden.ephemeral.audio.units

import garden.ephemeral.audio.util.row
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.floats.plusOrMinus
import io.kotest.matchers.shouldBe

class DecibelSpec : FreeSpec({
    "toGain" - {
        "for voltage" - {
            withData(
                // OpenAL spec says:
                // "An AL_GAIN value of 0.5 is equivalent to an attenuation of 6 dB."
                // So it's clear the gain they're talking about is the gain in voltage, as opposed to power.
                row((-6).dB, 0.5011872f),

                row(20.dB, 10.0f),
                row(0.dB, 1.0f),
                row((-20).dB, 0.1f),
                row((-119.9).dB, 1.0E-6f),
                row((-120).dB, 0.0f),
            ) { (input, expected) ->
                input.toGain() shouldBe (expected plusOrMinus 1.0E-7f)
            }
        }

        "for power" - {
            withData(
                row(10.dB, 10.0f),
                row(0.dB, 1.0f),
                row((-10).dB, 0.1f),
                row((-59.9).dB, 1.0E-6f),
                row((-60).dB, 0.0f),
            ) { (input, expected) ->
                input.toGain(Decibel.Scaling.Power) shouldBe (expected plusOrMinus 1.0E-7f)
            }
        }

    }
})
