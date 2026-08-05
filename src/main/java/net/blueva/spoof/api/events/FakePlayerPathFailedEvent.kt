package net.blueva.spoof.api.events

import net.blueva.spoof.api.FakePlayer
import net.blueva.spoof.api.pathfinding.Goal
import net.blueva.spoof.api.pathfinding.PathResult
import org.bukkit.event.HandlerList

/**
 * Fired when the pathfinder of a fake player fails to reach its goal
 * (no path, timeout, stuck, cancelled or interrupted).
 * 
 * @since 3.7
 */
class FakePlayerPathFailedEvent
/**
 * Creates the event.
 * 
 * @param fakePlayer the fake player whose navigation ended
 * @param goal       the goal that was being pursued, possibly `null`
 * @param result     the navigation result with the failure status
 */(
    fakePlayer: FakePlayer?,
    /**
     * Returns the goal that was being pursued.
     * 
     * @return the goal, or `null` if it had already been cleared
     * @since 3.7
     */
    val goal: Goal?,
    /**
     * Returns the navigation result describing the failure.
     * 
     * @return the result (status is never
     * [net.blueva.spoof.api.pathfinding.PathStatus.SUCCESS])
     * @since 3.7
     */
    val result: PathResult?
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
