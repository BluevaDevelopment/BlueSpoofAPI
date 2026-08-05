package net.blueva.spoof.api.events

import net.blueva.spoof.api.FakePlayer
import net.blueva.spoof.api.pathfinding.Goal
import net.blueva.spoof.api.pathfinding.PathResult
import org.bukkit.event.HandlerList

/**
 * Fired when the pathfinder of a fake player reaches its goal.
 * 
 * @since 3.7
 */
class FakePlayerGoalReachedEvent
/**
 * Creates the event.
 * 
 * @param fakePlayer the fake player that reached its goal
 * @param goal       the goal that was reached
 * @param result     the navigation result
 */(
    fakePlayer: FakePlayer?,
    /**
     * Returns the goal that was reached.
     * 
     * @return the goal
     * @since 3.7
     */
    val goal: Goal?,
    /**
     * Returns the navigation result.
     * 
     * @return the result (status is always
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
