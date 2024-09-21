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

    var sourceRelative: Int
        get() = getIntProperty(IntSourceProperty.SOURCE_RELATIVE)
        set(value) = setIntProperty(IntSourceProperty.SOURCE_RELATIVE, value)

    var buffer: Int
        get() = getIntProperty(IntSourceProperty.BUFFER)
        set(value) = setIntProperty(IntSourceProperty.BUFFER, value)

    var sourceState: AudioSourceState
        get() = getIntProperty(IntSourceProperty.SOURCE_STATE).toAudioSourceState()
        set(value) = setIntProperty(IntSourceProperty.SOURCE_STATE, value.openALValue)

    var buffersQueued: Int
        get() = getIntProperty(IntSourceProperty.BUFFERS_QUEUED)
        set(value) = setIntProperty(IntSourceProperty.BUFFERS_QUEUED, value)

    var buffersProcessed: Int
        get() = getIntProperty(IntSourceProperty.BUFFERS_PROCESSED)
        set(value) = setIntProperty(IntSourceProperty.BUFFERS_PROCESSED, value)

    var pitch: Float
        get() = getFloatProperty(FloatSourceProperty.PITCH)
        set(value) = setFloatProperty(FloatSourceProperty.PITCH, value)

    var gain: Float
        get() = getFloatProperty(FloatSourceProperty.GAIN)
        set(value) = setFloatProperty(FloatSourceProperty.GAIN, value)

    var minGain: Float
        get() = getFloatProperty(FloatSourceProperty.MIN_GAIN)
        set(value) = setFloatProperty(FloatSourceProperty.MIN_GAIN, value)

    var maxGain: Float
        get() = getFloatProperty(FloatSourceProperty.MAX_GAIN)
        set(value) = setFloatProperty(FloatSourceProperty.MAX_GAIN, value)

    var maxDistance: Float
        get() = getFloatProperty(FloatSourceProperty.MAX_DISTANCE)
        set(value) = setFloatProperty(FloatSourceProperty.MAX_DISTANCE, value)

    var rolloffFactor: Float
        get() = getFloatProperty(FloatSourceProperty.ROLLOFF_FACTOR)
        set(value) = setFloatProperty(FloatSourceProperty.ROLLOFF_FACTOR, value)

    var coneOuterGain: Float
        get() = getFloatProperty(FloatSourceProperty.CONE_OUTER_GAIN)
        set(value) = setFloatProperty(FloatSourceProperty.CONE_OUTER_GAIN, value)

    var coneInnerAngle: Float
        get() = getFloatProperty(FloatSourceProperty.CONE_INNER_ANGLE)
        set(value) = setFloatProperty(FloatSourceProperty.CONE_INNER_ANGLE, value)

    var coneOuterAngle: Float
        get() = getFloatProperty(FloatSourceProperty.CONE_OUTER_ANGLE)
        set(value) = setFloatProperty(FloatSourceProperty.CONE_OUTER_ANGLE, value)

    var referenceDistance: Float
        get() = getFloatProperty(FloatSourceProperty.REFERENCE_DISTANCE)
        set(value) = setFloatProperty(FloatSourceProperty.REFERENCE_DISTANCE, value)


    override fun close() {
        alDeleteSources(source)
    }

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

    fun setFloatProperty(property: FloatSourceProperty, value: Float) {
        AL10.alSourcef(source, property.openALValue, value)
    }

    companion object {
        val NULL = AudioSource(0)

        fun generate() = AudioSource(AL10.alGenSources())
    }
}
