package net.blueva.spoof.api.pathfinding

import java.util.concurrent.CompletableFuture

/**
 * Drives a fake player toward [Goal]s using A* pathfinding over the world
 * and client-style movement input to follow the computed path.
 *
 * The pathfinder walks on land, steps up single blocks, drops down ledges
 * (up to [Movements.maxDropDown]), sprints when allowed, avoids liquids and
 * hazardous blocks, and recomputes the path when it gets stuck or when a
 * dynamic goal (like `net.blueva.spoof.api.pathfinding.goals.GoalFollow`)
 * moves.
 *
 * @since 3.7
 */
interface Pathfinder {
    /**
     * Starts navigating to the given goal and returns a future that completes
     * with the outcome.
     *
     * Starting a new navigation cancels any previous one (the previous future
     * completes with [PathStatus.CANCELLED]).
     *
     * @param goal the goal to reach
     * @return future completing with [PathStatus.SUCCESS] when the goal is
     * reached, or with a failure status ([PathStatus.NO_PATH],
     * [PathStatus.TIMEOUT], [PathStatus.STUCK], [PathStatus.CANCELLED], ...)
     * @since 3.7
     */
    fun goTo(goal: Goal): CompletableFuture<PathResult>

    /**
     * Starts navigating to the given goal without waiting for the outcome.
     * Use [goal] and the `FakePlayerGoalReachedEvent` /
     * `FakePlayerPathFailedEvent` events to observe completion, or pass
     * `null` to stop navigating (equivalent to [cancel]).
     *
     * Returns the goal the pathfinder is currently navigating to, or `null` if idle.
     *
     * @since 3.7
     */
    var goal: Goal?

    /**
     * Stops navigating at the next path node (a graceful stop: the fake player
     * finishes its current step instead of halting mid-motion). The pending
     * [goTo] future, if any, completes with [PathStatus.CANCELLED].
     *
     * @since 3.7
     */
    fun stop()

    /**
     * Immediately stops navigating and releases all movement controls.
     * The pending [goTo] future, if any, completes with [PathStatus.CANCELLED].
     *
     * @since 3.7
     */
    fun cancel()

    /**
     * Returns whether the pathfinder is currently navigating to a goal.
     *
     * @since 3.7
     */
    val isNavigating: Boolean

    /**
     * Returns whether the fake player is currently moving under the
     * pathfinder's control.
     *
     * @since 3.7
     */
    val isMoving: Boolean

    /**
     * The movement configuration currently in use. The returned object is a
     * live view: mutating it affects ongoing and future navigations. Setting
     * it replaces the configuration (a defensive copy is stored).
     *
     * @since 3.7
     */
    var movements: Movements

    /**
     * Returns a snapshot of the path currently being followed.
     *
     * @return immutable copy of the remaining path nodes, empty if idle
     * @since 3.7
     */
    val currentPath: List<PathNode>

    /**
     * Computes a path to the given goal without moving, using the current
     * [Movements]. Useful for previewing or measuring routes.
     *
     * @param goal the goal to plan for
     * @return future completing with the computed path (the fake player does
     * not move; [PathResult.status] is [PathStatus.SUCCESS] if a full path
     * was found)
     * @since 3.7
     */
    fun getPathTo(goal: Goal): CompletableFuture<PathResult>
}
