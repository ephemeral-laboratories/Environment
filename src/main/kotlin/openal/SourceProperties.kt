package garden.ephemeral.audio.openal

import org.lwjgl.openal.AL10.AL_BUFFER
import org.lwjgl.openal.AL10.AL_BUFFERS_PROCESSED
import org.lwjgl.openal.AL10.AL_BUFFERS_QUEUED
import org.lwjgl.openal.AL10.AL_CONE_INNER_ANGLE
import org.lwjgl.openal.AL10.AL_CONE_OUTER_ANGLE
import org.lwjgl.openal.AL10.AL_CONE_OUTER_GAIN
import org.lwjgl.openal.AL10.AL_DIRECTION
import org.lwjgl.openal.AL10.AL_GAIN
import org.lwjgl.openal.AL10.AL_MAX_DISTANCE
import org.lwjgl.openal.AL10.AL_MAX_GAIN
import org.lwjgl.openal.AL10.AL_MIN_GAIN
import org.lwjgl.openal.AL10.AL_PITCH
import org.lwjgl.openal.AL10.AL_POSITION
import org.lwjgl.openal.AL10.AL_REFERENCE_DISTANCE
import org.lwjgl.openal.AL10.AL_ROLLOFF_FACTOR
import org.lwjgl.openal.AL10.AL_SOURCE_RELATIVE
import org.lwjgl.openal.AL10.AL_SOURCE_STATE
import org.lwjgl.openal.AL10.AL_VELOCITY

enum class IntSourceProperty(internal val openALValue: Int) {
    SOURCE_RELATIVE(AL_SOURCE_RELATIVE),
    BUFFER(AL_BUFFER),
    SOURCE_STATE(AL_SOURCE_STATE),
    BUFFERS_QUEUED(AL_BUFFERS_QUEUED),
    BUFFERS_PROCESSED(AL_BUFFERS_PROCESSED),
}

enum class FloatSourceProperty(internal val openALValue: Int) {
    PITCH(AL_PITCH),
    GAIN(AL_GAIN),
    MIN_GAIN(AL_MIN_GAIN),
    MAX_GAIN(AL_MAX_GAIN),
    MAX_DISTANCE(AL_MAX_DISTANCE),
    ROLLOFF_FACTOR(AL_ROLLOFF_FACTOR),
    CONE_OUTER_GAIN(AL_CONE_OUTER_GAIN),
    CONE_INNER_ANGLE(AL_CONE_INNER_ANGLE),
    CONE_OUTER_ANGLE(AL_CONE_OUTER_ANGLE),
    REFERENCE_DISTANCE(AL_REFERENCE_DISTANCE),
}

enum class VectorSourceProperty(internal val openALValue: Int) {
    POSITION(AL_POSITION),
    VELOCITY(AL_VELOCITY),
    DIRECTION(AL_DIRECTION),
}
