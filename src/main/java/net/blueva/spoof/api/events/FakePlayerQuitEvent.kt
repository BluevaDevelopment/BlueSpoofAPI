package net.blueva.spoof.api.events

import net.blueva.spoof.api.FakePlayer
import org.bukkit.event.HandlerList

/**
 * Fired when a fake player is about to disconnect.
 *
 * This fires in addition to the standard `PlayerQuitEvent`. Any navigation in progress is
 * cancelled, any attached behaviour is detached, and pending action futures complete
 * exceptionally.
 *
 * @since 3.9
 */
class FakePlayerQuitEvent(
    fakePlayer: FakePlayer,
    /** Human-readable disconnect reason, for example `disconnect` or `plugin disable`. */
    val reason: String
) : FakePlayerEvent(fakePlayer) {

    override fun getHandlers(): HandlerList = handlerList

    companion object {
        /** Bukkit handler list accessor. */
        @JvmStatic
        val handlerList: HandlerList = HandlerList()
    }
}
