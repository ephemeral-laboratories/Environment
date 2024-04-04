package garden.ephemeral.audio.units

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.floats.FloatToleranceMatcher

val FloatToleranceMatcher.Hz get(): Matcher<Hz> = HzToleranceMatcher(this)

class HzToleranceMatcher(private val delegate: FloatToleranceMatcher) : Matcher<Hz> {
    override fun test(value: Hz): MatcherResult {
        return delegate.test(value.value)
    }
}
