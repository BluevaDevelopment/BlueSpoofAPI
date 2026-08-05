package net.blueva.spoof.api.events

import net.blueva.spoof.api.FakePlayer
import org.bukkit.event.HandlerList

/**
 * Fired when a fake player is about to disconnect from the server.
 * 
 * 
 * This fires in addition to the standard `PlayerQuitEvent`. Any
 * navigation in progress is cancelled and pending action futures complete
 * exceptionally.
 * 
 * @since 3.7
 */
class FakePlayerQuitEvent
/**
 * Creates the event.
 * 
 * @param fakePlayer the fake player that is disconnecting
 * @param reason     human-readable disconnect reason (e.g. `"disconnect"`,
 * `"plugin disable"`)
 */(
    fakePlayer: FakePlayer?,
    /**
     * Returns the human-readable disconnect reason.
     * 
     * @return disconnect reason
     * @since 3.7
     */
    val reason: String?
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
