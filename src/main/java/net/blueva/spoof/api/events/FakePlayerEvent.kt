package net.blueva.spoof.api.events

import net.blueva.spoof.api.FakePlayer
import org.bukkit.event.Event

/**
 * Base class for all BlueSpoof fake-player events.
 * 
 * 
 * These are regular Bukkit events: register listeners with
 * `Bukkit.getPluginManager().registerEvents(...)` or
 * `callEvent`. Note that fake players are real server-side players, so the
 * standard Bukkit events (`PlayerJoinEvent`, `PlayerMoveEvent`,
 * `EntityDamageEvent`, …) also fire for them; these events add the
 * fake-player-specific semantics on top.
 * 
 * @since 3.7
 */
abstract class FakePlayerEvent : Event {
    /**
     * Returns the fake player this event is about.
     * 
     * @return the fake player
     * @since 3.7
     */
    val fakePlayer: FakePlayer?

    /**
     * Creates an event for the given fake player.
     * 
     * @param fakePlayer the fake player this event is about
     */
    protected constructor(fakePlayer: FakePlayer?) {
        this.fakePlayer = fakePlayer
    }

    /**
     * Creates an event for the given fake player, possibly asynchronous.
     * 
     * @param fakePlayer the fake player this event is about
     * @param async      whether the event is fired asynchronously
     */
    protected constructor(fakePlayer: FakePlayer?, async: Boolean) : super(async) {
        this.fakePlayer = fakePlayer
    }
}
