package net.blueva.spoof.api.learning

import java.util.Optional

/**
 * Where [PolicyProvider]s are registered and policies are resolved.
 *
 * @since 3.9
 */
interface PolicyRegistry {
    /**
     * Registers a provider, replacing any provider already registered with the same id.
     *
     * @since 3.9
     */
    fun register(provider: PolicyProvider)

    /**
     * Unregisters a provider by id.
     *
     * @since 3.9
     */
    fun unregister(id: String)

    /**
     * Every registered provider id, highest priority first.
     *
     * @since 3.9
     */
    fun providers(): List<String>

    /**
     * The policy to use for a game on a map, asking every provider in priority order.
     *
     * @since 3.9
     */
    fun resolve(gameId: String, mapKey: String): Optional<Policy>
}
