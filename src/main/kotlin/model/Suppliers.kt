package garden.ephemeral.audio.model

interface BufferSupplier {
    fun supply(bufferSize: Int): ShortArray?
}

val NO_BUFFER_SUPPLIER: BufferSupplier = object : BufferSupplier {
    override fun supply(bufferSize: Int) = null
}
