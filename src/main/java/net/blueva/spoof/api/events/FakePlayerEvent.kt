package net.blueva.spoof.api.events

import net.blueva.spoof.api.FakePlayer
import org.bukkit.event.Event

/**
 * Base class for BlueSpoof fake-player events.
 *
 * These are regular Bukkit events. Note that fake players are real server-side players, so the
 * standard Bukkit events (`PlayerJoinEvent`, `PlayerMoveEvent`, `EntityDamageEvent` and the rest)
 * fire for them too; these add the fake-player-specific semantics on top.
 *
 * BlueSpoof deliberately ships very few of these. Anything that happens per tick, or that concerns
 * one bot rather than the server, is delivered to that bot's
 * [net.blueva.spoof.api.brain.BotBrain] instead: a Bukkit event per bot per tick costs far more
 * than it is worth, and a behaviour that reacts to its own bot does not want to be told about
 * everyone else's.
 *
 * @since 3.9
 */
abstract class FakePlayerEvent : Event {

    /** The fake player this event is about. */
    val fakePlayer: FakePlayer

    protected constructor(fakePlayer: FakePlayer) {
        this.fakePlayer = fakePlayer
    }

    protected constructor(fakePlayer: FakePlayer, async: Boolean) : super(async) {
        this.fakePlayer = fakePlayer
    }
}
