package garden.ephemeral.audio.openal

import org.lwjgl.openal.AL10
import org.lwjgl.openal.AL10.AL_INVALID_ENUM
import org.lwjgl.openal.AL10.AL_INVALID_NAME
import org.lwjgl.openal.AL10.AL_INVALID_OPERATION
import org.lwjgl.openal.AL10.AL_INVALID_VALUE
import org.lwjgl.openal.AL10.AL_OUT_OF_MEMORY
import org.lwjgl.openal.ALC10

class OpenALException(errorCode: Int) : RuntimeException(formatMessage(errorCode)) {
    companion object {
        private fun formatMessage(errorCode: Int): String {
            val type = when (errorCode) {
                AL_INVALID_NAME -> "invalid name"
                AL_INVALID_ENUM -> "invalid enum"
                AL_INVALID_VALUE -> "invalid value"
                AL_INVALID_OPERATION -> "invalid operation"
                AL_OUT_OF_MEMORY -> "out of memory"
                else -> "unknown"
            }
            return "Internal OpenAL exception: $type"
        }

        fun throwIfError() {
            val errorCode = AL10.alGetError()
            if (errorCode != AL10.AL_NO_ERROR) {
                throw OpenALException(errorCode)
            }
        }

        fun throwIfError(deviceHandle: Long) {
            val errorCode = ALC10.alcGetError(deviceHandle)
            if (errorCode != ALC10.ALC_NO_ERROR) {
                throw OpenALException(errorCode)
            }
        }
    }
}
