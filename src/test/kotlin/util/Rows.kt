package garden.ephemeral.audio.util

import io.kotest.data.Row
import io.kotest.datatest.IsStableType

fun <A> row(a: A) = Row1(a)
fun <A, B> row(a: A, b: B) = Row2(a, b)
fun <A, B, C> row(a: A, b: B, c: C) = Row3(a, b, c)
fun <A, B, C, D> row(a: A, b: B, c: C, d: D) = Row4(a, b, c, d)

@IsStableType
data class Row1<A>(var a: A) : Row {
    override fun values() = listOf(a)
}

@IsStableType
data class Row2<A, B>(var a: A, var b: B) : Row {
    override fun values() = listOf(a, b)
}

@IsStableType
data class Row3<A, B, C>(var a: A, var b: B, var c: C) : Row {
    override fun values() = listOf(a, b, c)
}

@IsStableType
data class Row4<A, B, C, D>(var a: A, var b: B, var c: C, var d: D) : Row {
    override fun values() = listOf(a, b, c, d)
}
