package garden.ephemeral.audio.openal

import org.lwjgl.openal.ALC10
import org.lwjgl.openal.ALC10.alcCreateContext

/**
 * An OpenAL audio context.
 */
@JvmInline
value class AudioContext private constructor(private val context: Long) : AutoCloseable {
    fun makeCurrent() {
        ALC10.alcMakeContextCurrent(context)
    }

    override fun close() {
        ALC10.alcDestroyContext(context)
    }

    companion object {
        val NULL = AudioContext(0L)

        fun create(device: AudioDevice, attrList: IntArray): AudioContext {
            return AudioContext(alcCreateContext(device.device, attrList))
        }
    }
}
