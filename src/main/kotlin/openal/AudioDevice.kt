package garden.ephemeral.audio.openal

import org.lwjgl.openal.AL
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC10

/**
 * An OpenAL audio device.
 */
@JvmInline
value class AudioDevice internal constructor(internal val device: Long) : AutoCloseable {
    fun detectInternalExceptions() {
        OpenALException.throwIfError(device)
    }

    fun createCapabilities() {
        AL.createCapabilities(ALC.createCapabilities(device))
    }

    override fun close() {
        ALC10.alcCloseDevice(device)
    }

    companion object {
        val NULL = AudioDevice(0L)

        fun openDefaultDevice(): AudioDevice {
            val name = ALC10.alcGetString(0L, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER)
            check(name != null) { "Got nothing for default audio device!" }
            val device = ALC10.alcOpenDevice(name)
            check(device != 0L) { "No audio device found!" }
            return AudioDevice(device)
        }
    }
}
