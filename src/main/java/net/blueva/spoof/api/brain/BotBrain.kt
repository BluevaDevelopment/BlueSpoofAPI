package net.blueva.spoof.api.brain

/**
 * A behaviour that drives one fake player.
 *
 * A brain is attached to a bot by the [BrainRegistry] when its factory wins the claim for that bot,
 * ticked every server tick for as long as it stays attached, and detached when something else
 * claims the bot, when it yields, or when the bot goes away.
 *
 * Brains do not have to be complete. A brain that only knows what to do during a countdown can
 * yield the rest of the time, and something else picks the bot up. Composing several narrow brains
 * through priorities is the intended design, not a workaround.
 *
 * Implementations must not block. [onTick] runs on the bot's owning thread, on the server's tick
 * loop, once per bot per tick. Anything expensive (loading demonstrations, computing a long path,
 * touching a database) belongs off-thread with the result applied on a later tick. A brain that
 * throws repeatedly is detached with [DetachReason.FAILED] rather than being allowed to take the
 * server down with it.
 *
 * ```
 * class CircleBrain : BotBrain {
 *     override fun id() = "circle"
 *     override fun onTick(ctx: BrainContext) {
 *         ctx.actuator().aim((ctx.tick() * 4).toDouble(), 0.0, AimStyle.TRACK)
 *         ctx.actuator().moveInput(MoveInput.FORWARD)
 *     }
 * }
 * ```
 *
 * @since 3.9
 */
interface BotBrain {
    /**
     * A stable id for this brain, used in logs and in operator tooling.
     *
     * @since 3.9
     */
    fun id(): String

    /**
     * Called once, on the tick this brain takes control of the bot.
     *
     * This is the place to check [net.blueva.spoof.api.control.Capabilities] and
     * [BrainContext.game], and to [BrainContext.yield] straight away if this brain cannot actually
     * do the job on this server or in this game.
     *
     * @since 3.9
     */
    fun onAttach(ctx: BrainContext) {}

    /**
     * Called every server tick while this brain is attached.
     *
     * @since 3.9
     */
    fun onTick(ctx: BrainContext)

    /**
     * Called once, when this brain loses control of the bot. Release anything the brain was
     * holding; movement input is cleared by the registry either way.
     *
     * @since 3.9
     */
    fun onDetach(ctx: BrainContext, reason: DetachReason) {}

    /**
     * Called when something happens that a brain should not have to poll for.
     *
     * @since 3.9
     */
    fun onSignal(ctx: BrainContext, signal: BrainSignal) {}
}
