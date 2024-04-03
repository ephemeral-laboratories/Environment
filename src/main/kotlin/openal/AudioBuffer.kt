package garden.ephemeral.audio.openal

import garden.ephemeral.audio.units.Hz
import org.lwjgl.openal.AL10.alBufferData
import org.lwjgl.openal.AL10.alDeleteBuffers
import org.lwjgl.openal.AL10.alGenBuffers

@JvmInline
value class AudioBuffer internal constructor(internal val bufferName: Int) : AutoCloseable {
    override fun close() {
        alDeleteBuffers(bufferName)
    }

    fun bufferData(format: AudioFormat, samples: ShortArray, frequency: Hz) {
        alBufferData(bufferName, format.openALValue, samples, frequency.value.toInt())
    }

    companion object {
        val NULL = AudioBuffer(0)

        /**
         * Generates a single buffer.
         */
        fun generate(): AudioBuffer {
            return AudioBuffer(alGenBuffers())
        }

        /**
         * Generates multiple buffers in one go.
         *
         * @param count the number of buffers.
         * @return the array of buffers.
         */
        fun generate(count: Int): Array<AudioBuffer> {
            val result = IntArray(count)
            alGenBuffers(IntArray(count))
            OpenALException.throwIfError()
            return result.map(::AudioBuffer).toTypedArray()
        }

        /**
         * Closes multiple buffers in one go.
         *
         * @param buffers the buffers to close.
         */
        fun close(buffers: Array<AudioBuffer>) {
            alDeleteBuffers(buffers.map(AudioBuffer::bufferName).toIntArray())
        }
    }
}