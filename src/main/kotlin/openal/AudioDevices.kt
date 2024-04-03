package garden.ephemeral.audio.openal

import org.lwjgl.openal.ALC10.ALC_DEVICE_SPECIFIER
import org.lwjgl.openal.ALC11
import org.lwjgl.openal.ALC11.ALC_CAPTURE_DEVICE_SPECIFIER
import org.lwjgl.openal.ALUtil
import org.lwjgl.openal.EnumerateAllExt

object AudioDevices {
    private fun enumerateDevices(typeToken: Int): Sequence<AudioDeviceInfo> {
        val names = ALUtil.getStringList(0, typeToken)
        OpenALException.throwIfError(0)
        if (names == null) return emptySequence()
        return names.asSequence().map(::AudioDeviceInfo)
    }

    /**
     * Enumerates the available playback devices on the current system.
     *
     * @return a sequence of the available playback devices.
     */
    fun enumeratePlaybackDevices(): Sequence<AudioDeviceInfo> {
        // I heard something about OpenAL 1.1 not needing this check anymore, but the code I saw that comment
        // on was still doing the check, so I have zero trust in their claim.
        val typeToken = if (ALC11.alcIsExtensionPresent(0, "ALC_ENUMERATION_EXT")) {
            EnumerateAllExt.ALC_ALL_DEVICES_SPECIFIER
        } else {
            ALC_DEVICE_SPECIFIER
        }
        return enumerateDevices(typeToken)
    }

    /**
     * Enumerates the available capture devices on the current system.
     *
     * @return a sequence of the available capture devices.
     */
    fun enumerateCaptureDevices(): Sequence<AudioDeviceInfo> {
        return enumerateDevices(ALC_CAPTURE_DEVICE_SPECIFIER)
    }
}
