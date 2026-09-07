package net.blueva.spoof.api.brain

/**
 * A one-off notification delivered to a [BotBrain] outside its regular tick.
 *
 * Signals exist so that a brain does not have to poll for things that are naturally events. The
 * built-in kinds cover what most behaviours need; [kind] is a plain string rather than an enum so
 * that a game integration can deliver its own without every integration needing an API change.
 *
 * @since 3.9
 */
class BrainSignal(
    /** What happened, for example `damaged`, `died`, `respawned`, `game_phase_changed`. */
    @JvmField val kind: String,
    /** Whatever the sender attached, or `null`. Interpretation is up to the sender and receiver. */
    @JvmField val payload: Any?
) {
    override fun toString(): String = "BrainSignal($kind)"

    companion object {
        /** The fake player took damage. Payload is the damage source entity, when there is one. */
        const val DAMAGED: String = "damaged"

        /** The fake player died. */
        const val DIED: String = "died"

        /** The fake player respawned. */
        const val RESPAWNED: String = "respawned"

        /** The fake player was teleported by something other than its own brain. */
        const val TELEPORTED: String = "teleported"

        /** The game the fake player is in changed phase. Payload is the new phase. */
        const val GAME_PHASE_CHANGED: String = "game_phase_changed"

        /** The fake player was eliminated from its game. Payload is the reason, when there is one. */
        const val ELIMINATED: String = "eliminated"
    }
}
