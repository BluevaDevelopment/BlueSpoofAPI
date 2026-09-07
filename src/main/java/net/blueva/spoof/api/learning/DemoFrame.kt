package net.blueva.spoof.api.learning

import org.bukkit.util.Vector

/**
 * One tick of a recorded player, the unit a [Demonstration] is made of.
 *
 * Frames describe what a player did, not what they saw. What they saw is reconstructed from the
 * world when the demonstration is used, which is what lets one recording stay useful after the map
 * around it changes.
 *
 * @since 3.9
 */
class DemoFrame(
    /** Tick index within the demonstration, starting at 0. */
    @JvmField val tick: Int,
    @JvmField val x: Double,
    @JvmField val y: Double,
    @JvmField val z: Double,
    @JvmField val yaw: Float,
    @JvmField val pitch: Float,
    /** Velocity at the start of this tick. */
    @JvmField val velocity: Vector,
    @JvmField val onGround: Boolean,
    @JvmField val sneaking: Boolean,
    @JvmField val sprinting: Boolean,
    @JvmField val jumping: Boolean,
    /**
     * Forward movement input, reconstructed from the change in position relative to facing. This is
     * what makes a recording replayable as *input* rather than as a sequence of teleports.
     */
    @JvmField val forwardInput: Float,
    /** Strafe movement input, reconstructed the same way. */
    @JvmField val strafeInput: Float,
    @JvmField val heldSlot: Int,
    @JvmField val attacking: Boolean,
    @JvmField val usingItem: Boolean,
    @JvmField val health: Double,
    @JvmField val food: Int,
    /** Extra per-game values a feature extractor attached at capture time. */
    @JvmField val extra: Map<String, Double>
) {
    override fun toString(): String = "DemoFrame($tick, $x, $y, $z)"
}
