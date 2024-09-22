package garden.ephemeral.audio.uimodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import java.util.UUID

data class Cable<T : Any>(
    val sourcePortId: UUID,
    val destinationPortId: UUID,
    val sourceState: State<T>,
    val destinationState: MutableState<T>,
) {
    /**
     * Updates the destination state from the source state.
     * Has to be done inside the cable because it's the only place which knows that the two have
     * the same generic parameter.
     */
    fun updateValue() {
        destinationState.value = sourceState.value
    }
}
