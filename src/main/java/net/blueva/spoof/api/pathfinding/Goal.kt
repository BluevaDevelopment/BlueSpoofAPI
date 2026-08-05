package net.blueva.spoof.api.pathfinding

/**
 * A navigation goal for the [Pathfinder].
 *
 * A goal answers two questions: "is this node an acceptable destination?"
 * ([isEnd]) and "how good is this node as a step towards the destination?"
 * ([heuristic], an admissible-ish distance estimate driving the A* search).
 *
 * Ready-made implementations live in `net.blueva.spoof.api.pathfinding.goals`:
 * stand on an exact block (`GoalBlock`), get within a radius (`GoalNear`,
 * `GoalNearXZ`), reach a column (`GoalXZ`), stand next to a block
 * (`GoalGetToBlock`) or follow an entity (`GoalFollow`). Plugins can implement
 * this interface for custom goals.
 *
 * @since 3.7
 */
interface Goal {
    /**
     * Returns whether standing on the given node satisfies the goal.
     *
     * @param node a candidate node (block coordinates of the feet position)
     * @return `true` if the goal is reached at `node`
     * @since 3.7
     */
    fun isEnd(node: PathNode): Boolean

    /**
     * Returns an estimate of the remaining distance from the given node to the
     * goal, in blocks. Lower values make the A* prefer the node.
     *
     * @param node a candidate node
     * @return estimated remaining distance
     * @since 3.7
     */
    fun heuristic(node: PathNode): Double

    /**
     * Returns whether this goal can move over time (e.g. following an entity).
     * Dynamic goals make the pathfinder periodically recompute the path and
     * never fire a "goal reached" completion until the fake player actually
     * stands still within range.
     *
     * @return `true` if the goal's target changes over time
     * @since 3.7
     */
    fun isDynamic(): Boolean {
        return false
    }
}
