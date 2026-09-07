package net.blueva.spoof.api.brain

import net.blueva.spoof.api.FakePlayer
import net.blueva.spoof.api.behavior.HumanProfile
import net.blueva.spoof.api.control.Actuator
import net.blueva.spoof.api.game.GameSession
import net.blueva.spoof.api.senses.Senses
import java.util.Optional

/**
 * Everything a [BotBrain] is handed on each callback: the bot, what it can perceive, what it can
 * do, and how it is allowed to behave.
 *
 * A context belongs to one attachment of one brain to one bot. It stops working once the brain is
 * detached, so do not hold on to it.
 *
 * @since 3.9
 */
interface BrainContext {
    /** The fake player this brain is driving. */
    fun bot(): FakePlayer

    /** What the bot can perceive. */
    fun senses(): Senses

    /** What the bot can do. */
    fun actuator(): Actuator

    /** How human this bot is required to look. */
    fun profile(): HumanProfile

    /**
     * The game the bot is currently inside, if any provider claims it. This is how a brain reads
     * arena state without knowing which plugin is running the game.
     *
     * @since 3.9
     */
    fun game(): Optional<GameSession>

    /**
     * The server tick counter for this attachment, starting at 1 on the tick the brain attached.
     *
     * @since 3.9
     */
    fun tick(): Long

    /**
     * A place for a brain to keep state between ticks without a map keyed by bot name. Cleared when
     * the brain detaches.
     *
     * @since 3.9
     */
    fun <T : Any> memory(key: String, type: Class<T>): Optional<T>

    /**
     * Stores a value in this attachment's memory. Passing `null` removes the key.
     *
     * @since 3.9
     */
    fun remember(key: String, value: Any?)

    /**
     * An optional extra service the environment can supply, for integrations that want to hand
     * their own brains something BlueSpoof knows nothing about.
     *
     * @since 3.9
     */
    fun <T : Any> capability(type: Class<T>): Optional<T>

    /**
     * Gives up control of this bot voluntarily. The brain is detached with
     * [DetachReason.YIELDED] and the next best claimant takes over, typically BlueSpoof's own
     * ambient behaviour.
     *
     * A brain that cannot do its job (a capability is missing, the arena is not configured, the
     * game is one it does not understand) should yield rather than stand still. A bot doing
     * something plausible always beats a bot doing nothing.
     *
     * @since 3.9
     */
    fun yield(reason: String)
}
