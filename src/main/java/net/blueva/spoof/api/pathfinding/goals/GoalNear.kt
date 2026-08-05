package net.blueva.spoof.api.pathfinding.goals

import net.blueva.spoof.api.pathfinding.Goal
import net.blueva.spoof.api.pathfinding.PathNode
import org.bukkit.Location
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Goal that is satisfied by standing within a radius of a point (3D distance).
 *
 * @since 3.7
 */
class GoalNear(
    /** Center block X. */
    val x: Int,
    /** Center block Y. */
    val y: Int,
    /** Center block Z. */
    val z: Int,
    range: Double
) : Goal {
    /** Acceptable distance in blocks. */
    val range: Double = max(0.0, range)

    /**
     * Creates a goal for the floored coordinates of a location and a radius.
     *
     * @param location center
     * @param range    acceptable distance in blocks
     */
    constructor(location: Location, range: Double) : this(
        location.blockX,
        location.blockY,
        location.blockZ,
        range
    )

    override fun isEnd(node: PathNode): Boolean {
        val dx = node.x - x
        val dy = node.y - y
        val dz = node.z - z
        return sqrt((dx * dx + dy * dy + dz * dz).toDouble()) <= range
    }

    override fun heuristic(node: PathNode): Double {
        val dx = node.x - x
        val dy = node.y - y
        val dz = node.z - z
        return max(0.0, sqrt((dx * dx + dy * dy + dz * dz).toDouble()) - range)
    }

    override fun toString(): String {
        return "GoalNear($x, $y, $z, range=$range)"
    }
}
