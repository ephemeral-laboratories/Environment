package garden.ephemeral.audio.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

private fun closeChain(resources: List<AutoCloseable>) {
    if (resources.isEmpty()) return
    try {
        resources.last().close()
    } finally {
        resources.dropLast(1).also(::closeChain)
    }
}

/**
 * Scope used for the block when calling [initializingResources].
 */
class ResourceInitializationScope {
    private val resourcesToCloseLater = mutableListOf<AutoCloseable>()

    /**
     * Adds a resource to be closed later.
     *
     * @param resource the resource.
     */
    fun closeLater(resource: AutoCloseable) {
        resourcesToCloseLater.add(resource)
    }

    /**
     * Adds multiple resources to be closed later.
     *
     * @param resources the resources.
     */
    fun closeLater(resources: Array<out AutoCloseable>) {
        resourcesToCloseLater.addAll(resources)
    }

    fun closeNow() = closeChain(resourcesToCloseLater)
}

/**
 * Begins a block to initialize multiple resources.
 *
 * This is done as safely as possible. If any failure occurs, all previously-initialized resources are closed.
 * Resources are closed in order of most recently initialized.
 *
 * @param block the block of code to run.
 * @return an object which can be closed later to close all resources opened during the block.
 */
@OptIn(ExperimentalContracts::class)
inline fun initializingResources(block: ResourceInitializationScope.() -> Unit): AutoCloseable {
    // Needed to ensure the caller that the method is not called more than once.
    // TODO: Technically if an error is thrown from `ResourceInitializationScope` it's called zero times.
    //       Read the contract API carefully to figure out what it means when it says how many calls.
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    val scope = ResourceInitializationScope()
    var success = false
    try {
        scope.apply(block)
        success = true
        return AutoCloseable { scope.closeNow() }
    } finally {
        if (!success) scope.closeNow()
    }
}
