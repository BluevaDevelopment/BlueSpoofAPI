package net.blueva.spoof.api.senses

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.BlockFace

/**
 * A block a fake player can perceive, as an immutable snapshot rather than a live
 * `org.bukkit.block.Block` handle.
 *
 * Snapshots are deliberate: senses are read from behaviours that may run a tick or more after the
 * read, and handing out a live block would let a behaviour observe changes it should not have
 * noticed yet. [material] plus [data] describe what the block was when it was sensed.
 *
 * [data] carries the modern block-data string (the same form `BlockData.asString()` produces, for
 * example `minecraft:oak_stairs[facing=north,half=bottom]`) on server versions that have it, and
 * is `null` on legacy versions where only [material] is meaningful. Behaviours that need to tell
 * two states of the same material apart must tolerate it being absent.
 *
 * @since 3.9
 */
class BlockHit(
    /** Block X. */
    @JvmField val x: Int,
    /** Block Y. */
    @JvmField val y: Int,
    /** Block Z. */
    @JvmField val z: Int,
    /** The block's material. */
    @JvmField val material: Material,
    /** The block-data string on modern versions, `null` on legacy ones. */
    @JvmField val data: String?,
    /** The face that was hit, for raycast results; `null` for plain lookups. */
    @JvmField val face: BlockFace?
) {
    /**
     * Creates a hit with no face information.
     *
     * @since 3.9
     */
    constructor(x: Int, y: Int, z: Int, material: Material, data: String?) :
        this(x, y, z, material, data, null)

    /** Returns the location of this block's corner in the given world. */
    fun toLocation(world: World): Location = Location(world, x.toDouble(), y.toDouble(), z.toDouble())

    /** Returns the location of the centre of this block in the given world. */
    fun toCenterLocation(world: World): Location = Location(world, x + 0.5, y + 0.5, z + 0.5)

    /** Returns whether this block is air of any kind. */
    val isAir: Boolean
        get() = material == Material.AIR || material.name == "CAVE_AIR" || material.name == "VOID_AIR"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BlockHit) return false
        return x == other.x && y == other.y && z == other.z &&
            material == other.material && data == other.data && face == other.face
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        result = 31 * result + material.hashCode()
        result = 31 * result + (data?.hashCode() ?: 0)
        result = 31 * result + (face?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String = "BlockHit($x, $y, $z, $material)"
}
