package net.blueva.spoof.api.brain

import net.blueva.spoof.api.FakePlayer
import java.util.Optional

/**
 * Where behaviours are registered and arbitrated.
 *
 * Reached with `BlueSpoofAPI.brains()`.
 *
 * The rules, which exist so that two plugins driving bots on the same server do not tear them
 * apart:
 *
 * - At most one [BotBrain] is attached to a fake player at a time.
 * - Claims are re-evaluated periodically. The highest-priority [BotBrainFactory] whose
 *   [BotBrainFactory.claims] returns `true` wins the bot; a tie keeps the incumbent.
 * - A server operator driving a bot by command outranks every registered factory.
 * - BlueSpoof's own ambient behaviour is registered like any other factory, at priority 0, so it
 *   composes with third-party behaviour through the same mechanism instead of racing it.
 *
 * @since 3.9
 */
interface BrainRegistry {
    /**
     * Registers a factory, replacing any factory already registered with the same id.
     *
     * @since 3.9
     */
    fun register(factory: BotBrainFactory)

    /**
     * Unregisters a factory by id. Any brain it currently has attached is detached with
     * [DetachReason.PLUGIN_DISABLED].
     *
     * @since 3.9
     */
    fun unregister(id: String)

    /**
     * Every registered factory id, highest priority first.
     *
     * @since 3.9
     */
    fun registered(): List<String>

    /**
     * The brain currently attached to the given fake player, if any.
     *
     * @since 3.9
     */
    fun active(bot: FakePlayer): Optional<BotBrain>

    /**
     * Forces a fresh claim evaluation for the given bot, instead of waiting for the next periodic
     * one. Useful right after something changes that a factory's [BotBrainFactory.claims] depends
     * on, such as a bot joining an arena.
     *
     * @since 3.9
     */
    fun reevaluate(bot: FakePlayer)

    /**
     * Delivers a signal to whichever brain is currently attached to the bot, if any.
     *
     * @since 3.9
     */
    fun signal(bot: FakePlayer, signal: BrainSignal)

    /**
     * Detaches whatever brain is driving the bot and prevents any factory from claiming it until
     * [release] is called. This is how a server operator takes manual control.
     *
     * @since 3.9
     */
    fun suspend(bot: FakePlayer, reason: String)

    /**
     * Lifts a [suspend], letting factories claim the bot again.
     *
     * @since 3.9
     */
    fun release(bot: FakePlayer)

    /**
     * Whether the given bot is currently suspended from automatic control.
     *
     * @since 3.9
     */
    fun isSuspended(bot: FakePlayer): Boolean
}
