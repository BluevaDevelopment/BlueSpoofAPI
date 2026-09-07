package net.blueva.spoof.api.control

/**
 * What a given fake player can actually do on the server version it is running on.
 *
 * BlueSpoof supports Minecraft 1.8.8 through current, and some actions need a native server hook
 * that only exists on recent versions. Rather than discovering that by catching an exception on a
 * hot path, query this once (typically when a
 * [net.blueva.spoof.api.brain.BotBrain] attaches) and pick a behaviour that fits.
 *
 * ```
 * if (!bot.capabilities().digging()) {
 *     ctx.yield("this game needs digging and this server is too old")
 *     return
 * }
 * ```
 *
 * @since 3.9
 */
interface Capabilities {
    /**
     * Whether [AdvancedActuator.dig] and [AdvancedActuator.stopDigging] work.
     *
     * @since 3.9
     */
    fun digging(): Boolean

    /**
     * Whether [AdvancedActuator.useItem], [AdvancedActuator.useItemOn],
     * [AdvancedActuator.interact] and [AdvancedActuator.releaseUseItem] work.
     *
     * @since 3.9
     */
    fun itemUse(): Boolean

    /**
     * Whether [AdvancedActuator.craft] works.
     *
     * @since 3.9
     */
    fun crafting(): Boolean

    /**
     * Whether clicking inside an opened container works. Reading a container's contents works on
     * every version; only clicking needs the native hook.
     *
     * @since 3.9
     */
    fun containerClicks(): Boolean

    /**
     * Whether movement is resolved by the server's own physics rather than approximated.
     *
     * [Actuator.moveInput] takes analog axes on every supported version: they reach the same
     * impulses a real client sends, so a bot can walk at two thirds speed anywhere. What differs is
     * what happens next. When this is `true` the server's own `travel()` resolves friction,
     * acceleration, gravity and collisions exactly as it does for a real player. When it is `false`
     * a stepper approximates that, which is accurate enough for walking and for reaching a
     * destination, but not for movement that has to be frame-exact, such as a parkour jump measured
     * in fractions of a block.
     *
     * @since 3.9
     */
    fun analogMovement(): Boolean

    /**
     * Whether [net.blueva.spoof.api.senses.HudSenses] receives the scoreboard, titles, action bar
     * and messages sent to this fake player.
     *
     * @since 3.9
     */
    fun hudInterception(): Boolean

    /**
     * The data version of the running server, or `-1` when it cannot be determined. Useful as a
     * last-resort version check; prefer the named capabilities above.
     *
     * @since 3.9
     */
    fun minecraftDataVersion(): Int
}
