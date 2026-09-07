package net.blueva.spoof.api.brain

import net.blueva.spoof.api.FakePlayer

/**
 * Decides which fake players a kind of behaviour wants to drive, and creates one [BotBrain] per bot
 * it wins.
 *
 * Registered with [BrainRegistry.register]. The registry re-evaluates claims periodically, so a
 * factory expresses "I want this bot right now" rather than having to hook every event that might
 * make a bot interesting.
 *
 * @since 3.9
 */
interface BotBrainFactory {
    /**
     * A stable id, unique across every registered factory. Registering a second factory with the
     * same id replaces the first.
     *
     * @since 3.9
     */
    fun id(): String

    /**
     * How strongly this factory outranks others. The highest-priority factory that claims a bot
     * gets it; ties keep whichever brain is already attached, so a bot is never handed back and
     * forth between two equally ranked behaviours.
     *
     * BlueSpoof's own ambient behaviour sits at 0 and a server operator taking manual control
     * always outranks everything, so a plugin can safely pick any positive number. Integrations
     * that drive a bot through a whole minigame should sit near 100; narrow behaviours that only
     * apply in a specific moment should sit above whatever they need to interrupt.
     *
     * @since 3.9
     */
    fun priority(): Int

    /**
     * Whether this factory wants to drive the given bot at this moment.
     *
     * Called often, for every fake player on the server. Keep it cheap: a field read and a
     * comparison, not a world scan or a database query.
     *
     * @since 3.9
     */
    fun claims(bot: FakePlayer): Boolean

    /**
     * Creates a brain for a bot this factory has just won. Called once per attachment.
     *
     * @since 3.9
     */
    fun create(bot: FakePlayer): BotBrain
}
