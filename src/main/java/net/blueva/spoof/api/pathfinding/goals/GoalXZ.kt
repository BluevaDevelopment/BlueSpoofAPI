package net.blueva.spoof.api.pathfinding.goals

import net.blueva.spoof.api.pathfinding.Goal
import net.blueva.spoof.api.pathfinding.PathNode
import org.bukkit.Location
import kotlin.math.sqrt

/**
 * Goal that is satisfied by reaching an exact X/Z column, at any height the
 * fake player can stand on.
 *
 * @since 3.7
 */
class GoalXZ(
    /** Target block X. */
    val x: Int,
    /** Target block Z. */
    val z: Int
) : Goal {
    /**
     * Creates a goal for the column of a location.
     *
     * @param location target location (only X/Z are used)
     */
    constructor(location: Location) : this(location.blockX, location.blockZ)

    override fun isEnd(node: PathNode): Boolean {
        return node.x == x && node.z == z
    }

    override fun heuristic(node: PathNode): Double {
        val dx = node.x - x
        val dz = node.z - z
        return sqrt((dx * dx + dz * dz).toDouble())
    }

    override fun toString(): String {
        return "GoalXZ($x, $z)"
    }
}
