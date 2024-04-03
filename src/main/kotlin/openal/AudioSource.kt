package garden.ephemeral.audio.openal

import garden.ephemeral.audio.openal.AudioSourceState.Companion.toAudioSourceState
import org.lwjgl.openal.AL10
import org.lwjgl.openal.AL10.alDeleteSources
import org.lwjgl.openal.AL10.alSourcePause
import org.lwjgl.openal.AL10.alSourcePlay
import org.lwjgl.openal.AL10.alSourceQueueBuffers
import org.lwjgl.openal.AL10.alSourceRewind
import org.lwjgl.openal.AL10.alSourceStop

/**
 * An OpenAL audio source.
 */
@JvmInline
value class AudioSource private constructor(private val source: Int) : AutoCloseable {
    override fun close() {
        alDeleteSources(source)
    }

    fun getState() = getIntProperty(IntSourceProperty.SOURCE_STATE).toAudioSourceState()

    fun play() {
        alSourcePlay(source)
    }

    fun pause() {
        alSourcePause(source)
    }

    fun stop() {
        alSourceStop(source)
    }

    fun rewind() {
        alSourceRewind(source)
    }

    fun queueBuffer(buffer: AudioBuffer) {
        alSourceQueueBuffers(source, buffer.bufferName)
    }

    fun unqueueBuffer(): AudioBuffer {
        return AudioBuffer(AL10.alSourceUnqueueBuffers(source))
    }

    fun getIntProperty(property: IntSourceProperty) = AL10.alGetSourcei(source, property.openALValue)
    fun getIntVectorProperty(property: VectorSourceProperty): List<Int> {
        val values = IntArray(3)
        AL10.alGetSourceiv(source, property.openALValue, values)
        return values.asList()
    }

    fun setIntProperty(property: IntSourceProperty, value: Int) {
        AL10.alSourcei(source, property.openALValue, value)
    }

    fun getFloatProperty(property: FloatSourceProperty) = AL10.alGetSourcef(source, property.openALValue)
    fun getFloatVectorProperty(property: VectorSourceProperty): List<Float> {
        val values = FloatArray(3)
        AL10.alGetSourcefv(source, property.openALValue, values)
        return values.asList()
    }

    companion object {
        val NULL = AudioSource(0)

        fun generate() = AudioSource(AL10.alGenSources())
    }
}
