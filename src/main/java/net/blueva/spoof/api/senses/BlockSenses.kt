package net.blueva.spoof.api.senses

import org.bukkit.Material
import java.util.Optional
import java.util.function.Predicate

/**
 * What a fake player can see of the blocks around it.
 *
 * Every lookup is bounded by the configured sense radius. A block outside it is not returned, and
 * that is deliberate rather than a limitation: a behaviour must not be able to read the whole world
 * and act on information a real client could not have.
 *
 * Reads within one tick are served from a snapshot shared by every behaviour on the server, so
 * asking repeatedly is cheap.
 *
 * @since 3.9
 */
interface BlockSenses {
    /**
     * The block at the given coordinates, or an empty optional if it is outside the sense radius or
     * in an unloaded chunk.
     *
     * @since 3.9
     */
    fun at(x: Int, y: Int, z: Int): Optional<BlockHit>

    /**
     * The block the fake player is standing on, if any.
     *
     * @since 3.9
     */
    fun below(): Optional<BlockHit>

    /**
     * Every block within [radius] matching [match], nearest first, capped at [limit] results.
     *
     * @param radius search radius in blocks, clamped to the configured sense radius
     * @since 3.9
     */
    fun find(match: Predicate<BlockHit>, radius: Int, limit: Int): List<BlockHit>

    /**
     * Convenience for [find] matching a single material.
     *
     * @since 3.9
     */
    fun findMaterial(material: Material, radius: Int, limit: Int): List<BlockHit> =
        find(Predicate { it.material == material }, radius, limit)

    /**
     * The first non-passable block along the fake player's line of sight, or an empty optional if
     * nothing is hit within [maxDistance]. The returned hit carries the face that was struck.
     *
     * @since 3.9
     */
    fun raycast(maxDistance: Double): Optional<BlockHit>

    /**
     * An immutable cube of blocks centred on the fake player, for behaviours that need to reason
     * over a whole area at once (route planning, floor scanning, structure comparison) rather than
     * poking at individual coordinates.
     *
     * @param radius half-width of the cube, clamped to the configured sense radius
     * @since 3.9
     */
    fun snapshot(radius: Int): BlockSnapshot
}
