package net.blueva.spoof.api.senses

/**
 * Everything a fake player can perceive, grouped by what it is perceiving.
 *
 * Reached with [net.blueva.spoof.api.FakePlayer.senses]. Senses are the counterpart of
 * [net.blueva.spoof.api.control.Actuator]: one place to read the world, one place to act on it.
 *
 * Perception is deliberately narrower than "call anything you like on the Bukkit player". Sense
 * radius, line-of-sight filtering and reaction delay are enforced here rather than left to each
 * behaviour, because a behaviour that reads the whole world and reacts to it instantly produces a
 * bot that is obviously not a person. A behaviour that genuinely needs more can still reach the
 * Bukkit player through [net.blueva.spoof.api.FakePlayer.bukkitPlayer], and should treat needing to
 * as a sign that it is about to build something that looks fake.
 *
 * @since 3.9
 */
interface Senses {
    /** The fake player's own state. */
    fun self(): SelfSenses

    /** The blocks around the fake player. */
    fun blocks(): BlockSenses

    /** The entities around the fake player. */
    fun entities(): EntitySenses

    /** What the server has told the fake player through the interface. */
    fun hud(): HudSenses

    /**
     * The server tick these senses were last refreshed at. Within a single tick every read is
     * served from the same snapshot, so two behaviours reading the same thing see the same thing.
     *
     * @since 3.9
     */
    fun tick(): Long
}
