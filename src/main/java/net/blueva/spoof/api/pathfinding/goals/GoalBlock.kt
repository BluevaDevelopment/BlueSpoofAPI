package net.blueva.spoof.api.pathfinding.goals

import net.blueva.spoof.api.pathfinding.Goal
import net.blueva.spoof.api.pathfinding.PathNode
import org.bukkit.Location
import kotlin.math.sqrt

/**
 * Goal that is satisfied by standing on an exact block.
 *
 * @since 3.7
 */
class GoalBlock(
    /** Target block X. */
    @kotlin.jvm.JvmField val x: Int,
    /** Target block Y. */
    @kotlin.jvm.JvmField val y: Int,
    /** Target block Z. */
    @kotlin.jvm.JvmField val z: Int
) : Goal {
    /**
     * Creates a goal for the floored coordinates of a location.
     *
     * @param location target location
     */
    constructor(location: Location) : this(location.blockX, location.blockY, location.blockZ)

    override fun isEnd(node: PathNode): Boolean {
        return node.x == x && node.y == y && node.z == z
    }

    override fun heuristic(node: PathNode): Double {
        val dx = node.x - x
        val dy = node.y - y
        val dz = node.z - z
        return sqrt((dx * dx + dy * dy + dz * dz).toDouble())
    }

    override fun toString(): String {
        return "GoalBlock($x, $y, $z)"
    }
}
