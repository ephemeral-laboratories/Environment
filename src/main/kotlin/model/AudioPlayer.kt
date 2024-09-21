package garden.ephemeral.audio.model

import com.google.common.flogger.FluentLogger
import garden.ephemeral.audio.openal.AudioBuffer
import garden.ephemeral.audio.openal.AudioContext
import garden.ephemeral.audio.openal.AudioDevice
import garden.ephemeral.audio.openal.AudioFormat
import garden.ephemeral.audio.openal.AudioSource
import garden.ephemeral.audio.openal.AudioSourceState
import garden.ephemeral.audio.units.Decibel
import garden.ephemeral.audio.units.Hertz
import garden.ephemeral.audio.units.dB
import garden.ephemeral.audio.util.initializingResources
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedDeque


/**
 * A buffer consumer which plays the audio to a real output device.
 *
 * @property bufferSupplier a supplier of buffers to be played.
 * @property format the audio format to use.
 * @property sampleRate the sample rate in Hertz.
 */
class AudioPlayer(bufferSupplier: BufferSupplier, val format: AudioFormat, val sampleRate: Hertz) : AutoCloseable {
    private sealed class AudioPlayerMessage {
        data object UpdateVolume : AudioPlayerMessage()
        data object UpdateBufferSupplier : AudioPlayerMessage()
        data object Shutdown : AudioPlayerMessage()
    }

    private val messageQueue: Queue<AudioPlayerMessage> = ConcurrentLinkedDeque()

    @Volatile
    var volume: Decibel = (-60).dB
        set(value) {
            field = value
            logger.atFine().log("Posting message: %s", AudioPlayerMessage.UpdateVolume)
            messageQueue.offer(AudioPlayerMessage.UpdateVolume)
        }

    val volumeRange: ClosedRange<Decibel> = (-60).dB..0.dB

    @Volatile
    var bufferSupplier: BufferSupplier = bufferSupplier
        set(value) {
            field = value
            logger.atFine().log("Posting message: %s", AudioPlayerMessage.UpdateBufferSupplier)
            messageQueue.offer(AudioPlayerMessage.UpdateBufferSupplier)
        }

    private var thread: Thread? = null

    private inner class Runner : Runnable {
        private val lockObject = Object()

        private var volume = this@AudioPlayer.volume
        private var bufferSupplier = this@AudioPlayer.bufferSupplier
        private var isClosed = false
        private var isRunning = false

        private fun triggerPlayback() {
            synchronized(lockObject) {
                logger.atFine().log("Triggering playback")
                isRunning = true
                lockObject.notifyAll()
            }
        }

        private fun pollForMessages() {
            synchronized(lockObject) {
                while (true) {
                    val message = messageQueue.poll() ?: break
                    logger.atFine().log("Processing message: $message")
                    when (message) {
                        AudioPlayerMessage.UpdateVolume -> volume = this@AudioPlayer.volume

                        AudioPlayerMessage.UpdateBufferSupplier -> {
                            bufferSupplier = this@AudioPlayer.bufferSupplier
                            triggerPlayback()
                        }

                        AudioPlayerMessage.Shutdown -> {
                            isClosed = true
                            triggerPlayback()
                        }
                    }
                }
            }
        }

        private fun waitUntilRunning() {
            synchronized(lockObject) {
                while (!isRunning) {
                    lockObject.wait(MESSAGE_QUEUE_POLL_PERIOD_MILLIS)
                    // Have to process messages here too, else the resume would never get through
                    pollForMessages()
                }
            }
        }

        override fun run() {
            var device: AudioDevice
            var context: AudioContext
            var source: AudioSource = AudioSource.NULL
            lateinit var buffers: Array<AudioBuffer>

            var bufferIndex = 0

            fun bufferSamples(samples: ShortArray) {
                synchronized(lockObject) {
                    logger.atFine().log("Enqueueing buffer, index $bufferIndex")
                    val buffer = buffers[bufferIndex]
                    bufferIndex = (bufferIndex + 1) % BUFFER_COUNT

                    buffer.bufferData(format, samples, sampleRate)
                    source.queueBuffer(buffer)
                }
            }

            synchronized(lockObject) {
                initializingResources {
                    logger.atFine().log("Opening default audio device...")
                    device = AudioDevice.openDefaultDevice().also(::closeLater)
                    logger.atFine().log("Opened audio device: $device")
                    context = AudioContext.create(device, intArrayOf(0)).also(::closeLater)
                    context.makeCurrent()
                    device.createCapabilities()

                    buffers = AudioBuffer.generate(BUFFER_COUNT)
                    closeLater { AudioBuffer.close(buffers) }

                    source = AudioSource.generate().also(::closeLater)

                    // Feed some dummy buffers into the source to get things started.
                    for (i in 0 until BUFFER_COUNT) {
                        bufferSamples(ShortArray(0))
                    }

                    device.detectInternalExceptions()
                }.use {
                    outerLoop@
                    while (!isClosed) {
                        pollForMessages()
                        waitUntilRunning()

                        // Stop can happen during pause
                        if (isClosed) {
                            break
                        }

                        source.gain = volume.toGain()

                        val buffersProcessed = source.buffersProcessed
                        logger.atFine().log("Buffers processed: $buffersProcessed")
                        for (i in 0 until buffersProcessed) {
                            logger.atFine().log("Getting buffer to play, from $bufferSupplier")
                            val samples = bufferSupplier.supply(BUFFER_SIZE)
                            if (samples == null) {
                                isRunning = false
                                break
                            }
                            logger.atFine().log("Recreating buffer at index $bufferIndex")
                            source.unqueueBuffer().close()
                            buffers[bufferIndex] = AudioBuffer.generate()
                            logger.atFine().log("Buffering samples")
                            bufferSamples(samples)
                        }
                        val sourceState = source.sourceState
                        logger.atFine().log("Source state was $sourceState")
                        if (sourceState != AudioSourceState.PLAYING) {
                            logger.atFine().log("Playing source")
                            source.play()
                        }
                        device.detectInternalExceptions()
                    }
                }
            }
        }
    }

    fun start() {
        var thread = this.thread
        if (thread == null) {
            logger.atFine().log("Starting AudioPlayer thread...")
            thread = Thread(Runner(), "AudioPlayer")
            this.thread = thread
            thread.start()
            logger.atFine().log("Thread started.")
        }
    }

    override fun close() {
        val thread = this.thread
        if (thread != null) {
            this.thread = null
            logger.atFine().log("Posting message: ${AudioPlayerMessage.Shutdown}")
            messageQueue.offer(AudioPlayerMessage.Shutdown)
            logger.atFine().log("Waiting for thread to finish...")
            thread.join()
            logger.atFine().log("Thread finished.")
        }
    }

    companion object {
        // TODO: What is a good buffer size? The video I saw said 512, but I see 1024 and 2048 stated more commonly. Should it be configurable? Why?
        internal const val BUFFER_SIZE = 2048
        private const val BUFFER_COUNT = 16

        // TODO: What is a good period?
        private const val MESSAGE_QUEUE_POLL_PERIOD_MILLIS = 100L

        private val logger: FluentLogger = FluentLogger.forEnclosingClass()
    }
}
