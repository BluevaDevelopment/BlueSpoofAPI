package net.blueva.spoof.api.control

import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * A single tick of analog movement input, in the fake player's own facing frame: exactly what a
 * real client sends every tick.
 *
 * [forward] is positive towards where the bot is looking, [strafe] is positive to its right. Both
 * are clamped to `-1.0 .. 1.0`, and the pair is clamped to a unit disc so that holding forward and
 * right at once does not move the bot faster than holding one of them, matching vanilla.
 *
 * @since 3.9
 */
class MoveInput(forward: Float, strafe: Float, val jump: Boolean, val sneak: Boolean, val sprint: Boolean) {

    /** Forward axis, `-1.0` (backwards) to `1.0` (forwards). */
    val forward: Float

    /** Strafe axis, `-1.0` (left) to `1.0` (right). */
    val strafe: Float

    init {
        val clampedForward = max(-1.0f, min(1.0f, forward))
        val clampedStrafe = max(-1.0f, min(1.0f, strafe))
        val magnitude = sqrt(clampedForward * clampedForward + clampedStrafe * clampedStrafe)
        if (magnitude > 1.0f) {
            this.forward = clampedForward / magnitude
            this.strafe = clampedStrafe / magnitude
        } else {
            this.forward = clampedForward
            this.strafe = clampedStrafe
        }
    }

    /**
     * Creates an input with no modifier keys held.
     *
     * @since 3.9
     */
    constructor(forward: Float, strafe: Float) : this(forward, strafe, false, false, false)

    /** Whether this input asks for any movement at all. */
    val isMoving: Boolean
        get() = forward != 0.0f || strafe != 0.0f

    /** Returns a copy of this input with [jump] set. */
    fun jumping(jump: Boolean): MoveInput = MoveInput(forward, strafe, jump, sneak, sprint)

    /** Returns a copy of this input with [sneak] set. */
    fun sneaking(sneak: Boolean): MoveInput = MoveInput(forward, strafe, jump, sneak, sprint)

    /** Returns a copy of this input with [sprint] set. */
    fun sprinting(sprint: Boolean): MoveInput = MoveInput(forward, strafe, jump, sneak, sprint)

    override fun toString(): String =
        "MoveInput(forward=$forward, strafe=$strafe, jump=$jump, sneak=$sneak, sprint=$sprint)"

    companion object {
        /** No input at all: the bot decelerates with vanilla friction. */
        @JvmField
        val NONE: MoveInput = MoveInput(0.0f, 0.0f)

        /** Full forward, no modifiers. */
        @JvmField
        val FORWARD: MoveInput = MoveInput(1.0f, 0.0f)

        /** Full forward while sprinting. */
        @JvmField
        val SPRINT_FORWARD: MoveInput = MoveInput(1.0f, 0.0f, false, false, true)
    }
}
