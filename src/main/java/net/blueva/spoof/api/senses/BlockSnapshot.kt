package net.blueva.spoof.api.senses

import org.bukkit.Material
import java.util.Optional

/**
 * An immutable cube of block data captured at one instant, for behaviours that reason over an area
 * rather than individual blocks.
 *
 * Coordinates are absolute world coordinates, not offsets, so a snapshot can be passed around and
 * compared against another taken later without tracking where it came from.
 *
 * @since 3.9
 */
interface BlockSnapshot {
    /** Inclusive minimum world coordinates of the captured cube. */
    fun minX(): Int
    fun minY(): Int
    fun minZ(): Int

    /** Inclusive maximum world coordinates of the captured cube. */
    fun maxX(): Int
    fun maxY(): Int
    fun maxZ(): Int

    /** The server tick at which the cube was captured. */
    fun tick(): Long

    /** Whether the given coordinates fall inside this snapshot. */
    fun contains(x: Int, y: Int, z: Int): Boolean =
        x >= minX() && x <= maxX() && y >= minY() && y <= maxY() && z >= minZ() && z <= maxZ()

    /**
     * The material at the given world coordinates, or [Material.AIR] if the coordinates fall
     * outside the snapshot. Use [contains] first when the difference matters.
     *
     * @since 3.9
     */
    fun materialAt(x: Int, y: Int, z: Int): Material

    /**
     * The full block hit at the given world coordinates, or an empty optional if outside.
     *
     * @since 3.9
     */
    fun at(x: Int, y: Int, z: Int): Optional<BlockHit>

    /**
     * Every coordinate whose material differs between this snapshot and [other]. Both snapshots
     * must cover the same cube; a mismatch throws.
     *
     * This is what makes "the floor under me disappeared", "someone broke my bed" and "the
     * structure I am copying just changed" cheap to detect without polling individual blocks.
     *
     * @throws IllegalArgumentException if the two snapshots cover different regions
     * @since 3.9
     */
    fun diff(other: BlockSnapshot): List<BlockHit>
}
