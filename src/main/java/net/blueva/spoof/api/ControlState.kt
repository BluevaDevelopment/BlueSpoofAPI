package net.blueva.spoof.api

/**
 * Movement input that can be held pressed on a fake player, exactly like the
 * seven keyboard controls a real Minecraft client exposes.
 * 
 * 
 * Control states behave like **held keys**, not impulses:
 * [setControlState(FORWARD, true)][FakePlayer.setControlState]
 * makes the fake player walk forward (in the direction it is facing) every tick
 * until the state is set back to `false`. Combine with
 * [lookAt][FakePlayer.lookAt] to steer.
 * 
 * @since 3.7
 */
enum class ControlState {
    /** Walk forward (the `W` key).  */
    FORWARD,

    /** Walk backward (the `S` key).  */
    BACKWARD,

    /** Strafe left (the `A` key).  */
    LEFT,

    /** Strafe right (the `D` key).  */
    RIGHT,

    /**
     * Hold jump (the `Space` key). While held, the fake player jumps again
     * as soon as it lands, exactly like a real client holding space — and swims
     * upward in liquids.
     */
    JUMP,

    /** Sprint (double-tap `W` / the sprint key). Affects speed and jump boost.  */
    SPRINT,

    /** Sneak (the `Shift` key). Slows movement to 30% and shows the crouch pose.  */
    SNEAK
}
