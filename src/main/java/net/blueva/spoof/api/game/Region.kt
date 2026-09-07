package net.blueva.spoof.api.game

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.math.min

/**
 * An axis-aligned box in a world, used for arena bounds, finish lines, safe zones, floors and any
 * other named piece of geometry a game exposes to bots.
 *
 * Corner order does not matter: the constructor normalises so that min is always below max on every
 * axis.
 *
 * @since 3.9
 */
class Region(
    /** The world this region is in. */
    @JvmField val world: World,
    x1: Double, y1: Double, z1: Double,
    x2: Double, y2: Double, z2: Double,
    /** An optional name, for regions that came from a named setup key. */
    @JvmField val name: String? = null
) {
    @JvmField val minX: Double = min(x1, x2)
    @JvmField val minY: Double = min(y1, y2)
    @JvmField val minZ: Double = min(z1, z2)
    @JvmField val maxX: Double = max(x1, x2)
    @JvmField val maxY: Double = max(y1, y2)
    @JvmField val maxZ: Double = max(z1, z2)

    /**
     * Creates a region from two corner locations, which must be in the same world.
     *
     * @since 3.9
     */
    constructor(corner1: Location, corner2: Location, name: String? = null) : this(
        corner1.world ?: throw IllegalArgumentException("corner1 has no world"),
        corner1.x, corner1.y, corner1.z,
        corner2.x, corner2.y, corner2.z,
        name
    )

    /** Whether the given location is inside this region, boundaries included. */
    fun contains(location: Location): Boolean {
        val locationWorld = location.world ?: return false
        if (locationWorld.name != world.name) return false
        return location.x >= minX && location.x <= maxX &&
            location.y >= minY && location.y <= maxY &&
            location.z >= minZ && location.z <= maxZ
    }

    /** The centre of this region. */
    fun center(): Location =
        Location(world, (minX + maxX) / 2.0, (minY + maxY) / 2.0, (minZ + maxZ) / 2.0)

    /** The size of this region on each axis. */
    fun size(): Vector = Vector(maxX - minX, maxY - minY, maxZ - minZ)

    /**
     * The shortest distance from the given location to this region, or `0.0` if it is inside.
     *
     * @since 3.9
     */
    fun distanceTo(location: Location): Double {
        val dx = max(0.0, max(minX - location.x, location.x - maxX))
        val dy = max(0.0, max(minY - location.y, location.y - maxY))
        val dz = max(0.0, max(minZ - location.z, location.z - maxZ))
        return Math.sqrt(dx * dx + dy * dy + dz * dz)
    }

    override fun toString(): String =
        "Region(${name ?: "unnamed"}, ${world.name}, $minX..$maxX, $minY..$maxY, $minZ..$maxZ)"
}
