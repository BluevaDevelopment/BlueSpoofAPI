package net.blueva.spoof.api.control

/**
 * How a fake player moves its camera towards a target rotation.
 *
 * Every style except [SNAP] is shaped by the bot's
 * [net.blueva.spoof.api.behavior.HumanProfile]: the turn is spread over several ticks, never
 * reaches the target exactly, and never turns faster than the profile allows. [SNAP] bypasses the
 * smoothing but not the profile's residual aim error, so it is still not a perfect rotation.
 *
 * @since 3.9
 */
enum class AimStyle {
    /**
     * Turn in a single tick. Use for scripted, non-competitive rotations (facing a spawn platform,
     * looking at a menu). Using this to track an opponent is what makes a bot look like an aimbot.
     */
    SNAP,

    /**
     * A fast, slightly overshooting turn, like a player flicking their mouse. Best for reacting to
     * something that just appeared.
     */
    FLICK,

    /**
     * A continuous, damped follow of a moving target. Best for combat and for watching another
     * player walk past.
     */
    TRACK,

    /**
     * A brief look away and back, like a player checking their surroundings. The camera returns to
     * roughly its previous rotation on its own.
     */
    GLANCE
}
