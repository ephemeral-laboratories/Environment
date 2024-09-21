package garden.ephemeral.audio.uimodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.geometry.Offset
import java.util.UUID
import kotlin.reflect.KClass

interface PortRegistry {
    fun <T : Any> registerOutputPort(id: UUID, type: KClass<T>, valueState: State<T>): Registration
    fun <T : Any> registerInputPort(id: UUID, type: KClass<T>, valueState: MutableState<T>): Registration

    fun getPortLocation(id: UUID): Offset
    fun updatePortLocation(id: UUID, location: Offset)

    interface Registration {
        fun unregister()
    }
}

data class OutputPortInfo<T : Any>(
    val id: UUID,
    val type: KClass<T>,
    val valueState: State<T>,
)

data class InputPortInfo<T : Any>(
    val id: UUID,
    val type: KClass<T>,
    val valueState: MutableState<T>,
)

internal class PortRegistryImpl : PortRegistry {
    private val outputPortStorage = mutableStateMapOf<UUID, OutputPortInfo<*>>()
    private val inputPortStorage = mutableStateMapOf<UUID, InputPortInfo<*>>()
    private val portLocations = mutableStateMapOf<UUID, Offset>()

    override fun <T : Any> registerOutputPort(
        id: UUID,
        type: KClass<T>,
        valueState: State<T>
    ): PortRegistry.Registration {
        outputPortStorage[id] = OutputPortInfo(id, type, valueState)
        return object : PortRegistry.Registration {
            override fun unregister() {
                outputPortStorage.remove(id)
            }
        }
    }

    override fun <T : Any> registerInputPort(
        id: UUID,
        type: KClass<T>,
        valueState: MutableState<T>
    ): PortRegistry.Registration {
        inputPortStorage[id] = InputPortInfo(id, type, valueState)
        return object : PortRegistry.Registration {
            override fun unregister() {
                inputPortStorage.remove(id)
            }
        }
    }

    override fun getPortLocation(id: UUID) = portLocations[id] ?: Offset.Zero

    override fun updatePortLocation(id: UUID, location: Offset) {
        portLocations[id] = location
    }
}

fun PortRegistry() = PortRegistryImpl() as PortRegistry

val LocalPortRegistry = compositionLocalOf<PortRegistry> { error("CompositionLocal LocalPortRegistry not present") }
