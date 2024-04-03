package garden.ephemeral.audio.openal

import org.lwjgl.openal.AL10

enum class AudioSourceState(val openALValue: Int) {
    INITIAL(AL10.AL_INITIAL),
    PLAYING(AL10.AL_PLAYING),
    PAUSED(AL10.AL_PAUSED),
    STOPPED(AL10.AL_STOPPED),
    ;

    companion object {
        fun Int.toAudioSourceState(): AudioSourceState {
            return AudioSourceState.entries.find { e -> e.openALValue == this }
                ?: throw IllegalArgumentException("Invalid source state: $this")
        }
    }
}
