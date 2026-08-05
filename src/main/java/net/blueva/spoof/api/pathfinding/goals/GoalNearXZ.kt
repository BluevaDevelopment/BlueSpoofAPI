package net.blueva.spoof.api.pathfinding.goals

import net.blueva.spoof.api.pathfinding.Goal
import net.blueva.spoof.api.pathfinding.PathNode
import org.bukkit.Location
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Goal that is satisfied by standing within a horizontal radius of a column,
 * at any height the fake player can stand on.
 *
 * @since 3.7
 */
class GoalNearXZ(
    /** Center block X. */
    val x: Int,
    /** Center block Z. */
    val z: Int,
    range: Double
) : Goal {
    /** Acceptable horizontal distance in blocks. */
    val range: Double = max(0.0, range)

    /**
     * Creates a goal for the column of a location and a horizontal radius.
     *
     * @param location center (only X/Z are used)
     * @param range    acceptable horizontal distance in blocks
     */
    constructor(location: Location, range: Double) : this(location.blockX, location.blockZ, range)

    override fun isEnd(node: PathNode): Boolean {
        val dx = node.x - x
        val dz = node.z - z
        return sqrt((dx * dx + dz * dz).toDouble()) <= range
    }

    override fun heuristic(node: PathNode): Double {
        val dx = node.x - x
        val dz = node.z - z
        return max(0.0, sqrt((dx * dx + dz * dz).toDouble()) - range)
    }

    override fun toString(): String {
        return "GoalNearXZ($x, $z, range=$range)"
    }
}
