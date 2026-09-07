package net.blueva.spoof.api.learning

import java.time.Duration
import java.util.Optional
import java.util.concurrent.CompletableFuture

/**
 * Where demonstrations are kept.
 *
 * Reads are asynchronous because a demonstration is thousands of frames on disk and a behaviour
 * asking for one must never block a server tick. Load what you need when a match starts, not while
 * it is running.
 *
 * @since 3.9
 */
interface DemonstrationStore {
    /**
     * Stores a demonstration.
     *
     * @since 3.9
     */
    fun record(demonstration: Demonstration): CompletableFuture<Void>

    /**
     * Loads demonstrations matching the query, most recent first.
     *
     * @since 3.9
     */
    fun query(query: DemoQuery): CompletableFuture<List<Demonstration>>

    /**
     * Loads one demonstration by id.
     *
     * @since 3.9
     */
    fun byId(id: String): CompletableFuture<Optional<Demonstration>>

    /**
     * How many demonstrations are stored for a map, without loading any of them.
     *
     * @since 3.9
     */
    fun count(gameId: String, mapKey: String): Int

    /**
     * Deletes demonstrations older than [olderThan], keeping at least [keepPerMap] of the most
     * recent for every map regardless of age.
     *
     * @since 3.9
     */
    fun prune(olderThan: Duration, keepPerMap: Int): CompletableFuture<Int>
}
