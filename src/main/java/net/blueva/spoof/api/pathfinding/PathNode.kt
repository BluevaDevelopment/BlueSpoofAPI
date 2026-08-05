package net.blueva.spoof.api.pathfinding

import org.bukkit.Location
import org.bukkit.World
import kotlin.math.sqrt

/**
 * A node in a navigation path: the block coordinates of the fake player's feet.
 *
 * @since 3.7
 */
class PathNode(
    /** Block X. */
    val x: Int,
    /** Block Y (feet level). */
    val y: Int,
    /** Block Z. */
    val z: Int
) {

    /**
     * Returns the Euclidean distance between this node and another.
     *
     * @param other another node
     * @return distance in blocks
     */
    fun distanceTo(other: PathNode): Double {
        val dx = x - other.x
        val dy = y - other.y
        val dz = z - other.z
        return sqrt((dx * dx + dy * dy + dz * dz).toDouble())
    }

    /**
     * Returns the horizontal (X/Z) distance between this node and another.
     *
     * @param other another node
     * @return horizontal distance in blocks
     */
    fun horizontalDistanceTo(other: PathNode): Double {
        val dx = x - other.x
        val dz = z - other.z
        return sqrt((dx * dx + dz * dz).toDouble())
    }

    /**
     * Returns the location of the center of this block in the given world.
     *
     * @param world the world
     * @return centered location
     */
    fun toCenterLocation(world: World?): Location {
        return Location(world, x + 0.5, y.toDouble(), z + 0.5)
    }

    /**
     * Returns the location of this block's corner in the given world.
     *
     * @param world the world
     * @return block-corner location
     */
    fun toLocation(world: World?): Location {
        return Location(world, x.toDouble(), y.toDouble(), z.toDouble())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is PathNode) {
            return false
        }
        return x == other.x && y == other.y && z == other.z
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }

    override fun toString(): String {
        return "PathNode($x, $y, $z)"
    }

    companion object {
        /**
         * Creates a node from the floored coordinates of a location.
         *
         * @param location a location
         * @return node at the block containing `location`
         */
        @JvmStatic
        fun of(location: Location): PathNode {
            return PathNode(location.blockX, location.blockY, location.blockZ)
        }
    }
}
