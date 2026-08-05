package net.blueva.spoof.api.pathfinding.goals

import net.blueva.spoof.api.pathfinding.Goal
import net.blueva.spoof.api.pathfinding.PathNode
import org.bukkit.Location
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Goal that is satisfied by standing next to a block (within interaction
 * range), instead of on top of it — useful for reaching chests, doors,
 * buttons or crafting stations.
 *
 * @since 3.7
 */
class GoalGetToBlock(
    /** Target block X. */
    val x: Int,
    /** Target block Y. */
    val y: Int,
    /** Target block Z. */
    val z: Int
) : Goal {
    /**
     * Creates a goal for standing next to the block at a location.
     *
     * @param location block location
     */
    constructor(location: Location) : this(location.blockX, location.blockY, location.blockZ)

    override fun isEnd(node: PathNode): Boolean {
        val dx = node.x - x
        val dy = node.y - y
        val dz = node.z - z
        val horizontal = sqrt((dx * dx + dz * dz).toDouble())
        return horizontal <= REACH && abs(dy) <= 1
    }

    override fun heuristic(node: PathNode): Double {
        val dx = node.x - x
        val dy = node.y - y
        val dz = node.z - z
        return max(0.0, sqrt((dx * dx + dy * dy + dz * dz).toDouble()) - REACH)
    }

    override fun toString(): String {
        return "GoalGetToBlock($x, $y, $z)"
    }

    companion object {
        /** Horizontal distance considered "next to" the block, in blocks. */
        private const val REACH = 1.75
    }
}
