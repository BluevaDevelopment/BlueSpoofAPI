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
     * Whether [Actuator.moveInput] drives real client-style analog input. When `false`, analog
     * input is quantised onto the seven [net.blueva.spoof.api.ControlState] keys by a
     * physics-approximating stepper, which is accurate enough for walking but not for
     * frame-precise parkour.
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
