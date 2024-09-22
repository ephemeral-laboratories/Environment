package garden.ephemeral.audio.uimodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import java.util.UUID
import kotlin.reflect.KClass

interface PortRegistry {
    fun <T : Any> registerOutputPort(id: UUID, type: KClass<T>, valueState: State<T>): Registration
    fun <T : Any> registerInputPort(id: UUID, type: KClass<T>, valueState: MutableState<T>): Registration

    fun outputById(id: UUID): OutputPortInfo<*>
    fun inputById(id: UUID): InputPortInfo<*>

    fun <T : Any> outputsByClass(type: KClass<T>): List<OutputPortInfo<T>>
    fun <T : Any> inputsByClass(type: KClass<T>): List<InputPortInfo<T>>

    fun getPortOffset(id: UUID): Offset
    fun updateRootLocation(location: LayoutCoordinates)
    fun updatePortLocation(id: UUID, location: LayoutCoordinates)

    val allCables: Set<Cable<*>>
    fun isPortConnected(portId: UUID): Boolean
    fun addCable(cable: Cable<*>)
    fun removeCable(cable: Cable<*>)

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

    private lateinit var rootLocation: LayoutCoordinates
    private val portLocations = mutableStateMapOf<UUID, Offset>()

    private val outputPortConnections = mutableStateMapOf<UUID, Set<Cable<*>>>()
    private val inputPortConnections = mutableStateMapOf<UUID, Cable<*>>()

    private val _allCables = mutableStateMapOf<Cable<*>, Unit>()
    override val allCables: Set<Cable<*>>
        get() = _allCables.keys

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

    override fun outputById(id: UUID): OutputPortInfo<*> {
        return outputPortStorage[id] ?: throw IllegalArgumentException("No output port found for $id")
    }

    override fun inputById(id: UUID): InputPortInfo<*> {
        return inputPortStorage[id] ?: throw IllegalArgumentException("No input port found for $id")
    }

    override fun <T : Any> outputsByClass(type: KClass<T>): List<OutputPortInfo<T>> {
        @Suppress("UNCHECKED_CAST")
        return outputPortStorage.values.filter { info -> info.type == type } as List<OutputPortInfo<T>>
    }

    override fun <T : Any> inputsByClass(type: KClass<T>): List<InputPortInfo<T>> {
        @Suppress("UNCHECKED_CAST")
        return inputPortStorage.values.filter { info -> info.type == type } as List<InputPortInfo<T>>
    }

    override fun getPortOffset(id: UUID) = portLocations[id] ?: Offset.Zero

    override fun updateRootLocation(location: LayoutCoordinates) {
        rootLocation = location
    }

    override fun updatePortLocation(id: UUID, location: LayoutCoordinates) {
        portLocations[id] = rootLocation.localPositionOf(location, Offset.Zero)
    }

    override fun isPortConnected(portId: UUID): Boolean {
        return outputPortConnections.containsKey(portId) || inputPortConnections.containsKey(portId)
    }

    override fun addCable(cable: Cable<*>) {
        // Performing the operation which may fail first
        inputPortConnections.merge(cable.destinationPortId, cable) { oldValue, _ ->
            throw IllegalStateException("Input ${cable.destinationPortId} is already connected! $oldValue")
        }

        outputPortConnections.merge(cable.sourcePortId, setOf(cable), Set<Cable<*>>::plus)
        _allCables[cable] = Unit
    }

    override fun removeCable(cable: Cable<*>) {
        _allCables.remove(cable)
        inputPortConnections.remove(cable.destinationPortId)
        while (true) {
            val existing = outputPortConnections.getValue(cable.sourcePortId)
            if (outputPortConnections.replace(cable.sourcePortId, existing, existing - cable)) {
                break
            }
        }
    }
}

fun PortRegistry() = PortRegistryImpl() as PortRegistry

val LocalPortRegistry = compositionLocalOf<PortRegistry> { error("CompositionLocal LocalPortRegistry not present") }
