package net.blueva.spoof.api.events

import net.blueva.spoof.api.FakePlayer
import org.bukkit.event.HandlerList

/**
 * Fired when a fake player has fully joined the server and is ready to be
 * controlled through the API.
 * 
 * 
 * This fires in addition to the standard `PlayerJoinEvent`, once the
 * fake player's physics, skin and state are fully initialised.
 * 
 * @since 3.7
 */
class FakePlayerSpawnEvent
/**
 * Creates the event.
 * 
 * @param fakePlayer the fake player that joined
 */
    (fakePlayer: FakePlayer?) : FakePlayerEvent(fakePlayer) {
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
