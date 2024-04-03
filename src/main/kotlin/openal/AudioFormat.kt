package garden.ephemeral.audio.openal

import org.lwjgl.openal.AL10.AL_FORMAT_MONO16
import org.lwjgl.openal.AL10.AL_FORMAT_MONO8
import org.lwjgl.openal.AL10.AL_FORMAT_STEREO16
import org.lwjgl.openal.AL10.AL_FORMAT_STEREO8

enum class AudioFormat(internal val openALValue: Int) {
    MONO8(AL_FORMAT_MONO8),
    MONO16(AL_FORMAT_MONO16),
    STEREO8(AL_FORMAT_STEREO8),
    STEREO16(AL_FORMAT_STEREO16),
}
