package garden.ephemeral.audio.units

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.floats.FloatToleranceMatcher

val FloatToleranceMatcher.Hz get(): Matcher<Hertz> = HertzToleranceMatcher(this)

class HertzToleranceMatcher(private val delegate: FloatToleranceMatcher) : Matcher<Hertz> {
    override fun test(value: Hertz): MatcherResult {
        return delegate.test(value.value)
    }
}
