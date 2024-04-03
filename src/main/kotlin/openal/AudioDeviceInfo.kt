package garden.ephemeral.audio.openal

import org.lwjgl.openal.ALC10

/**
 * Holder for audio device info from an enumeration.
 *
 * @property name the name of the audio device.
 */
data class AudioDeviceInfo(val name: String) {
    /**
     * Opens the device.
     *
     * @return a new device. If you open a device, you should [close][AudioDevice.close] it!
     */
    fun open() = AudioDevice(
        ALC10.alcOpenDevice(ALC10.alcGetString(0L, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER))
    )
}
