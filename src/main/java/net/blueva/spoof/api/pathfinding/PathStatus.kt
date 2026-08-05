package net.blueva.spoof.api.pathfinding

/**
 * Outcome status of a pathfinding request.
 *
 * @since 3.7
 */
enum class PathStatus {
    /** The goal was reached. */
    SUCCESS,

    /** No path to the goal exists within the configured search limits. */
    NO_PATH,

    /** The search took longer than [Movements.thinkTimeoutMillis]. */
    TIMEOUT,

    /** The fake player got stuck and could not make progress after retries. */
    STUCK,

    /** Navigation was cancelled (by [Pathfinder.stop], [Pathfinder.cancel] or a newer navigation request). */
    CANCELLED,

    /** A dynamic goal moved and the path was recomputed (intermediate status; navigation continues with the new path). */
    GOAL_CHANGED,

    /** The fake player went offline or the navigation was interrupted externally. */
    INTERRUPTED,

    /** Pathfinding is not supported in the current context (e.g. the fake player is offline or in an unloaded world). */
    UNSUPPORTED
}
