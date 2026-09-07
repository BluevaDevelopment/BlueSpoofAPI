package net.blueva.spoof.api.events

import net.blueva.spoof.api.FakePlayer
import org.bukkit.event.HandlerList

/**
 * Fired when a fake player has fully joined the server and is ready to be controlled.
 *
 * This fires in addition to the standard `PlayerJoinEvent`, once the fake player's physics, skin
 * and state are fully initialised.
 *
 * @since 3.9
 */
class FakePlayerSpawnEvent(fakePlayer: FakePlayer) : FakePlayerEvent(fakePlayer) {

    override fun getHandlers(): HandlerList = handlerList

    companion object {
        /** Bukkit handler list accessor. */
        @JvmStatic
        val handlerList: HandlerList = HandlerList()
    }
}
