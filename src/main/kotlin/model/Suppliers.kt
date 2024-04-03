package garden.ephemeral.audio.model

typealias BooleanSupplier = () -> Boolean

val NO_BOOLEAN_SUPPLIER = { false }

typealias FloatSupplier = () -> Float

val NO_FLOAT_SUPPLIER = { false }

typealias BufferSupplier = (bufferSize: Int) -> ShortArray?

val NO_BUFFER_SUPPLIER: BufferSupplier = { bufferSize: Int -> null }
