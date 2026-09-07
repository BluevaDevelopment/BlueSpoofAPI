package net.blueva.spoof.api.learning

import java.util.Optional

/**
 * Supplies policies for games and maps.
 *
 * Register one to give BlueSpoof's bots your own decision making, whether it is a hand-written rule
 * set, a table you fitted yourself, or a model you trained offline. Providers are asked in priority
 * order and the first one that answers wins, so a specific provider can override a general one for
 * one game without replacing anything else.
 *
 * @since 3.9
 */
interface PolicyProvider {
    /** A stable id. */
    fun id(): String

    /** Higher is asked first. */
    fun priority(): Int

    /** Whether this provider has anything to offer for the given game. */
    fun supports(gameId: String): Boolean

    /**
     * The policy to use for this game on this map, or an empty optional to let the next provider
     * answer.
     *
     * Called when a behaviour starts a match, not per tick, so it may do real work such as reading
     * demonstrations or loading a model. It must still not block the server thread; return a policy
     * that answers `PolicyAction.none()` until its data has loaded if loading is slow.
     *
     * @since 3.9
     */
    fun load(gameId: String, mapKey: String): Optional<Policy>
}
