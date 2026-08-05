package net.blueva.spoof.api.pathfinding

import java.util.Collections

/**
 * Result of a pathfinding request.
 *
 * @since 3.7
 */
class PathResult(
    /** The outcome status. */
    val status: PathStatus,
    path: MutableList<PathNode>,
    /** The node the fake player ended on, or `null` if navigation never started. */
    val finalNode: PathNode?,
    /** How many nodes the A* search expanded. */
    val nodesExplored: Int
) {
    /** The path followed (or the best partial path found on failure), immutable. */
    val path: MutableList<PathNode> = Collections.unmodifiableList(ArrayList(path))

    /**
     * Returns whether the goal was reached.
     *
     * @return `true` if [status] is [PathStatus.SUCCESS]
     */
    val isSuccess: Boolean
        get() = status == PathStatus.SUCCESS

    override fun toString(): String {
        return "PathResult($status, nodes=${path.size}, explored=$nodesExplored)"
    }
}
