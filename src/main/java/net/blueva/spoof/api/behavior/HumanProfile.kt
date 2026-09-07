package net.blueva.spoof.api.behavior

import java.util.Random

/**
 * The behavioural fingerprint of one fake player: how fast it reacts, how accurately it aims, how
 * often it makes a mistake, and how many actions per minute it is allowed.
 *
 * A profile is not decoration. It is enforced by
 * [net.blueva.spoof.api.control.Actuator] on every action a behaviour requests, and a behaviour
 * cannot opt out of it. A bot that aims perfectly, reacts in zero ticks and never misplays reads as
 * a bot within seconds of being watched, no matter how good the rest of its decision making is.
 *
 * Profiles are assigned deterministically from the fake player's name, so the same bot keeps the
 * same fingerprint for as long as it exists. Consistency is itself a realism cue: a player whose
 * reaction time swings wildly from minute to minute does not look human either.
 *
 * @since 3.9
 */
interface HumanProfile {
    /**
     * The profile's id, for example `casual`, `tryhard` or `distracted`.
     *
     * @since 3.9
     */
    fun id(): String

    /**
     * A reaction delay in ticks, sampled fresh on each call rather than fixed, so repeated
     * decisions do not land on a metronome.
     *
     * @since 3.9
     */
    fun reactionTicks(): Int

    /**
     * The residual aiming error in degrees that this bot never converges past. Applied to every
     * rotation, including [net.blueva.spoof.api.control.AimStyle.SNAP].
     *
     * @since 3.9
     */
    fun aimErrorDegrees(): Double

    /**
     * The maximum rotation in degrees this bot turns its head in one tick.
     *
     * @since 3.9
     */
    fun aimSpeedDegreesPerTick(): Double

    /**
     * The probability, per decision, that this bot deliberately does the wrong thing: takes a worse
     * route, hesitates on a jump, swings at nothing, opens the wrong chest.
     *
     * @since 3.9
     */
    fun misplayChance(): Double

    /**
     * The probability, per tick of standing still, that this bot fidgets: a small look around, a
     * hop, a hotbar cycle.
     *
     * @since 3.9
     */
    fun idleFidgetChance(): Double

    /**
     * The ceiling on how many actuator actions this bot may perform per minute. Requests over
     * budget are dropped rather than queued, exactly like a person who cannot click that fast.
     *
     * @since 3.9
     */
    fun actionsPerMinuteBudget(): Int

    /**
     * How good this bot is allowed to be, from `0.0` (plays badly on purpose) to `1.0` (plays as
     * well as the best demonstration available). Learned policies interpolate their output towards
     * the median of what humans did rather than the best of it, in proportion to this.
     *
     * @since 3.9
     */
    fun skill(): Double

    /**
     * A random source seeded from the fake player's name, so a bot's choices are reproducible for
     * as long as it is connected.
     *
     * @since 3.9
     */
    fun random(): Random
}
