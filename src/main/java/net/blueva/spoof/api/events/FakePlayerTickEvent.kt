package net.blueva.spoof.api.events

import net.blueva.spoof.api.FakePlayer
import org.bukkit.event.HandlerList

/**
 * Fired every server tick while a fake player is under active API control
 * (movement controls held, looking, or navigating).
 * 
 * 
 * Useful for scripting behaviours that must steer the fake player every
 * tick — the analogue of Mineflayer's `physicsTick`. The event is only
 * fired while the fake player's locomotion engine is engaged, and is skipped
 * entirely when no listener is registered, so idle fake players cost nothing.
 * 
 * @since 3.7
 */
class FakePlayerTickEvent
/**
 * Creates the event.
 * 
 * @param fakePlayer the fake player being controlled
 * @param tick       monotonically increasing per-bot tick counter
 */(
    fakePlayer: FakePlayer?,
    /**
     * Returns the per-bot tick counter for this event.
     * 
     * @return tick counter (starts at 1 each time the engine engages)
     * @since 3.7
     */
    val tick: Long
) : FakePlayerEvent(fakePlayer) {
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        /**
         * Bukkit handler list accessor.
         * 
         * @return the handler list
         */
        val handlerList: HandlerList = HandlerList()
    }
}
