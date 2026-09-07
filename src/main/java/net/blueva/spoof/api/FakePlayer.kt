package net.blueva.spoof.api

import net.blueva.spoof.api.behavior.HumanProfile
import net.blueva.spoof.api.brain.BotBrain
import net.blueva.spoof.api.control.Actuator
import net.blueva.spoof.api.control.Capabilities
import net.blueva.spoof.api.inventory.BotInventory
import net.blueva.spoof.api.pathfinding.Pathfinder
import net.blueva.spoof.api.senses.Senses
import org.bukkit.entity.Player
import java.util.Optional
import java.util.UUID

/**
 * A fake player controlled by BlueSpoof.
 *
 * A fake player is a **real server-side player**: it has a genuine [Player] object
 * ([bukkitPlayer]), physics, health, food, inventory and a tab-list entry, and it fires the usual
 * Bukkit events (`PlayerJoinEvent`, `PlayerMoveEvent`, `EntityDamageEvent` and the rest). What it
 * lacks is a network client, and this API plays that role.
 *
 * This interface is deliberately small. It answers "who is this bot" and hands you the four things
 * you actually work with:
 *
 * - [senses] to read the world, bounded the way a real client's view is bounded
 * - [actuator] to act on it, shaped so the result looks like a person
 * - [inventory] and [pathfinder] for the two subsystems big enough to own themselves
 *
 * Anything a bot perceives is on [Senses]; anything a bot does is on [Actuator]. There is no third
 * place, which is what makes the humanisation in [HumanProfile] something a behaviour cannot
 * accidentally sidestep.
 *
 * @since 3.9
 */
interface FakePlayer {

    // ------------------------------------------------------------------
    // Identity and lifecycle
    // ------------------------------------------------------------------

    /**
     * The fake player's exact in-game name.
     *
     * @since 3.9
     */
    fun name(): String

    /**
     * The fake player's UUID.
     *
     * @throws IllegalStateException if the fake player is offline
     * @since 3.9
     */
    fun uniqueId(): UUID

    /**
     * The underlying Bukkit [Player], or `null` if the fake player is offline.
     *
     * This is a real online player object: anything that works on a real player works on it, from
     * scoreboards to permissions. Prefer [senses] and [actuator] for anything a client would do, so
     * that BlueSpoof can keep its movement simulation coherent and so that the bot keeps looking
     * like a person.
     *
     * @since 3.9
     */
    fun bukkitPlayer(): Player?

    /**
     * Whether the fake player is currently online.
     *
     * @since 3.9
     */
    fun isOnline(): Boolean

    /**
     * Disconnects the fake player from the server, firing the usual quit events.
     *
     * @since 3.9
     */
    fun disconnect()

    // ------------------------------------------------------------------
    // Perception and action
    // ------------------------------------------------------------------

    /**
     * Everything this fake player can perceive.
     *
     * @since 3.9
     */
    fun senses(): Senses

    /**
     * Everything this fake player can do.
     *
     * @since 3.9
     */
    fun actuator(): Actuator

    /**
     * This fake player's own inventory. A shortcut for `senses().self().inventory()`.
     *
     * @since 3.9
     */
    fun inventory(): BotInventory

    /**
     * The pathfinder that walks this fake player to goals.
     *
     * ```
     * bot.pathfinder().goTo(GoalNear(100, 64, -30, 2.0)).thenAccept { result ->
     *     if (result.isSuccess) bot.actuator().chat("I'm here")
     * }
     * ```
     *
     * @since 3.9
     */
    fun pathfinder(): Pathfinder

    // ------------------------------------------------------------------
    // Behaviour
    // ------------------------------------------------------------------

    /**
     * What this fake player can do on the server version it is running on.
     *
     * @since 3.9
     */
    fun capabilities(): Capabilities

    /**
     * How human this fake player is required to look. Assigned deterministically from its name
     * unless a plugin overrides it through
     * [net.blueva.spoof.api.behavior.HumanProfileRegistry.assign].
     *
     * @since 3.9
     */
    fun profile(): HumanProfile

    /**
     * The behaviour currently driving this fake player, if any.
     *
     * @since 3.9
     */
    fun brain(): Optional<BotBrain>
}
